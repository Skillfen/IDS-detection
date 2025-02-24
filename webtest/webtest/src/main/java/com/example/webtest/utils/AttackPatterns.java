package com.example.webtest.utils;

public class AttackPatterns {

    public static boolean isSqlInjection(String url) {
        return url.toLowerCase().contains("select") || url.toLowerCase().contains("union");
    }

    public static boolean isXssAttack(String url) {
        return url.toLowerCase().contains("<script>");
    }
}
