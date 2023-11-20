package org.edelweiss.logging.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Data
@NoArgsConstructor
public class LogResultPostProcessor {

    private Class<? extends ResultPostProcessor> global;
}
