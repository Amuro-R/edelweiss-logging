package org.edelweiss.logging.log.aspect.processor;


import org.edelweiss.logging.log.aspect.MethodExecuteResult;

public class NopResultPostProcessor implements ResultPostProcessor {
    @Override
    public Object process(Object result, MethodExecuteResult methodExecuteResult) {
        return null;
    }
}
