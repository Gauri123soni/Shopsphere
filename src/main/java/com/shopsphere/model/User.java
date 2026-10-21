package com.shopsphere.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String mobile;
    private String role;
    private boolean status;
    private Timestamp createdAt;

    public User() {
    }

    public User(int userId, String name, String email, String passwordHash, String mobile, String role,
                boolean status, Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.mobile = mobile;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
