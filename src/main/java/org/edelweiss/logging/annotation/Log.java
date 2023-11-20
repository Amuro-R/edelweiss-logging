package org.edelweiss.logging.annotation;


import org.edelweiss.logging.aspect.executor.ConsoleLogExecutor;
import org.edelweiss.logging.aspect.executor.LogExecutor;
import org.edelweiss.logging.aspect.processor.NopResultPostProcessor;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;
import org.edelweiss.logging.el.ILogParseFunction;

import java.lang.annotation.*;

/**
 * 模板变量说明
 * {#xxx#} 方法执行前的SpEl变量
 * {@xxx@} 方法执行前的函数变量，需创建{@link ILogParseFunction}类型的spring bean
 * [#xxx#] 方法执行后的SpEl变量
 * [@xxx@] 方法执行后的函数变量，需创建{@link ILogParseFunction}类型的spring bean
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Log {

    /**
     * 操作人
     * 方法级覆盖类级设置
     */
    String operator() default "default";

    /**
     * 请求所属的IP地址
     */
    @Deprecated
    String ip() default "none";

    /**
     * 业务类型
     * 方法级覆盖类级设置，类级覆盖全局配置
     */
    String bizType() default "default";

    /**
     * 标签数组
     * 方法级,类级，全局，三者取并集
     */
    String[] tags() default {};

    /**
     * 操作成功的模板
     * 类级模板+方法级模板=最终模板
     */
    String successTemplate();

    /**
     * 操作失败的模板
     * 如果不设置，则操作失败时不会记录日志
     * 类级模板+方法级模板=最终模板
     */
    String failTemplate() default "";

    /**
     * 自定义返回值的变量名，供表达式中使用
     * 方法级覆盖类级设置，类级覆盖全局配置
     */
    String resultName() default "result";

    /**
     * 执行该条日志的执行器
     * 方法级覆盖类级设置，类级覆盖全局配置
     */
    Class<? extends LogExecutor>[] executors() default {ConsoleLogExecutor.class};

    /**
     * 后置处理器
     * 方法级覆盖类级设置，类级覆盖全局配置
     */
    Class<? extends ResultPostProcessor> processor() default NopResultPostProcessor.class;


}
