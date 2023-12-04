package org.edelweiss.logging.pojo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.pojo.eo.LogLevelEnum;
import org.edelweiss.logging.pojo.eo.ResultTypeEnum;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Data
@NoArgsConstructor
public class LogPO {
    private String id;
    private String group;
    private String subject;
    private String ip;
    private String content;
    private String resType;
    private String bizType;
    private LinkedHashMap<String, String> tags;
    private Date createTime;
    private Date modifiedTime;
    private String logLevel;

    public LogPO(String group, String subject, String ip, String bizType, ResultTypeEnum resultType, LogLevelEnum logLevel, String content, LinkedHashMap<String, String> tags) {
        this.group = group;
        this.subject = subject;
        this.ip = ip;
        this.content = content;
        this.resType = resultType.code;
        this.logLevel = logLevel.code;
        this.bizType = bizType;
        Date date = new Date();
        this.createTime = date;
        this.modifiedTime = date;
        this.tags = tags;
    }

}
