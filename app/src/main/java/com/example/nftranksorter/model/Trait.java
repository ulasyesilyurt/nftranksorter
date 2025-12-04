package com.example.nftranksorter.model;

public class Trait {
    private String key;
    private String value;

    public Trait(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return key;
    }
    public String getValue(){
        return value;
    }
}
