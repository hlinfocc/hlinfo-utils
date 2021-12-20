package net.hlinfo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 声明的一个 POJO 所对应的数据表名。
 *
 * This Class Powered by Nutz annotation
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MTable {

    String value() default "";

    /** 表名前缀 */
    String prefix() default "";

    /** 表名后缀 */
    String suffix() default "";
}
