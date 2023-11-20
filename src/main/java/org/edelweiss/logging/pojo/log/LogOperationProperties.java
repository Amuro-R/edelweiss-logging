package org.edelweiss.logging.pojo.log;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogOperationProperties {
    private boolean enable = false;
    // private String type = "noop";
    private LogType type = LogType.NOOP;
}
