# Configuration Framework

This is a framework that can simplify the process for programmers to process configuration files.  
It has the following features:

- better profile operation through proxy mode
- automatic refresh of file updates
- json, yaml and properties formats
- configuration file group, that is, all configuration files under the specified folder of batch agent
- asynchronous update and synchronous modification
- By annotating the binding node, there will also be a default implementation, that is, binding according to the method name

**Simpler, for example you can quickly access a yaml configuration using the method provided in Configuration Framework.**  
First, suppose you have a yaml configuration file named `config.yml` as follows
```yaml
settings:
  enable: true
  debug: false
```
Then, write the corresponding configuration interface according to the yaml configuration file
```java
@Configuration
interface SettingsConfig extends AutoConfiguration {
    @Binding("settings.enable")
    boolean isEnabled();
    
    @Binding("settings.debug")
    boolean isDebug();
    
}
```
Next, you can enhance the SettingsConfig using `ConfigurationEnhancer#enhance` and get a proxy SettingsConfig object
```java
SettingsConfig settingsConfig = ConfigurationEnhancer.enhance(MyConfigGroup.class);
settingsConfig.isEnabled();
settingsConfig.isDebug();
```
For more complex ones, suppose you have a folder `datas` which contains all players' data, and the data of each player is like this
```yaml
name: 'rokuko'
password: '******'
count: 100
hobbies:
 - 'programming'
 - 'gaming'
 - 'traveling'
```
Then, you can generate proxy objects for them by configuring file groups
```java
@Configuration(group = true, folder = "datas")
interface DataConfig extends AutoConfigurationGroup<DataConfig> {

    @Binding("name")
    String getName();

    @Binding("password")
    String getPassword();

    @Binding("count")
    Integer getCount();

    @Binding("hobbies")
    List<String> getHobbies();

}
```
Finally, you can obtain all the configuration file data through the enhanced object's `groups` method
```java
DataConfig datas = ConfigurationEnhancer.enhance(DataConfig.class);
datas.forEach(playerData -> {
    logger.info(
        "name: {}, password: {}, count: {}, hobbies: {}",
        playerData.getName(),
        playerData.getPassword(),
        playerData.getCount(),
        playerData.getHobbies()
    );
});
```
Due to the space problem, only the most basic usage is shown here.   
If you are interested in this framework, welcome star and contribute code to it.  
Your support will be the greatest encouragement