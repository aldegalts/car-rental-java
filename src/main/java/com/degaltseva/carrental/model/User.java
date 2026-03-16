package com.degaltseva.carrental.model;

public class User {

    private Long id;
    private String username;
    private String passwordHash;
    private Long roleId;

    public User() {}

    public User(Long id, String username, String passwordHash, Long roleId) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
}
