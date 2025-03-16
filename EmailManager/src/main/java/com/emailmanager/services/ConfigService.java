package com.emailmanager.services;

import com.emailmanager.models.EmailConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
    private static final String CONFIG_FILE = "/config.json";

    public EmailConfig loadConfig() throws Exception {
        logger.info("Loading configuration");
        EmailConfig config;

        // Try to load base configuration from file
        try (InputStream is = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                logger.warn("Configuration file not found, using empty configuration");
                config = new EmailConfig();
            } else {
                ObjectMapper mapper = new ObjectMapper();
                config = mapper.readValue(is, EmailConfig.class);
            }
        }

        // Override with environment variables if present
        String clientId = System.getenv("EMAIL_MANAGER_CLIENT_ID");
        String clientSecret = System.getenv("EMAIL_MANAGER_CLIENT_SECRET");
        String tenantId = System.getenv("EMAIL_MANAGER_TENANT_ID");
        String userEmail = System.getenv("EMAIL_MANAGER_USER_EMAIL");

        if (clientId != null) config.setClientId(clientId);
        if (clientSecret != null) config.setClientSecret(clientSecret);
        if (tenantId != null) config.setTenantId(tenantId);
        if (userEmail != null) config.setUserEmail(userEmail);

        // Validate configuration
        if (config.getClientId() == null || config.getClientId().isEmpty() ||
            config.getClientSecret() == null || config.getClientSecret().isEmpty() ||
            config.getTenantId() == null || config.getTenantId().isEmpty() ||
            config.getUserEmail() == null || config.getUserEmail().isEmpty()) {
            throw new IllegalStateException(
                "Missing required configuration. Please set the following environment variables:\n" +
                "EMAIL_MANAGER_CLIENT_ID\n" +
                "EMAIL_MANAGER_CLIENT_SECRET\n" +
                "EMAIL_MANAGER_TENANT_ID\n" +
                "EMAIL_MANAGER_USER_EMAIL"
            );
        }

        logger.info("Configuration loaded successfully for user: {}", config.getUserEmail());
        return config;
    }
} 