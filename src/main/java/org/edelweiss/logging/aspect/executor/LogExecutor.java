package org.edelweiss.logging.aspect.executor;


import org.edelweiss.logging.pojo.po.LogPO;

public interface LogExecutor {

    Object execute(LogPO logOperationPO);


}

