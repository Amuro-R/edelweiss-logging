package org.edelweiss.logging.annotation;

import org.edelweiss.logging.aspect.executor.LogExecutor;

import java.lang.annotation.*;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Documented
public @interface LogExecutorItem {
    /**
     * 执行器类
     */
    Class<? extends LogExecutor> clazz();

    /**
     * 是否异步执行
     */
    boolean async() default true;
}
