package com.xz.jlw2.entity;

public class ClassifyEntity {
    private String name;
    private int src;

    public ClassifyEntity() {
    }

    public ClassifyEntity(String name, int src) {
        this.name = name;
        this.src = src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }
}
