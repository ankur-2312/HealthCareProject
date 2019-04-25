package com.healthcareproject.model;

public class model {

    private String key;

    private String name;

    public model(String key, String name) {
        this.key = key;
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
