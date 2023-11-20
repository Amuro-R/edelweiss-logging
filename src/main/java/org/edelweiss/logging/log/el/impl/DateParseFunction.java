package org.edelweiss.logging.log.el.impl;


import org.edelweiss.logging.log.el.ILogParseFunction;
import org.edelweiss.logging.util.DateUtil;

import java.util.Date;

public class DateParseFunction implements ILogParseFunction {
    @Override
    public String functionName() {
        return "DateParseFunction";
    }

    @Override
    public Object parse(Object[] args) {
        if (args != null && args.length > 0) {
            if (args[0] != null && args[0] instanceof Date) {
                Date obj = (Date) args[0];
                return DateUtil.formatDateDefault(obj);
            }
        }
        return null;
    }
}
