package org.edelweiss.logging.aspect.processor;


import org.edelweiss.logging.aspect.MethodExecuteResult;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class NopResultPostProcessor implements ResultPostProcessor {
    @Override
    public Object process(Object result, MethodExecuteResult methodExecuteResult) {
        return null;
    }
}
