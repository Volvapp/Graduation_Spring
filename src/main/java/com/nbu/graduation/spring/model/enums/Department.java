package com.nbu.graduation.spring.model.enums;

public enum Department {
    ENGINEERING, FINANCE, MARKETING, SALES, IT;

    public String getDisplayName() {
        return switch (this) {
            case ENGINEERING -> "Инженерство";
            case FINANCE -> "Финанси";
            case MARKETING -> "Маркетинг";
            case SALES -> "Продажби";
            case IT -> "ИТ";
        };
    }
}
