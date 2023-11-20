package org.edelweiss.logging.pojo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.pojo.eo.OperationTypeEnum;
import org.edelweiss.logging.pojo.eo.ResultTypeEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Data
@NoArgsConstructor
public class LogPO {
    private String id;
    private String operator;
    @Deprecated
    private String phone;
    private String content;
    private String resType;
    private String bizType;
    private List<String> tags;
    private Date createTime;
    private Date modifiedTime;

    public LogPO(String operator, String phone, OperationTypeEnum operationTypeEnum, ResultTypeEnum resType, String content) {
        this.operator = operator;
        this.phone = phone;
        this.content = content;
        // this.params = params;
        this.resType = resType.codeDesc;
        this.bizType = operationTypeEnum.code;
        Date now = new Date();
        this.createTime = now;
        this.modifiedTime = now;
        this.tags = new ArrayList<>();
    }
}
