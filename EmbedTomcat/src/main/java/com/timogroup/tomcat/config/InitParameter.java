package com.timogroup.tomcat.config;

/**
 * Created by TimoRD on 2016/7/8.
 */
public class InitParameter {

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public InitParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "InitParameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
