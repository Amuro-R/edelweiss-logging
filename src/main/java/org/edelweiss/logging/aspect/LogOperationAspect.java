package org.edelweiss.logging.aspect;

import org.edelweiss.logging.annotation.LogOperation;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;
import org.edelweiss.logging.context.LogContext;
import org.edelweiss.logging.context.TemplateHandlerContext;
import org.edelweiss.logging.aspect.executor.LogOperationExecutor;
import org.edelweiss.logging.aspect.part.StringPart;
import org.edelweiss.logging.el.LogEvaluationContext;
import org.edelweiss.logging.el.LogOperationExpressionEvaluator;
import org.edelweiss.logging.el.LogParseFunctionFactory;
import org.edelweiss.logging.properties.LogOperationProperties;
import org.edelweiss.logging.pojo.po.LogPO;
import org.edelweiss.logging.pojo.eo.OperationTypeEnum;
import org.edelweiss.logging.pojo.eo.ResultTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Aspect
public class LogOperationAspect {

    private final StandardReflectionParameterNameDiscoverer nameDiscoverer = new StandardReflectionParameterNameDiscoverer();
    private final LogOperationExpressionEvaluator expressionEvaluator = new LogOperationExpressionEvaluator();

    @Autowired
    private LogParseFunctionFactory logParseFunctionFactory;

    @Autowired
    private LogOperationExecutor logOperationExecutor;

    @Autowired
    private ResultPostProcessor resultPostProcessor;

    @Autowired
    private LogOperationProperties logOperationProperties;

    @Pointcut("@annotation(com.uniubi.mbi.core.service.log.annotation.LogOperation)")
    public void logOperationCutPoint() {
    }

    @Around("com.uniubi.mbi.core.service.log.aspect.LogOperationAspect.logOperationCutPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        Object[] args = pjp.getArgs();
        LogOperation logOperationOnMethod = method.getAnnotation(LogOperation.class);
        LogOperation logOperationOnClass = clazz.getAnnotation(LogOperation.class);

        MethodExecuteResult methodExecuteResult = new MethodExecuteResult();
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        TemplateHandlerContext templateHandlerContext = null;

        if (!logOperationProperties.isEnable()) {
            return pjp.proceed(args);
        }

        try {
            templateHandlerContext = this.logBefore(method, args, logOperationOnMethod, logOperationOnClass,
                    methodExecuteResult, methodKey);
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
            LogPO logOperation = this.logAfter(method, args, logOperationOnMethod, logOperationOnClass, methodExecuteResult,
                    methodKey, result, templateHandlerContext);

            if (logOperationExecutor != null) {
                logOperationExecutor.execute(logOperation);
            } else {
                throw new RuntimeException("没有合适的日志记录器");
            }
        } catch (Exception e) {
            // if (methodExecuteResult.getBusinessException() != e) {
            //     e.printStackTrace();
            // }
            e.printStackTrace();
        } finally {
            LogContext.removeLogAttributes();
        }

        if (methodExecuteResult.getBusinessException() != null) {
            throw methodExecuteResult.getBusinessException();
        }

        return result;
    }

    private TemplateHandlerContext logBefore(Method method, Object[] args, LogOperation logOperationOnMethod,
                                             LogOperation logOperationOnClass, MethodExecuteResult methodExecuteResult,
                                             AnnotatedElementKey methodKey) {
        LogContext.createCurrentStackFrame();

        String successTemplate = logOperationOnMethod.successTemplate();
        String failTemplate = logOperationOnMethod.failTemplate();

        successTemplate = this.integrateTemplatePrefix(successTemplate, logOperationOnClass);
        failTemplate = this.integrateTemplatePrefix(failTemplate, logOperationOnClass);

        this.handleAnnotationValue(logOperationOnMethod, logOperationOnClass);

        LogEvaluationContext beforeEvaluationContext = new LogEvaluationContext(null, method, args, nameDiscoverer);

        LogOperationTemplateHandler successHandler = this.createHandlerAndParseTemplate(successTemplate, logParseFunctionFactory,
                expressionEvaluator, methodKey, beforeEvaluationContext, true);

        LogOperationTemplateHandler failHandler = null;
        if (!failTemplate.trim().equals("")) {
            methodExecuteResult.setHasFailTemplate(true);
            failHandler = this.createHandlerAndParseTemplate(failTemplate, logParseFunctionFactory, expressionEvaluator, methodKey,
                    beforeEvaluationContext, true);
        }

        return new TemplateHandlerContext(successHandler, failHandler);
    }

    private LogPO logAfter(Method method, Object[] args, LogOperation logOperationOnMethod, LogOperation logOperationOnClass,
                           MethodExecuteResult methodExecuteResult, AnnotatedElementKey methodKey, Object result,
                           TemplateHandlerContext templateHandlerContext) throws Throwable {

        if (methodExecuteResult.isLogBeforeFail()) {
            throw new RuntimeException("before日志异常，跳过后续日志解析");
        }

        if (resultPostProcessor != null) {
            resultPostProcessor.process(result, methodExecuteResult);
        }

        LogEvaluationContext afterEvaluationContext = new LogEvaluationContext(null, method, args, nameDiscoverer);

        String resultName = this.getResultName(logOperationOnMethod, logOperationOnClass);
        afterEvaluationContext.setVariable(resultName, result);

        ResultTypeEnum resultType = this.getResultType(methodExecuteResult);
        List<StringPart> resultPartList = this.getStringPartList(methodExecuteResult, methodKey, templateHandlerContext, afterEvaluationContext);

        String content = this.getContentString(resultPartList);

        return this.getLogOperationPO(afterEvaluationContext, resultType, content);
    }

    private String integrateTemplatePrefix(String template, LogOperation logOperationOnClass) {
        if (logOperationOnClass != null) {
            String templatePrefix = logOperationOnClass.successTemplate();
            template = templatePrefix + template;
        }
        return template;
    }

    private LogOperationTemplateHandler createHandlerAndParseTemplate(String template, LogParseFunctionFactory logParseFunctionFactory,
                                                                      LogOperationExpressionEvaluator expressionEvaluator,
                                                                      AnnotatedElementKey methodKey, LogEvaluationContext evaluationContext,
                                                                      boolean before) {
        LogOperationTemplateHandler templateHandler = new LogOperationTemplateHandler(logParseFunctionFactory, template, expressionEvaluator);
        templateHandler.extractStringPart();
        templateHandler.methodValueInject(methodKey, evaluationContext, before);
        return templateHandler;
    }


    private void handleAnnotationValue(LogOperation logOperationOnMethod, LogOperation logOperationOnClass) {
        if (!"".equals(logOperationOnMethod.operator())) {
            LogContext.setLogAttributeCommon(LogOperationConstant.OPERATOR, logOperationOnMethod.operator());
        } else if (logOperationOnClass != null && !"".equals(logOperationOnClass.operator())) {
            LogContext.setLogAttributeCommon(LogOperationConstant.OPERATOR, logOperationOnClass.operator());
        }

        if (!"".equals(logOperationOnMethod.ip())) {
            LogContext.setLogAttributeCommon(LogOperationConstant.IP, logOperationOnMethod.ip());
        } else if (logOperationOnClass != null && !"".equals(logOperationOnClass.ip())) {
            LogContext.setLogAttributeCommon(LogOperationConstant.IP, logOperationOnClass.ip());
        }
//        操作类型非共有
        if (!OperationTypeEnum.NONE.equals(logOperationOnMethod.operationType())) {
            LogContext.setLogAttribute(LogOperationConstant.OPERATION_TYPE, logOperationOnMethod.operationType());
        } else if (logOperationOnClass != null && !OperationTypeEnum.NONE.equals(logOperationOnClass.operationType())) {
            LogContext.setLogAttribute(LogOperationConstant.OPERATION_TYPE, logOperationOnClass.operationType());
        } else {
            LogContext.setLogAttribute(LogOperationConstant.OPERATION_TYPE, OperationTypeEnum.UNKNOWN);
        }
    }

    private String getResultName(LogOperation logOperationOnMethod, LogOperation logOperationOnClass) {
        String resultName = logOperationOnMethod.resultName();
        if (!"result".equals(logOperationOnMethod.resultName())) {
            resultName = logOperationOnMethod.resultName();
        } else if (logOperationOnClass != null && !"result".equals(logOperationOnClass.resultName())) {
            resultName = logOperationOnClass.resultName();
        }
        return resultName;
    }

    @SuppressWarnings("ConstantConditions")
    private LogPO getLogOperationPO(LogEvaluationContext afterEvaluationContext, ResultTypeEnum resultType, String content) {
        OperationTypeEnum operationTypeEnum = (OperationTypeEnum) afterEvaluationContext.lookupVariable(LogOperationConstant.OPERATION_TYPE);
        String operator = String.valueOf(afterEvaluationContext.lookupVariable(LogOperationConstant.OPERATOR));
        String ip = String.valueOf(afterEvaluationContext.lookupVariable(LogOperationConstant.IP));
        String phone = String.valueOf(afterEvaluationContext.lookupVariable(LogOperationConstant.PHONE));
        return new LogPO(operator, phone, operationTypeEnum, resultType, content);
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
