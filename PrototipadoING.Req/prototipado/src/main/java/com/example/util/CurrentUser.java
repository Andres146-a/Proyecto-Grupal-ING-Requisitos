package com.example.util;


public class CurrentUser {
    private static String role;

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        CurrentUser.role = role;
    }
}
