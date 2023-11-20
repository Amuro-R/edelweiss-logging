package org.edelweiss.logging;

import org.edelweiss.logging.config.LogWebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fzw
 * @date 2023/11/17
 **/
@Configuration
@Import(LogWebConfig.class)
public class EdelweissLoggingAutoConfiguration {


}
