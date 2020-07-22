package edu.jphoebe.demo.springmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * JPService class
 *
 * @author 蒋时华
 * @date 2019/12/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface JPService {

    String value() default "";

}
