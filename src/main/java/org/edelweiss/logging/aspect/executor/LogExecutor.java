package org.edelweiss.logging.aspect.executor;


import org.edelweiss.logging.pojo.po.LogPO;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public interface LogExecutor {

    Object execute(LogPO logPO);


}

