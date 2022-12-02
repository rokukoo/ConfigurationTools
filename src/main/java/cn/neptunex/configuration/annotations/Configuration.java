package cn.neptunex.configuration.annotations;

import cn.neptunex.configuration.enums.ConfigurationType;
import cn.neptunex.configuration.interfaces.ConfigurationMapper;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface Configuration {

    String value() default "config.yml";
    ConfigurationType type() default ConfigurationType.YAML;
    String root() default "";

    // NOTE: 这个地方有待优化, 因为如果开启了文件组模式, 那这里可能会有批量更新的风险, 慎用
    boolean autoReload() default false;
    // TODO: 这个地方到时候用来指定文件变化的回调
    Class<?> reloadCallback() default void.class;
    boolean autoSave() default false;

    boolean group() default false;
    String folder() default "";
//    Class<? extends ConfigurationMapper> mapper() default ConfigurationMapper.class;

}
