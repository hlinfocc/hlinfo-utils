package net.hlinfo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可以为表或字段添加相应的注释。
 *
 * <b>动态表名暂时不支持</b>
 *
 * This Class Powered by Nutz annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Documented
public @interface MComment {
    String value() default "";
}
