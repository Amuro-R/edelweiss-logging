package org.edelweiss.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.edelweiss.logging.annotation.ELog;
import org.edelweiss.logging.aspect.executor.ConsoleLogExecutor;
import org.edelweiss.logging.aspect.executor.LogExecutor;
import org.edelweiss.logging.aspect.part.StringPart;
import org.edelweiss.logging.aspect.processor.NopResultPostProcessor;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;
import org.edelweiss.logging.context.LogContext;
import org.edelweiss.logging.context.TemplateHandlerContext;
import org.edelweiss.logging.el.LogEvaluationContext;
import org.edelweiss.logging.el.LogExpressionEvaluator;
import org.edelweiss.logging.el.LogParseFunctionRegistry;
import org.edelweiss.logging.pojo.eo.LogLevelEnum;
import org.edelweiss.logging.pojo.eo.ResultTypeEnum;
import org.edelweiss.logging.pojo.po.LogPO;
import org.edelweiss.logging.properties.LogProperties;
import org.edelweiss.logging.registry.LogExecutorRegistry;
import org.edelweiss.logging.registry.LogResultPostProcessorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static org.edelweiss.logging.properties.LogProperties.*;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Slf4j
@Aspect
public class LogAspect {

    private final StandardReflectionParameterNameDiscoverer nameDiscoverer = new StandardReflectionParameterNameDiscoverer();
    private final LogExpressionEvaluator expressionEvaluator = new LogExpressionEvaluator();

    @Autowired
    private LogParseFunctionRegistry logParseFunctionRegistry;
    @Autowired
    private LogExecutorRegistry logExecutorRegistry;
    @Autowired
    private LogResultPostProcessorRegistry logResultPostProcessorRegistry;
    @Autowired
    private LogProperties logProperties;
    @Autowired
    @Qualifier("edelweiss.log.thread.pool.default")
    private ThreadPoolExecutor threadPoolExecutor;

    @Pointcut("@annotation(org.edelweiss.logging.annotation.ELog)")
    public void logCutPoint() {
    }

    @Around("org.edelweiss.logging.aspect.LogAspect.logCutPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        Object[] args = pjp.getArgs();
        ELog logOnMethod = method.getAnnotation(ELog.class);
        ELog logOnClass = clazz.getAnnotation(ELog.class);

        MethodExecuteResult methodExecuteResult = new MethodExecuteResult();
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        TemplateHandlerContext templateHandlerContext = null;

        if (!logProperties.isEnable()) {
            return pjp.proceed(args);
        }

        try {
            templateHandlerContext = this.logBefore(method, args, logOnMethod, logOnClass, methodExecuteResult, methodKey);
        } catch (Exception e) {
            methodExecuteResult.setLogBeforeFail(true);
            log.error("before log error", e);
        }

        Object result = null;
        try {
            result = pjp.proceed(args);
        } catch (Throwable e) {
            methodExecuteResult.setBusinessFail(true);
            methodExecuteResult.setBusinessException(e);
        }

        try {
            LogPO logPO = this.logAfter(method, args, logOnMethod, logOnClass, methodExecuteResult, methodKey, result, templateHandlerContext);

            this.doLogExecute(logOnMethod, logOnClass, logPO);

        } catch (Exception e) {
            log.error("后执行日志异常", e);
        } finally {
            LogContext.removeLogAttributes();
        }

        if (methodExecuteResult.getBusinessException() != null) {
            throw methodExecuteResult.getBusinessException();
        }

        return result;
    }

    private void doLogExecute(ELog logOnMethod, ELog logOnClass, LogPO logPO) {
        LinkedHashSet<LogExecutorItemProp> executors = new LinkedHashSet<>();
        LogExecutorItemProp globalExecutor = Optional.ofNullable(logProperties.getExecutor()).map(LogExecutorProp::getGlobal).orElse(null);
        if (!ObjectUtils.isEmpty(logOnMethod.executors())) {
            executors.addAll(Arrays.stream(logOnMethod.executors()).map(LogExecutorItemProp::new).collect(Collectors.toList()));
        } else if (!ObjectUtils.isEmpty(logOnClass.executors())) {
            executors.addAll(Arrays.stream(logOnClass.executors()).map(LogExecutorItemProp::new).collect(Collectors.toList()));
        } else if (globalExecutor != null) {
            executors.add(globalExecutor);
        } else {
            executors.add(new LogExecutorItemProp(ConsoleLogExecutor.class));
        }

        for (LogExecutorItemProp executor : executors) {
            Class<? extends LogExecutor> clazz = executor.getClazz();
            LogExecutor logExecutor = logExecutorRegistry.get(clazz);
            if (logExecutor == null) {
                log.warn("未找到对应的执行器, clazz:{}", clazz);
                continue;
            }
            try {
                if (executor.isAsync() && threadPoolExecutor != null) {
                    threadPoolExecutor.execute(() -> {
                        logExecutor.execute(logPO);
                    });
                } else {
                    logExecutor.execute(logPO);
                }
            } catch (Exception e) {
                log.error("日志执行器执行失败, clazz:" + clazz, e);
            }
        }
    }

    private TemplateHandlerContext logBefore(Method method, Object[] args, ELog logOnMethod, ELog logOnClass,
                                             MethodExecuteResult methodExecuteResult, AnnotatedElementKey methodKey) {
        LogContext.createCurrentStackFrame();

        String successTemplate = logOnMethod.successTemplate();
        String failTemplate = logOnMethod.failTemplate();

        successTemplate = this.integrateTemplatePrefix(successTemplate, logOnClass);
        failTemplate = this.integrateTemplatePrefix(failTemplate, logOnClass);

        this.handleAnnotationValue(logOnMethod, logOnClass);

        LogEvaluationContext beforeEvaluationContext = new LogEvaluationContext(null, method, args, nameDiscoverer);

        LogTemplateHandler successHandler = this.createHandlerAndParseTemplate(successTemplate, logParseFunctionRegistry,
                expressionEvaluator, methodKey, beforeEvaluationContext, true);

        LogTemplateHandler failHandler = null;
        if (!failTemplate.trim().isEmpty()) {
            methodExecuteResult.setHasFailTemplate(true);
            failHandler = this.createHandlerAndParseTemplate(failTemplate, logParseFunctionRegistry, expressionEvaluator,
                    methodKey, beforeEvaluationContext, true);
        }

        return new TemplateHandlerContext(successHandler, failHandler);
    }

    private LogPO logAfter(Method method, Object[] args, ELog logOnMethod, ELog logOnClass,
                           MethodExecuteResult methodExecuteResult, AnnotatedElementKey methodKey, Object result,
                           TemplateHandlerContext templateHandlerContext) throws Throwable {

        if (methodExecuteResult.isLogBeforeFail()) {
            throw new RuntimeException("before日志异常，跳过后续日志解析");
        }

        this.resultPostProcess(logOnMethod, logOnClass, result, methodExecuteResult);

        LogEvaluationContext afterEvaluationContext = new LogEvaluationContext(null, method, args, nameDiscoverer);

        String resultName = this.getResultName(logOnMethod, logOnClass);
        afterEvaluationContext.setVariable(resultName, result);

        ResultTypeEnum resultType = this.getResultType(methodExecuteResult);
        List<StringPart> resultPartList = this.getStringPartList(methodExecuteResult, methodKey, templateHandlerContext, afterEvaluationContext);
        LogLevelEnum logLevel = methodExecuteResult.getLogLevel();
        String content = this.getContentString(resultPartList);
        return this.createLogPO(afterEvaluationContext, resultType, logLevel, content);
    }

    private void resultPostProcess(ELog logOnMethod, ELog logOnClass, Object result, MethodExecuteResult methodExecuteResult) {
        Class<? extends ResultPostProcessor> globalProcessor = Optional.ofNullable(logProperties.getProcessor())
                .map(LogResultPostProcessorProp::getGlobal)
                .map(LogResultPostProcessorItemProp::getClazz)
                .orElse(null);
        Class<? extends ResultPostProcessor> classProcessor = Optional.ofNullable(logOnClass).map(ELog::processor).orElse(null);
        Class<? extends ResultPostProcessor> methodProcessor = Optional.ofNullable(logOnMethod).map(ELog::processor).orElse(null);
        Class<? extends ResultPostProcessor> process = null;
        if (methodProcessor != null && methodProcessor != NopResultPostProcessor.class) {
            process = methodProcessor;
        } else if (classProcessor != null && classProcessor != NopResultPostProcessor.class) {
            process = classProcessor;
        } else if (globalProcessor != null && globalProcessor != NopResultPostProcessor.class) {
            process = globalProcessor;
        } else {
            process = NopResultPostProcessor.class;
        }
        ResultPostProcessor postProcessor = logResultPostProcessorRegistry.get(process);
        postProcessor.process(result, methodExecuteResult);
    }

    private String integrateTemplatePrefix(String template, ELog logOnClass) {
        if (logOnClass != null) {
            String templatePrefix = logOnClass.successTemplate();
            template = templatePrefix + template;
        }
        return template;
    }

    private LogTemplateHandler createHandlerAndParseTemplate(String template, LogParseFunctionRegistry logParseFunctionRegistry,
                                                             LogExpressionEvaluator expressionEvaluator,
                                                             AnnotatedElementKey methodKey, LogEvaluationContext evaluationContext,
                                                             boolean before) {
        LogTemplateHandler templateHandler = new LogTemplateHandler(logParseFunctionRegistry, template, expressionEvaluator);
        templateHandler.extractStringPart();
        templateHandler.methodValueInject(methodKey, evaluationContext, before);
        return templateHandler;
    }


    private void handleAnnotationValue(ELog logOnMethod, ELog logOnClass) {
        if (!"".equals(logOnMethod.group()) && !"default".equals(logOnMethod.group())) {
            LogContext.setLogAttributeCommon(LogConstant.GROUP, logOnMethod.group());
        } else if (logOnClass != null && !"".equals(logOnClass.group()) && !"default".equals(logOnClass.group())) {
            LogContext.setLogAttributeCommon(LogConstant.GROUP, logOnClass.group());
        } else {
            LogContext.setLogAttributeCommon(LogConstant.GROUP, "default");
        }

        if (!"".equals(logOnMethod.subject()) && !"default".equals(logOnMethod.subject())) {
            LogContext.setLogAttributeCommon(LogConstant.SUBJECT, logOnMethod.subject());
        } else if (logOnClass != null && !"".equals(logOnClass.subject()) && !"default".equals(logOnClass.subject())) {
            LogContext.setLogAttributeCommon(LogConstant.SUBJECT, logOnClass.subject());
        } else {
            LogContext.setLogAttributeCommon(LogConstant.SUBJECT, "default");
        }

        if (!"".equals(logOnMethod.bizType()) && !"default".equals(logOnMethod.bizType())) {
            LogContext.setLogAttribute(LogConstant.BIZ_TYPE, logOnMethod.bizType());
        } else if (logOnClass != null && !"default".equals(logOnClass.bizType()) && !"".equals(logOnClass.bizType())) {
            LogContext.setLogAttribute(LogConstant.BIZ_TYPE, logOnClass.bizType());
        } else {
            LogContext.setLogAttribute(LogConstant.BIZ_TYPE, "default");
        }

        LinkedHashMap<String, String> globalTags = Optional.ofNullable(logProperties.getTag()).map(LogTagProp::getGlobal).orElse(new LinkedHashMap<>());
        LinkedHashMap<String, String> classTags = this.extractTagsFromAnnotation(logOnClass);
        LinkedHashMap<String, String> methodTags = this.extractTagsFromAnnotation(logOnMethod);

        globalTags.putAll(classTags);
        globalTags.putAll(methodTags);

        LogContext.setLogAttribute(LogConstant.TAG, globalTags);
    }

    private LinkedHashMap<String, String> extractTagsFromAnnotation(ELog logOnClass) {
        LinkedHashMap<String, String> tags = Optional.ofNullable(logOnClass)
                .map(ELog::tags)
                .map(Arrays::asList)
                .map(item -> item.stream()
                        .map(item2 -> item2.split("="))
                        .filter(item2 -> item2.length == 2)
                        .collect(Collectors.toMap(item2 -> item2[0], item2 -> item2[1], (k1, k2) -> k2, LinkedHashMap::new)))
                .orElse(new LinkedHashMap<>());
        return tags;
    }

    private String getResultName(ELog logOnMethod, ELog logOnClass) {
        String globalResultName = Optional.ofNullable(logProperties.getResultName()).map(LogResultNameProp::getGlobal).orElse(null);
        String resultName = null;
        if (!"result".equals(logOnMethod.resultName()) && !"".equals(logOnMethod.resultName())) {
            resultName = logOnMethod.resultName();
        } else if (logOnClass != null && !"result".equals(logOnClass.resultName()) && !"".equals(logOnClass.resultName())) {
            resultName = logOnClass.resultName();
        } else if (!ObjectUtils.isEmpty(globalResultName)) {
            resultName = globalResultName;
        } else {
            resultName = "result";
        }
        return resultName;
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    private LogPO createLogPO(LogEvaluationContext afterEvaluationContext, ResultTypeEnum resultType, LogLevelEnum logLevel, String content) {
        String bizType = String.valueOf(afterEvaluationContext.lookupVariable(LogConstant.BIZ_TYPE));
        String subject = String.valueOf(afterEvaluationContext.lookupVariable(LogConstant.SUBJECT));
        String group = String.valueOf(afterEvaluationContext.lookupVariable(LogConstant.GROUP));
        String ip = String.valueOf(afterEvaluationContext.lookupVariable(LogConstant.IP));
        LinkedHashMap<String, String> tags = (LinkedHashMap<String, String>) afterEvaluationContext.lookupVariable(LogConstant.TAG);
        return new LogPO(group, subject, ip, bizType, resultType, logLevel, content, tags);
    }

    private String getContentString(List<StringPart> resultPartList) {
        StringBuilder sbr = new StringBuilder();
        for (StringPart stringPart : resultPartList) {
            Object value = stringPart.getValue();
            sbr.append(value);
        }
        return sbr.toString();
    }

    private List<StringPart> getStringPartList(MethodExecuteResult methodExecuteResult, AnnotatedElementKey methodKey,
                                               TemplateHandlerContext templateHandlerContext,
                                               LogEvaluationContext afterEvaluationContext) throws Throwable {
        List<StringPart> resultPartList = new ArrayList<>();
        if (methodExecuteResult.isBusinessFail()) {
            if (methodExecuteResult.isHasFailTemplate()) {
                resultPartList = templateHandlerContext.getFailHandler().methodValueInject(methodKey, afterEvaluationContext, false);
            } else {
                throw methodExecuteResult.getBusinessException();
            }
        } else {
            resultPartList = templateHandlerContext.getSuccessHandler().methodValueInject(methodKey, afterEvaluationContext, false);
        }
        return resultPartList;
    }

    private ResultTypeEnum getResultType(MethodExecuteResult methodExecuteResult) {
        ResultTypeEnum resultType = ResultTypeEnum.UNKNOWN;
        if (methodExecuteResult.isBusinessFail()) {
            resultType = ResultTypeEnum.FAIL;
        } else {
            resultType = ResultTypeEnum.SUCCESS;
        }
        return resultType;
    }
}
