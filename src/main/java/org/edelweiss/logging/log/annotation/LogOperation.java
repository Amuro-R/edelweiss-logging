package org.edelweiss.logging.log.annotation;


import org.edelweiss.logging.log.el.ILogParseFunction;
import org.edelweiss.logging.pojo.log.OperationTypeEnum;

import java.lang.annotation.*;

/**
 * 模板变量说明
 * {#xxx#} 方法执行前的SpEl变量
 * {@xxx@} 方法执行前的函数变量，需创建{@link ILogParseFunction}类型的spring bean
 * [#xxx#] 方法执行后的SpEl变量
 * [@xxx@] 方法执行后的函数变量，需创建{@link ILogParseFunction}类型的spring bean
 * <p>
 * 当该注解作用于类级别时，模板将作为所有方法级别的模板的前缀使用
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface LogOperation {

    /**
     * 操作人，默认为登录用户的姓名
     */
    String operator() default "";

    /**
     * 请求所属的IP地址
     */
    String ip() default "";

    /**
     * 操作类型，必须设置，方法级别设置会覆盖类级别设置
     */
    OperationTypeEnum operationType() default OperationTypeEnum.NONE;

    /**
     * 操作成功的模板，必须设置
     */
    String successTemplate();

    /**
     * 操作失败的模板，如果不设置，则操作失败时不会记录日志
     */
    String failTemplate() default "";

    /**
     * 自定义返回值得变量名，供表达式中使用，方法级别设置会覆盖类级别设置
     */
    String resultName() default "result";

}
