package com.zhaoxinms.contract.conf.util.compare;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
/**
 * 字段标记注解
 *
 * @author zyqok
 * @since 2022/05/05
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Compare {
 
    /**
     * 字段名称
     */
    String value();  //变更字段名
    boolean showDetail() default true;  //是否显示变更明细
    String dict() default "";  //字典类型用于转化转世
    String dateFormat() default "";  //日期类型用于转换格式
}
