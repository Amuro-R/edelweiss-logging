package org.edelweiss.logging.aspect.processor;


import org.edelweiss.logging.aspect.MethodExecuteResult;

public class NopResultPostProcessor implements ResultPostProcessor {
    @Override
    public Object process(Object result, MethodExecuteResult methodExecuteResult) {
        return null;
    }
}
