package com.nbu.graduation.spring.model.enums;

public enum Role {
    STUDENT,
    TEACHER;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
