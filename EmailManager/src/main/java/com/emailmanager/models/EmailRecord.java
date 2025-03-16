package com.emailmanager.models;

import com.emailmanager.utils.OffsetDateTimeConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;

import java.time.OffsetDateTime;

public class EmailRecord {
    @CsvBindByPosition(position = 0)
    private String id;

    @CsvBindByPosition(position = 1)
    private String subject;

    @CsvCustomBindByPosition(position = 2, converter = OffsetDateTimeConverter.class)
    private OffsetDateTime receivedDateTime;

    @CsvBindByPosition(position = 3)
    private String from;

    @CsvBindByPosition(position = 4)
    private String internetMessageId;

    @CsvBindByPosition(position = 5)
    private String conversationId;

    @CsvBindByPosition(position = 6)
    private String references;

    @CsvBindByPosition(position = 7)
    private String messageType;

    // Default constructor for CSV binding
    public EmailRecord() {
    }

    // Constructor with all fields
    public EmailRecord(String id, String subject, OffsetDateTime receivedDateTime, String from,
                      String internetMessageId, String conversationId, String references) {
        this.id = id;
        this.subject = subject;
        this.receivedDateTime = receivedDateTime;
        this.from = from;
        this.internetMessageId = internetMessageId;
        this.conversationId = conversationId;
        this.references = references;
        this.messageType = references == null || references.isEmpty() ? "Original" : "Reply";
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public OffsetDateTime getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(OffsetDateTime receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getInternetMessageId() {
        return internetMessageId;
    }

    public void setInternetMessageId(String internetMessageId) {
        this.internetMessageId = internetMessageId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
        this.messageType = references == null || references.isEmpty() ? "Original" : "Reply";
    }

    public String getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return String.format("[%s] From: %s, Subject: %s, Received: %s, Type: %s",
                id, from, subject, receivedDateTime, messageType);
    }
} 