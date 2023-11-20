package org.edelweiss.logging.pojo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.pojo.eo.ResultTypeEnum;

import java.util.Date;
import java.util.LinkedHashSet;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Data
@NoArgsConstructor
public class LogPO {
    private String id;
    private String operator;
    private String ip;
    private String content;
    private String resType;
    private String bizType;
    private LinkedHashSet<String> tags;
    private Date createTime;
    private Date modifiedTime;

    public LogPO(String operator, String ip, String bizType, ResultTypeEnum resultType, String content, LinkedHashSet<String> tags) {
        this.operator = operator;
        this.ip = ip;
        this.content = content;
        this.resType = resultType.code;
        this.bizType = bizType;
        Date date = new Date();
        this.createTime = date;
        this.modifiedTime = date;
        this.tags = tags;
    }

}
