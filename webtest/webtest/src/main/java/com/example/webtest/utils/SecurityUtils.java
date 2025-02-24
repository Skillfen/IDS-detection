package com.example.webtest.utils;

public class SecurityUtils {

    // Check if input contains SQL Injection patterns
    public static boolean isSQLInjection(String input) {
        String[] sqlInjectionPatterns = {"'", "--", "/*", "*/", "DROP", "SELECT", "INSERT", "UPDATE", "DELETE", "OR", "AND"};
        for (String pattern : sqlInjectionPatterns) {
            if (input != null && input.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    // Check if input contains XSS patterns
    public static boolean isXSS(String input) {
        String[] xssPatterns = {"<script>", "</script>", "<img", "<iframe", "<object", "<applet", "<embed"};
        for (String pattern : xssPatterns) {
            if (input != null && input.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
}
