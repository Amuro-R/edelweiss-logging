package org.edelweiss.logging.aspect.executor;

import org.edelweiss.logging.pojo.po.LogPO;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class ConsoleLogExecutor implements LogExecutor {
    @Override
    public Object execute(LogPO logPO) {
        System.out.println(logPO);
        return null;
    }
}
