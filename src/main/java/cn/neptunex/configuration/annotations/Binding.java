package cn.neptunex.configuration.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD})
public @interface Binding {

    String value() default "config.yml";
    // This types signify what the method will do, enums get, set.
    String type() default "get";

}