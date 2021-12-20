package net.hlinfo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 给出字段的更加精确的数据库类型描述，方便 创建数据表
 *
 * This Class Powered by Nutz annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface MColDefine {

	/**
	 * 数据库字段类型
	 * @return  MColType类型
	 * @see net.hlinfo.mybatis.annotation.MColType
	 */
	MColType type() default MColType.AUTO;

	/**
	 * 宽度/长度, 例如定义字符串长度为1024 就写  width=1024
	 * @return 宽度/长度
	 */
	int width() default 0;

	/**
	 * 精度,小数点后多少位,默认是2
	 * @return 精度
	 */
	int precision() default 2;

	/**
	 * 是否为非空,默认为false
	 * @return 是否为非空,默认为false
	 */
	boolean notNull() default false;

	/**
	 * @return 是否为无符号数值,默认为false
	 */
	boolean unsigned() default false;

	/**
	 * 描述当前字段是否自增，如果和 @Id 冲突，以 @Id 的优先
	 * @return 是否自增
	 */
	boolean auto() default false;

	/**
	 * 自定义数据库字段类型, 例如写  customType="image" 等, 然后<b>请务必再设置type属性!!</b>
	 * @return 自定义数据类型,可以任意合法的类型声明,仅用于建表
	 */
	String customType() default "";

	/**
	 * @return 描述当前字段是否可插入
	 */
	boolean insert() default true;

	/**
	 * @return 描述当前字段是否可更新
	 */
	boolean update() default true;


}
