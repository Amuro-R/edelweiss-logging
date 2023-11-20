package org.edelweiss.logging.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.aspect.executor.LogExecutor;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Data
@NoArgsConstructor
public class LogExecutorProperties {

    private Class<? extends LogExecutor> global;

}
