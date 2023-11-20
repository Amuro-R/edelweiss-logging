package org.edelweiss.logging.annotation;

import org.edelweiss.logging.aspect.executor.LogExecutor;

import java.lang.annotation.*;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Documented
public @interface LogExecutorItem {
    Class<? extends LogExecutor> clazz();

    boolean async() default true;
}
