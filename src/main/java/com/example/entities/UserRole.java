package com.example.entities;

enum UserRole {
    ROLE_GUEST("ROLE_GUEST"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;
    UserRole(String role) {
        this.role = role;
    }
    private String getRole() { return role; }
}
