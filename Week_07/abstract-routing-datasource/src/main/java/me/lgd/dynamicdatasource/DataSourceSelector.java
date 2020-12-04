package me.lgd.dynamicdatasource;

import java.lang.annotation.*;

/**
 * @author lgd
 * @date 2020/12/1 21:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataSourceSelector {

    DynamicDataSourceEnum value() default DynamicDataSourceEnum.PRIMARY;

    boolean clear() default true;

}
