package com.emailmanager.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeConverter extends AbstractBeanField<OffsetDateTime, String> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            return value != null ? OffsetDateTime.parse(value, formatter) : null;
        } catch (Exception e) {
            throw new CsvDataTypeMismatchException(value, OffsetDateTime.class, "Invalid date-time format");
        }
    }

    @Override
    protected String convertToWrite(Object value) {
        return value != null ? ((OffsetDateTime) value).format(formatter) : "";
    }
} 