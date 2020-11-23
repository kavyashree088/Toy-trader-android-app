package com.example.toytrader.admin;

public class ToyModel {
    private String name;
    private String toyId;

    public ToyModel(String name, String toyId) {
        this.name = name;
        this.toyId = toyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToyId() {
        return toyId;
    }

    public void setToyId(String toyId) {
        this.toyId = toyId;
    }
}
