package org.edelweiss.logging.el;

public interface ILogParseFunction {

    public String functionName();

    public Object parse(Object[] args);
}
