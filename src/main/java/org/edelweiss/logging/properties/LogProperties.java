package org.edelweiss.logging.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogProperties {
    private boolean enable = false;
    private LogExecutorProperties executor;
    private LogResultPostProcessor processor;
    private LogBizTypeProperties bizType;
    private LogTagProperties tag;
    private LogResultNameProperties resultName;

}
