package org.edelweiss.logging.el;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class LogExpressionEvaluator extends CachedExpressionEvaluator {

    private final ConcurrentMap<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>();

    public Object getExpressionValue(AnnotatedElementKey elementKey, String expression, EvaluationContext evalContext) {
        return super.getExpression(this.expressionCache, elementKey, expression).getValue(evalContext);
    }

    public Object parseValue(AnnotatedElementKey elementKey, String expression, EvaluationContext evalContext) {
        return super.getParser().parseRaw(expression).getValue(evalContext);
    }


}
