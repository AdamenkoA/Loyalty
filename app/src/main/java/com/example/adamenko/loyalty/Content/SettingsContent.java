package com.example.adamenko.loyalty.Content;

/**
 * Created by Adamenko on 03.10.2016.
 */

public class SettingsContent {
    public SettingsContent() {
    }

    private Integer id;
    private String key;
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SettingsContent(Integer id, String key, String value) {

        this.id = id;
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
