package org.edelweiss.logging.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.pojo.eo.LogTypeEnum;

@Data
@NoArgsConstructor
public class LogProperties {
    private boolean enable = false;
    // private String type = "noop";
    private LogTypeEnum type = LogTypeEnum.NOOP;
}
