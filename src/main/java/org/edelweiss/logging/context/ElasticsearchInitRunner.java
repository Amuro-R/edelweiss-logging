package org.edelweiss.logging.context;// package com.uniubi.mbi.core.service.log.context;
//
// import com.uniubi.mbi.core.service.log.pojo.LogOperationProperties;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.ApplicationArguments;
// import org.springframework.boot.ApplicationRunner;
//
// @Slf4j
// public class ElasticsearchInitRunner implements ApplicationRunner {
//
//     @Autowired
//     private LogOperationProperties logOperationProperties;
//
//     @Override
//     public void run(ApplicationArguments args) throws Exception {
//         log.debug("ElasticsearchInitRunner init");
//         //LogOperationProperties.ESLogOperationProperties es = logOperationProperties.getEs();
//         //if (es != null && StringUtils.hasLength(es.getLogIndexName()) && StringUtils.hasLength(es.getLogIndexType())) {
//         //    log.info("es日志已开启");
//         //    boolean exist = ElasticSearchUtil.indexExist(es.getLogIndexName());
//         //    if (!exist) {
//         //        boolean index = ElasticSearchUtil.createIndex(LogOperationPO.class);
//         //        log.info("创建es日志索引: {}", index ? "成功" : "失败");
//         //    } else {
//         //        log.info("es日志索引已存在");
//         //    }
//         //}
//     }
// }
