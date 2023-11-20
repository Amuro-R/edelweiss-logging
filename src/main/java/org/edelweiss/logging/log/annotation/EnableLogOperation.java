package org.edelweiss.logging.log.annotation;

import org.edelweiss.logging.log.config.LogOperationDefaultConfig;
import org.edelweiss.logging.log.config.LogWebConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({LogOperationDefaultConfig.class, LogWebConfig.class})
@Documented
public @interface EnableLogOperation {
}
