package cn.neptunex.test;

import cn.neptunex.configuration.annotations.Binding;
import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.interfaces.IConfiguration;
import cn.neptunex.configuration.ConfigurationProcessor;
import org.junit.jupiter.api.Test;

public class AutoConfigurationTest {

    @Test
    public void test1(){
        MyConfig myConfig = ConfigurationProcessor.process(MyConfig.class);
        boolean isEnabled = myConfig.isEnabled();
        System.out.println(isEnabled);
    }

}

@Configuration
interface MyConfig extends IConfiguration{

    @Binding("settings.enable")
    boolean isEnabled();

}
