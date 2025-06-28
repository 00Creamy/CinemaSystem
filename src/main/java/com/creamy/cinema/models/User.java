package com.creamy.cinema.models;

public class User {
    public enum AccessLevel {
        CUSTOMER("Customer", 0),
        STAFF("Staff", 1),
        MANAGER("Manager", 2),
        ADMIN("Admin", 3);

        private final String name;
        private final int level;

        AccessLevel(String name, int level) {
            this.name = name;
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }

        public static AccessLevel getLevelByName(String name) {
            return switch (name) {
                case "Customer" -> AccessLevel.CUSTOMER;
                case "Staff" -> AccessLevel.STAFF;
                case "Manager" -> AccessLevel.MANAGER;
                case "Admin" -> AccessLevel.ADMIN;
                default -> null;
            };
        }

        public static AccessLevel getLevelByLevel(int level) {
            return switch (level) {
                case 0 -> AccessLevel.CUSTOMER;
                case 1 -> AccessLevel.STAFF;
                case 2 -> AccessLevel.MANAGER;
                case 3 -> AccessLevel.ADMIN;
                default -> null;
            };
        }
    }

    private int userId;
    private int sessionVersion;
    private String username;
    private String password;
    private AccessLevel accessLevel;
    private String name;
    private String email;
    private String phoneNo;
    private boolean deleted;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSessionVersion() {
        return sessionVersion;
    }

    public void setSessionVersion(int sessionVersion) {
        this.sessionVersion = sessionVersion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
