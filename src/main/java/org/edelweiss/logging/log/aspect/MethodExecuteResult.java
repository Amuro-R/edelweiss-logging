package org.edelweiss.logging.log.aspect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MethodExecuteResult {

    private boolean logBeforeFail;
    private boolean businessFail;
    private boolean hasFailTemplate;
    private Throwable businessException;

}
