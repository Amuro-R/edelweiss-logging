package org.edelweiss.logging.aspect;

import org.edelweiss.logging.annotation.Log;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;
import org.edelweiss.logging.context.LogContext;
import org.edelweiss.logging.context.TemplateHandlerContext;
import org.edelweiss.logging.aspect.executor.LogExecutor;
import org.edelweiss.logging.aspect.part.StringPart;
import org.edelweiss.logging.el.LogEvaluationContext;
import org.edelweiss.logging.el.LogOperationExpressionEvaluator;
import org.edelweiss.logging.el.LogParseFunctionFactory;
import org.edelweiss.logging.properties.LogProperties;
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
public class LogAspect {

    private final StandardReflectionParameterNameDiscoverer nameDiscoverer = new StandardReflectionParameterNameDiscoverer();
    private final LogOperationExpressionEvaluator expressionEvaluator = new LogOperationExpressionEvaluator();

    @Autowired
    private LogParseFunctionFactory logParseFunctionFactory;

    @Autowired
    private LogExecutor logExecutor;

    @Autowired
    private ResultPostProcessor resultPostProcessor;

    @Autowired
    private LogProperties logProperties;

    @Pointcut("@annotation(org.edelweiss.logging.annotation.Log)")
    public void logOperationCutPoint() {
    }

    @Around("org.edelweiss.logging.aspect.LogAspect.logOperationCutPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        Object[] args = pjp.getArgs();
        Log logOnMethod = method.getAnnotation(Log.class);
        Log logOnClass = clazz.getAnnotation(Log.class);

        MethodExecuteResult methodExecuteResult = new MethodExecuteResult();
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        TemplateHandlerContext templateHandlerContext = null;

        if (!logProperties.isEnable()) {
            return pjp.proceed(args);
        }

        try {
            templateHandlerContext = this.logBefore(method, args, logOnMethod, logOnClass,
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
            LogPO logOperation = this.logAfter(method, args, logOnMethod, logOnClass, methodExecuteResult,
                    methodKey, result, templateHandlerContext);

            if (logExecutor != null) {
                logExecutor.execute(logOperation);
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

    private TemplateHandlerContext logBefore(Method method, Object[] args, Log logOnMethod,
                                             Log logOnClass, MethodExecuteResult methodExecuteResult,
                                             AnnotatedElementKey methodKey) {
        LogContext.createCurrentStackFrame();

        String successTemplate = logOnMethod.successTemplate();
        String failTemplate = logOnMethod.failTemplate();

        successTemplate = this.integrateTemplatePrefix(successTemplate, logOnClass);
        failTemplate = this.integrateTemplatePrefix(failTemplate, logOnClass);

        this.handleAnnotationValue(logOnMethod, logOnClass);

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

    private LogPO logAfter(Method method, Object[] args, Log logOnMethod, Log logOnClass,
                           MethodExecuteResult methodExecuteResult, AnnotatedElementKey methodKey, Object result,
                           TemplateHandlerContext templateHandlerContext) throws Throwable {

        if (methodExecuteResult.isLogBeforeFail()) {
            throw new RuntimeException("before日志异常，跳过后续日志解析");
        }

        if (resultPostProcessor != null) {
            resultPostProcessor.process(result, methodExecuteResult);
        }

        LogEvaluationContext afterEvaluationContext = new LogEvaluationContext(null, method, args, nameDiscoverer);

        String resultName = this.getResultName(logOnMethod, logOnClass);
        afterEvaluationContext.setVariable(resultName, result);

        ResultTypeEnum resultType = this.getResultType(methodExecuteResult);
        List<StringPart> resultPartList = this.getStringPartList(methodExecuteResult, methodKey, templateHandlerContext, afterEvaluationContext);

        String content = this.getContentString(resultPartList);

        return this.getLogOperationPO(afterEvaluationContext, resultType, content);
    }

    private String integrateTemplatePrefix(String template, Log logOnClass) {
        if (logOnClass != null) {
            String templatePrefix = logOnClass.successTemplate();
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


    private void handleAnnotationValue(Log logOnMethod, Log logOnClass) {
        if (!"".equals(logOnMethod.operator())) {
            LogContext.setLogAttributeCommon(LogOperationConstant.OPERATOR, logOnMethod.operator());
        } else if (logOnClass != null && !"".equals(logOnClass.operator())) {
            LogContext.setLogAttributeCommon(LogOperationConstant.OPERATOR, logOnClass.operator());
        }

        if (!"".equals(logOnMethod.ip())) {
            LogContext.setLogAttributeCommon(LogOperationConstant.IP, logOnMethod.ip());
        } else if (logOnClass != null && !"".equals(logOnClass.ip())) {
            LogContext.setLogAttributeCommon(LogOperationConstant.IP, logOnClass.ip());
        }
//        操作类型非共有
        if (!OperationTypeEnum.NONE.equals(logOnMethod.bizType())) {
            LogContext.setLogAttribute(LogOperationConstant.OPERATION_TYPE, logOnMethod.bizType());
        } else if (logOnClass != null && !OperationTypeEnum.NONE.equals(logOnClass.bizType())) {
            LogContext.setLogAttribute(LogOperationConstant.OPERATION_TYPE, logOnClass.bizType());
        } else {
            LogContext.setLogAttribute(LogOperationConstant.OPERATION_TYPE, OperationTypeEnum.UNKNOWN);
        }
    }

    private String getResultName(Log logOnMethod, Log logOnClass) {
        String resultName = logOnMethod.resultName();
        if (!"result".equals(logOnMethod.resultName())) {
            resultName = logOnMethod.resultName();
        } else if (logOnClass != null && !"result".equals(logOnClass.resultName())) {
            resultName = logOnClass.resultName();
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
