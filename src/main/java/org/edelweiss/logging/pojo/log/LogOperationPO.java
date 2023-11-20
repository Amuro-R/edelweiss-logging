package org.edelweiss.logging.pojo.log;// package com.uniubi.mbi.core.service.log.pojo;
//
// import com.uniubi.mbi.common.util.DateUtil;
// import lombok.Data;
// import lombok.NoArgsConstructor;
//
// import java.time.Instant;
// import java.util.Date;
//
// @Data
// @NoArgsConstructor
// //@Document(indexName = "#{logOperationProperties.es.logIndexName}", type = "#{logOperationProperties.es.logIndexType}", createIndex = false, shards = 4, replicas = 0)
// public class LogOperationPO {
//
//     //@Id
//     private String id;
//
//     //@Field(name = LogOperationPO.OPERATOR, type = FieldType.Text)
//     private String operator;
//
//     //@Field(name = LogOperationPO.IP, type = FieldType.Ip)
//     private String ip;
//
//     //@Field(name = LogOperationPO.CONTENT, type = FieldType.Text)
//     private String content;
//
//     //@Field(name = LogOperationPO.OPERATION_TYPE, type = FieldType.Keyword)
//     private String operationType;
//
//     //@Field(name = LogOperationPO.RESULT_TYPE, type = FieldType.Keyword)
//     private String resultType;
//
//     //@Field(name = LogOperationPO.GMT_CREATE, type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd HH:mm:ss")
//     private String gmtCreate;
//
//     public LogOperationPO(String operator, String ip, String content, String operationType, ResultType result) {
//         this.operator = operator;
//         this.ip = ip;
//         this.content = content;
//         this.operationType = operationType;
//         this.resultType = result.codeDesc;
//         this.gmtCreate = DateUtil.formatDefault(Date.from(Instant.now()));
//     }
//
//     public static final String OPERATOR = "operator";
//     public static final String IP = "ip";
//     public static final String CONTENT = "content";
//     public static final String OPERATION_TYPE = "operationType";
//     public static final String RESULT_TYPE = "resultType";
//     public static final String GMT_CREATE = "gmtCreate";
// }
