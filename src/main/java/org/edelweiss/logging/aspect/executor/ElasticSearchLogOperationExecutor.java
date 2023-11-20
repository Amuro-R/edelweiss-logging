package org.edelweiss.logging.aspect.executor;//package com.neon.logdemo.log.aspect.executor;
//
//import com.neon.logdemo.log.config.LogOperationConfig;
//import com.neon.logdemo.log.pojo.LogOperationPO;
//import com.neon.logdemo.log.pojo.LogOperationProperties;
//import com.neon.logdemo.log.util.ElasticSearchUtil;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class ElasticSearchLogOperationExecutor implements LogOperationExecutor {
//
//    private final LogOperationProperties.ESLogOperationProperties esLogOperationProperties;
//
//    public ElasticSearchLogOperationExecutor(
//            LogOperationProperties.ESLogOperationProperties esLogOperationProperties) {
//        this.esLogOperationProperties = esLogOperationProperties;
//    }
//
//    @Override
//    public Object execute(LogOperationPO logOperationPO) {
//        log.debug("ElasticSearchLogOperationExecutor execute");
//        String logIndexName = esLogOperationProperties.getLogIndexName();
//        String logIndexType = esLogOperationProperties.getLogIndexType();
//        LogOperationConfig.LOG_THREAD_POOL_EXECUTOR.submit(() -> {
//            ElasticSearchUtil.createDocument(logIndexName, logOperationPO, logIndexType);
//        });
//        return null;
//    }
//}
