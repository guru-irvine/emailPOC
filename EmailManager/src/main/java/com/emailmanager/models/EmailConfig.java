package com.emailmanager.models;

public class EmailConfig {
    private String clientId;
    private String clientSecret;
    private String tenantId;
    private String userEmail;
    private String csvFilePath;
    private String emailStoragePath;

    // Default constructor for Jackson
    public EmailConfig() {
    }

    // Getters and setters
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public String getEmailStoragePath() {
        return emailStoragePath;
    }

    public void setEmailStoragePath(String emailStoragePath) {
        this.emailStoragePath = emailStoragePath;
    }
} 