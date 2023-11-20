package org.edelweiss.logging.pojo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.pojo.eo.OperationTypeEnum;
import org.edelweiss.logging.pojo.eo.ResultTypeEnum;

import java.util.Date;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Data
@NoArgsConstructor
public class LogPO {
    private Long id;
    private String username;
    private String phone;
    private String content;
    private String resultType;
    private String operationType;
    private Date gmtCreate;
    private Date gmtModified;

    public LogPO(String operator, String phone, OperationTypeEnum operationTypeEnum, ResultTypeEnum resultType, String content) {
        this.username = operator;
        this.phone = phone;
        this.content = content;
        // this.params = params;
        this.resultType = resultType.codeDesc;
        this.operationType = operationTypeEnum.code;
        Date now = new Date();
        this.gmtCreate = now;
        this.gmtModified = now;
    }
}
