package org.edelweiss.logging.log.el;

public interface ILogParseFunction {

    public String functionName();

    public Object parse(Object[] args);
}
