package com.devopsbuddy.enums;

/**
 * Created by Jayden on 7/13/2017.
 */
public enum PlanEnum {
    BASIC(1,"Basic"),
    PRO(2,"Pro");

    private final int id;

    private final String name;

    PlanEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
