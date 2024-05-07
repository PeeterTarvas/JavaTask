package com.test.helmes.config.security.user;

public enum UserRole {
    USER, ADMIN;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    // spring roles must have role prefix, we have described them in ApplicationRoles.java
    public String toApplicationRole() {
        return "ROLE_" + this.name();
    }

}
