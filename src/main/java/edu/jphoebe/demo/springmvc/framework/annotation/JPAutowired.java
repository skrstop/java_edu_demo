package edu.jphoebe.demo.springmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * JPAutowired class
 *
 * @author 蒋时华
 * @date 2017/12/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface JPAutowired {

    String value() default "";

    boolean required() default true;

}
