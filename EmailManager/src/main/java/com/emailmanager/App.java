package com.emailmanager;

import com.emailmanager.models.EmailConfig;
import com.emailmanager.models.EmailRecord;
import com.emailmanager.services.ConfigService;
import com.emailmanager.services.EmailStorageService;
import com.emailmanager.services.GraphService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static GraphService graphService;
    private static EmailStorageService emailStorageService;

    public static void main(String[] args) {
        try {
            initialize();
            boolean running = true;

            while (running) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        downloadNewEmails();
                        break;
                    case 2:
                        replyToEmail();
                        break;
                    case 3:
                        listAllEmails();
                        break;
                    case 4:
                        viewConversationThread();
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            logger.error("Application error: ", e);
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void initialize() throws Exception {
        ConfigService configService = new ConfigService();
        EmailConfig config = configService.loadConfig();
        graphService = new GraphService(config);
        emailStorageService = new EmailStorageService("emails.csv");
        logger.info("Application initialized successfully");
    }

    private static void displayMenu() {
        System.out.println("\nEmail Manager Menu:");
        System.out.println("1. Download new emails");
        System.out.println("2. Reply to an email");
        System.out.println("3. List all emails");
        System.out.println("4. View conversation thread");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void downloadNewEmails() {
        try {
            System.out.println("Downloading new emails...");
            List<EmailRecord> newEmails = graphService.getEmailsAsync();
            emailStorageService.saveEmails(newEmails);
            System.out.println("Downloaded " + newEmails.size() + " emails successfully.");
        } catch (Exception e) {
            logger.error("Error downloading emails: ", e);
            System.out.println("Failed to download emails: " + e.getMessage());
        }
    }

    private static void replyToEmail() {
        try {
            List<EmailRecord> emails = emailStorageService.loadEmails();
            if (emails.isEmpty()) {
                System.out.println("No emails available.");
                return;
            }

            System.out.print("Enter the index of the email to reply to: ");
            int index = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (index < 1 || index > emails.size()) {
                System.out.println("Invalid email index.");
                return;
            }

            EmailRecord email = emails.get(index - 1);
            System.out.print("Enter your reply message: ");
            String replyMessage = scanner.nextLine();

            graphService.replyToEmail(email.getId(), replyMessage);
            System.out.println("Reply sent successfully.");
        } catch (Exception e) {
            logger.error("Error replying to email: ", e);
            System.out.println("Failed to send reply: " + e.getMessage());
        }
    }

    private static void listAllEmails() {
        try {
            List<EmailRecord> emails = emailStorageService.loadEmails();
            if (emails.isEmpty()) {
                System.out.println("No emails available.");
                return;
            }

            System.out.println("\nAll Emails:");
            for (int i = 0; i < emails.size(); i++) {
                EmailRecord email = emails.get(i);
                System.out.printf("%d. [%s] From: %s, Subject: %s%n",
                    i + 1, email.getReceivedDateTime(), email.getFrom(), email.getSubject());
            }
        } catch (Exception e) {
            logger.error("Error listing emails: ", e);
            System.out.println("Failed to list emails: " + e.getMessage());
        }
    }

    private static void viewConversationThread() {
        try {
            List<EmailRecord> emails = emailStorageService.loadEmails();
            if (emails.isEmpty()) {
                System.out.println("No emails available.");
                return;
            }

            System.out.print("Enter the index of the email to view its conversation: ");
            int index = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (index < 1 || index > emails.size()) {
                System.out.println("Invalid email index.");
                return;
            }

            EmailRecord email = emails.get(index - 1);
            List<EmailRecord> thread = emailStorageService.getConversationThread(email.getConversationId());
            
            System.out.println("\nConversation Thread:");
            for (int i = 0; i < thread.size(); i++) {
                EmailRecord threadEmail = thread.get(i);
                System.out.printf("%d. [%s] From: %s, Subject: %s, Type: %s%n",
                    i + 1, threadEmail.getReceivedDateTime(), threadEmail.getFrom(),
                    threadEmail.getSubject(), threadEmail.getMessageType());
                
                try {
                    String content = emailStorageService.getEmailContent(threadEmail.getId());
                    System.out.println("Content:");
                    System.out.println(content);
                    System.out.println("----------------------------------------");
                } catch (IOException e) {
                    System.out.println("(Email content not available)");
                    System.out.println("----------------------------------------");
                }
            }
        } catch (Exception e) {
            logger.error("Error viewing conversation thread: ", e);
            System.out.println("Failed to view conversation thread: " + e.getMessage());
        }
    }
} 