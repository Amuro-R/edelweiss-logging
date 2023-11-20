package org.edelweiss.logging.el;

import org.edelweiss.logging.context.LogContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class LogEvaluationContext extends MethodBasedEvaluationContext {
    @SuppressWarnings("ConstantConditions")
    public LogEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
        this.setVariables(LogContext.getLogAttributesCommon());
        this.setVariables(LogContext.getLogAttributes());

    }
}
