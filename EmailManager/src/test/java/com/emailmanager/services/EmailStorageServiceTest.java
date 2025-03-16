package com.emailmanager.services;

import com.emailmanager.models.EmailRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailStorageServiceTest {
    @TempDir
    Path tempDir;

    private EmailStorageService emailStorageService;
    private static final String TEST_FILE = "test_emails.csv";

    @BeforeEach
    void setUp() {
        String csvPath = tempDir.resolve(TEST_FILE).toString();
        emailStorageService = new EmailStorageService(csvPath);
    }

    @Test
    void saveAndLoadEmails() throws Exception {
        // Prepare test data
        List<EmailRecord> testEmails = Arrays.asList(
            new EmailRecord("1", "Test Subject 1", OffsetDateTime.now(), "sender1@test.com",
                "msg1", "conv1", null),
            new EmailRecord("2", "Test Subject 2", OffsetDateTime.now(), "sender2@test.com",
                "msg2", "conv1", "msg1")
        );

        // Save emails
        emailStorageService.saveEmails(testEmails);

        // Load and verify
        List<EmailRecord> loadedEmails = emailStorageService.loadEmails();
        assertEquals(2, loadedEmails.size());
        assertEquals("Test Subject 1", loadedEmails.get(0).getSubject());
        assertEquals("Test Subject 2", loadedEmails.get(1).getSubject());
    }

    @Test
    void getConversationThread() throws Exception {
        // Prepare test data with a conversation thread
        List<EmailRecord> testEmails = Arrays.asList(
            new EmailRecord("1", "Original", OffsetDateTime.now().minusHours(2), "sender1@test.com",
                "msg1", "conv-1", null),
            new EmailRecord("2", "Reply 1", OffsetDateTime.now().minusHours(1), "sender2@test.com",
                "msg2", "conv-1", "msg1"),
            new EmailRecord("3", "Different Thread", OffsetDateTime.now(), "sender3@test.com",
                "msg3", "conv-2", null)
        );

        // Save emails
        emailStorageService.saveEmails(testEmails);

        // Get conversation thread
        List<EmailRecord> thread = emailStorageService.getConversationThread("conv-1");
        assertEquals(2, thread.size());
        assertEquals("Original", thread.get(0).getSubject());
        assertEquals("Reply 1", thread.get(1).getSubject());
    }

    @Test
    void loadEmailsFromNonExistentFile() throws Exception {
        List<EmailRecord> emails = emailStorageService.loadEmails();
        assertTrue(emails.isEmpty());
    }
} 