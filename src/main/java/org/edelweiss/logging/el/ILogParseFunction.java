package org.edelweiss.logging.el;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public interface ILogParseFunction {

    public String functionName();

    public Object parse(Object[] args);
}
