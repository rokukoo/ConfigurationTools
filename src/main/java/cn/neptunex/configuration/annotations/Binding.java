package cn.neptunex.configuration.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Binding {

    String value() default "";

    // This types signify what the method will do, enums get, set.
    String type() default "get";

}
