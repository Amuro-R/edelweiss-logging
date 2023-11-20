package org.edelweiss.logging.aspect.processor;


import org.edelweiss.logging.aspect.MethodExecuteResult;

public interface ResultPostProcessor {

    Object process(Object result, MethodExecuteResult methodExecuteResult);

}
