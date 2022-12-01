package cn.neptunex.configuration.enums;

public enum ConfigurationType {
    JSON("json"),
    YAML("yaml"),
    UNKNOWN("UNKNOWN");

    private final String name;

    public String getName(){
        return this.name;
    }

    ConfigurationType(String name){
        this.name = name;
    }

}
