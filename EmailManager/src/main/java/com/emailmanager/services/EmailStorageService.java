package com.emailmanager.services;

import com.emailmanager.models.EmailRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmailStorageService {
    private static final Logger logger = LoggerFactory.getLogger(EmailStorageService.class);
    private final Path csvFilePath;
    private final Path storagePath;

    public EmailStorageService(String filename) {
        // Create base storage directory
        this.storagePath = Paths.get("emails", "storage");
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            logger.warn("Failed to create storage directory: {}", e.getMessage());
        }

        // Set CSV file path inside the emails directory
        this.csvFilePath = Paths.get("emails", filename);
        try {
            Files.createDirectories(csvFilePath.getParent());
        } catch (IOException e) {
            logger.warn("Failed to create emails directory: {}", e.getMessage());
        }
    }

    public void saveEmails(List<EmailRecord> emails) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        logger.info("Saving {} emails to CSV file: {}", emails.size(), csvFilePath);
        
        // First, load existing emails to avoid duplicates
        List<EmailRecord> existingEmails = loadEmails();
        List<EmailRecord> newEmails = new ArrayList<>();

        // Check for new emails
        for (EmailRecord email : emails) {
            if (existingEmails.stream().noneMatch(e -> e.getId().equals(email.getId()))) {
                newEmails.add(email);
            }
        }

        if (newEmails.isEmpty()) {
            logger.info("No new emails to save");
            return;
        }

        // Add new emails to existing ones
        List<EmailRecord> allEmails = new ArrayList<>(existingEmails);
        allEmails.addAll(newEmails);

        // Sort all emails by received date
        allEmails.sort(Comparator.comparing(EmailRecord::getReceivedDateTime));

        // Save to CSV
        try (FileWriter writer = new FileWriter(csvFilePath.toFile())) {
            new StatefulBeanToCsvBuilder<EmailRecord>(writer)
                .build()
                .write(allEmails);
            logger.info("Saved {} emails to CSV file", allEmails.size());
        }

        // Save new email content as .eml files
        for (EmailRecord email : newEmails) {
            saveEmailContent(email);
        }
    }

    private void saveEmailContent(EmailRecord email) {
        Path emlPath = storagePath.resolve(email.getId() + ".eml");
        try {
            // Create a simple .eml file with basic email information
            String emlContent = String.format(
                "From: %s\r\n" +
                "Subject: %s\r\n" +
                "Date: %s\r\n" +
                "Message-ID: %s\r\n" +
                "Conversation-ID: %s\r\n" +
                "References: %s\r\n" +
                "Type: %s\r\n",
                email.getFrom(),
                email.getSubject(),
                email.getReceivedDateTime(),
                email.getInternetMessageId(),
                email.getConversationId(),
                email.getReferences() != null ? email.getReferences() : "",
                email.getMessageType()
            );

            Files.writeString(emlPath, emlContent);
            logger.info("Saved email content to {}", emlPath);
        } catch (IOException e) {
            logger.error("Failed to save email content for ID {}: {}", email.getId(), e.getMessage());
        }
    }

    public List<EmailRecord> loadEmails() throws IOException {
        if (!Files.exists(csvFilePath)) {
            logger.info("CSV file not found at {}, returning empty list", csvFilePath);
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(csvFilePath.toFile())) {
            List<EmailRecord> emails = new CsvToBeanBuilder<EmailRecord>(reader)
                .withType(EmailRecord.class)
                .build()
                .parse();
            logger.info("Loaded {} emails from CSV file: {}", emails.size(), csvFilePath);
            return emails;
        }
    }

    public List<EmailRecord> getConversationThread(String conversationId) throws IOException {
        List<EmailRecord> allEmails = loadEmails();
        List<EmailRecord> thread = allEmails.stream()
            .filter(email -> conversationId.equals(email.getConversationId()))
            .sorted(Comparator.comparing(EmailRecord::getReceivedDateTime))
            .collect(Collectors.toList());
        
        logger.info("Found {} emails in conversation thread {}", thread.size(), conversationId);
        return thread;
    }

    public String getEmailContent(String emailId) throws IOException {
        Path emlPath = storagePath.resolve(emailId + ".eml");
        if (Files.exists(emlPath)) {
            return Files.readString(emlPath);
        }
        throw new IOException("Email content not found for ID: " + emailId);
    }

    public Path getStoragePath() {
        return storagePath;
    }
} 