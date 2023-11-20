package org.edelweiss.logging.log.aspect.processor;


import org.edelweiss.logging.log.aspect.MethodExecuteResult;

public interface ResultPostProcessor {

    Object process(Object result, MethodExecuteResult methodExecuteResult);

}
