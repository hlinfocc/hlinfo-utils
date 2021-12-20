package net.hlinfo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明一组数据表的索引
 *
 * @see MIndex
 *
 * This Class Powered by Nutz annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MTableIndexes {

    MIndex[] value();

}
