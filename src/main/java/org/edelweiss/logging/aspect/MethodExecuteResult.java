package org.edelweiss.logging.aspect;

import lombok.Data;
import lombok.NoArgsConstructor;

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

}
