package org.edelweiss.logging.aspect.processor;


import org.edelweiss.logging.aspect.MethodExecuteResult;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public interface ResultPostProcessor {

    Object process(Object result, MethodExecuteResult methodExecuteResult);

}
