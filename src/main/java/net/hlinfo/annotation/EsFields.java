/**
 * 
 */
package net.hlinfo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author hlinfo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public @interface EsFields {
    /**
     * 类型，默认为text
     */
    ESFieldType type() default ESFieldType.Auto;


    /**
     * 分词器，如：ik_max_word
     */
    String analyzer() default  "";


    /**
     * 是否使用ik分词器，此属性和analyzer 二选一
     * @return
     */
    boolean ikAnalyzer() default  false;
 
    /**
     * 是否 keyword": { "type": "keyword" }
     */
    boolean fieldsKeywordType() default false;
 

    enum ESFieldType {
        Auto, //
        Text, //
        Keyword, //
        Long, //
        Integer, //
        Short, //
        Byte, //
        Double, //
        Float, //
        Half_Float, //
        Scaled_Float, //
        Date, //
        Date_Nanos, //
        Boolean, //
        Binary, //
        Integer_Range, //
        Float_Range, //
        Long_Range, //
        Double_Range, //
        Date_Range, //
        Ip_Range, //
        Object, //
        Nested, //
        Ip, //
        TokenCount, //
        Percolator, //
        Flattened, //
        Search_As_You_Type, //
        /** @since 4.1 */
        Rank_Feature, //
        /** @since 4.1 */
        Rank_Features //
    }
}
