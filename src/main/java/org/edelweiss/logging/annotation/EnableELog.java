package org.edelweiss.logging.annotation;

import org.edelweiss.logging.config.LogDefaultConfig;
import org.edelweiss.logging.config.LogThreadConfig;
import org.edelweiss.logging.config.LogWebConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({LogDefaultConfig.class, LogWebConfig.class, LogThreadConfig.class})
@Documented
public @interface EnableELog {
}
