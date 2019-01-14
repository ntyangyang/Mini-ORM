package com.eason.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Eason
 * @createTime 2019-01-14 21:38
 * @description 该注解用来设置表名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ORMTable {
    public String name() default "";
}
