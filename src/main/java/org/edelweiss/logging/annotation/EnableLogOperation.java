package org.edelweiss.logging.annotation;

import org.edelweiss.logging.config.LogOperationDefaultConfig;
import org.edelweiss.logging.config.LogWebConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({LogOperationDefaultConfig.class, LogWebConfig.class})
@Documented
public @interface EnableLogOperation {
}
