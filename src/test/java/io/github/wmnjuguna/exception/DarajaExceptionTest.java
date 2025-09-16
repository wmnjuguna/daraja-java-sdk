package io.github.wmnjuguna.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarajaExceptionTest {

    @Test
    void constructor_WithMessage_ShouldSetMessage() {
        String message = "Test error message";
        DarajaException exception = new DarajaException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
        assertNull(exception.getErrorCode());
        assertEquals(0, exception.getHttpStatus());
        assertFalse(exception.hasErrorCode());
        assertFalse(exception.hasHttpStatus());
    }

    @Test
    void constructor_WithMessageAndCause_ShouldSetBoth() {
        String message = "Test error message";
        RuntimeException cause = new RuntimeException("Cause");
        DarajaException exception = new DarajaException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertNull(exception.getErrorCode());
        assertEquals(0, exception.getHttpStatus());
    }

    @Test
    void constructor_WithMessageErrorCodeAndHttpStatus_ShouldSetAll() {
        String message = "Test error message";
        String errorCode = "ERR001";
        int httpStatus = 400;

        DarajaException exception = new DarajaException(message, errorCode, httpStatus);

        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(httpStatus, exception.getHttpStatus());
        assertTrue(exception.hasErrorCode());
        assertTrue(exception.hasHttpStatus());
    }

    @Test
    void constructor_WithAllParameters_ShouldSetAll() {
        String message = "Test error message";
        RuntimeException cause = new RuntimeException("Cause");
        String errorCode = "ERR001";
        int httpStatus = 500;

        DarajaException exception = new DarajaException(message, cause, errorCode, httpStatus);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(httpStatus, exception.getHttpStatus());
        assertTrue(exception.hasErrorCode());
        assertTrue(exception.hasHttpStatus());
    }

    @Test
    void hasErrorCode_WithNullErrorCode_ShouldReturnFalse() {
        DarajaException exception = new DarajaException("message", null, 400);
        assertFalse(exception.hasErrorCode());
    }

    @Test
    void hasErrorCode_WithEmptyErrorCode_ShouldReturnFalse() {
        DarajaException exception = new DarajaException("message", "  ", 400);
        assertFalse(exception.hasErrorCode());
    }

    @Test
    void hasErrorCode_WithValidErrorCode_ShouldReturnTrue() {
        DarajaException exception = new DarajaException("message", "ERR001", 400);
        assertTrue(exception.hasErrorCode());
    }

    @Test
    void hasHttpStatus_WithZeroStatus_ShouldReturnFalse() {
        DarajaException exception = new DarajaException("message", "ERR001", 0);
        assertFalse(exception.hasHttpStatus());
    }

    @Test
    void hasHttpStatus_WithNegativeStatus_ShouldReturnFalse() {
        DarajaException exception = new DarajaException("message", "ERR001", -1);
        assertFalse(exception.hasHttpStatus());
    }

    @Test
    void hasHttpStatus_WithPositiveStatus_ShouldReturnTrue() {
        DarajaException exception = new DarajaException("message", "ERR001", 400);
        assertTrue(exception.hasHttpStatus());
    }

    @Test
    void toString_WithErrorCodeAndHttpStatus_ShouldIncludeBoth() {
        DarajaException exception = new DarajaException("Test message", "ERR001", 400);
        String toString = exception.toString();

        assertTrue(toString.contains("Test message"));
        assertTrue(toString.contains("ERR001"));
        assertTrue(toString.contains("400"));
        assertTrue(toString.contains("[Error Code: ERR001]"));
        assertTrue(toString.contains("[HTTP Status: 400]"));
    }

    @Test
    void toString_WithOnlyErrorCode_ShouldIncludeErrorCode() {
        DarajaException exception = new DarajaException("Test message", "ERR001", 0);
        String toString = exception.toString();

        assertTrue(toString.contains("Test message"));
        assertTrue(toString.contains("ERR001"));
        assertFalse(toString.contains("HTTP Status"));
    }

    @Test
    void toString_WithOnlyHttpStatus_ShouldIncludeHttpStatus() {
        DarajaException exception = new DarajaException("Test message", null, 400);
        String toString = exception.toString();

        assertTrue(toString.contains("Test message"));
        assertTrue(toString.contains("400"));
        assertFalse(toString.contains("Error Code"));
    }

    @Test
    void toString_WithoutErrorCodeOrHttpStatus_ShouldNotIncludeExtra() {
        DarajaException exception = new DarajaException("Test message");
        String toString = exception.toString();

        assertTrue(toString.contains("Test message"));
        assertFalse(toString.contains("Error Code"));
        assertFalse(toString.contains("HTTP Status"));
    }
}