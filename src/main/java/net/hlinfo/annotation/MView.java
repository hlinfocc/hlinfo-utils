package net.hlinfo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个实体，应该从何处获取。默认的，会从 '@MTable' 注解声明的表名获取。
 * <p>
 * 但是，某些时候，为了获得一些统计信息，你可能需要创建一个视图，而希望从视图获取自己的对象。
 * <p>
 * 那么在你的类上声明本注解，就可以做到这一点
 * This Class Powered by Nutz annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MView {
    String value() default "";

    /** 视图前缀 */
    String prefix() default "";

    /** 视图后缀 */
    String suffix() default "";
}
