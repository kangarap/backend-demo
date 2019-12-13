package com.demo.backend.config.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 日志注解类
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 日志描述
     * @return
     */
    @AliasFor("desc")
    String value() default "";

    @AliasFor("value")
    String desc() default "";


    /**
     * 是否不记录日志
     * @return
     */
    boolean ignore() default false;
}
