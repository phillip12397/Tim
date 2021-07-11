package com.developfuture.fortknox.spinner;

public class Investments {

    private String id;
    private String name;

    public Investments(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public String getId() { return id; }

    public String getName() { return name; }
}
