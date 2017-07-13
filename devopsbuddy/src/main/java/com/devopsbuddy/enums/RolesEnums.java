package com.devopsbuddy.enums;

/**
 * Created by Jayden on 7/13/2017.
 */
public enum RolesEnums {
    BASIC(1,"ROLE_BASIC"),
    PRO(2,"ROLE_PRO"),
    ADMIN(3,"ROLE_ADMIN");

    private final int id;

    private final String roleName;

    RolesEnums(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }
}
