package com.spzx.common.log.annotation;

import java.lang.annotation.*;

import com.spzx.common.log.enums.BusinessType;
import com.spzx.common.log.enums.OperatorType;

/**
 * 自定义操作日志记录注解
 *
 * @author spzx
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD }) //应用目标注解。说明一下我们注解可以应用在哪里？
@Retention(RetentionPolicy.RUNTIME) //保持性策略。
@Documented //文档化注解。注解是否会生成到Javadoc API文档中。
@Inherited //标注继承。
public @interface Log
{
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    public String[] excludeParamNames() default {};
}
