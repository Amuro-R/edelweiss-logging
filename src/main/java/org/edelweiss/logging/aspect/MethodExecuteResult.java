package org.edelweiss.logging.aspect;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.pojo.eo.LogLevelEnum;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Data
@NoArgsConstructor
public class MethodExecuteResult {

    private boolean logBeforeFail;
    private boolean businessFail;
    private boolean hasFailTemplate;
    private Throwable businessException;
    private LogLevelEnum logLevel = LogLevelEnum.INFO;

}
