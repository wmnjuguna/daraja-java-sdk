package io.github.wmnjuguna.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class StkPushPasswordUtilTest {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Test
    void generateTimestamp_ShouldReturnCorrectFormat() {
        String timestamp = StkPushPasswordUtil.generateTimestamp();

        assertNotNull(timestamp);
        assertEquals(14, timestamp.length());

        // Should be parseable as a LocalDateTime
        assertDoesNotThrow(() -> LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER));
    }

    @Test
    void generateTimestamp_WithDateTime_ShouldReturnCorrectFormat() {
        LocalDateTime testDateTime = LocalDateTime.of(2023, 12, 25, 14, 30, 45);
        String expected = "20231225143045";

        String actual = StkPushPasswordUtil.generateTimestamp(testDateTime);

        assertEquals(expected, actual);
    }

    @Test
    void generateTimestamp_WithNullDateTime_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StkPushPasswordUtil.generateTimestamp(null)
        );

        assertEquals("DateTime cannot be null", exception.getMessage());
    }

    @Test
    void generatePassword_WithCurrentTimestamp_ShouldReturnPasswordResult() {
        String businessShortCode = "174379";
        String passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";

        StkPushPasswordUtil.PasswordResult result = StkPushPasswordUtil.generatePassword(
            businessShortCode, passkey
        );

        assertNotNull(result);
        assertNotNull(result.getPassword());
        assertNotNull(result.getTimestamp());
        assertEquals(14, result.getTimestamp().length());

        // Verify timestamp format
        assertDoesNotThrow(() -> LocalDateTime.parse(result.getTimestamp(), TIMESTAMP_FORMATTER));

        // Verify password is Base64 encoded
        assertDoesNotThrow(() -> Base64.getDecoder().decode(result.getPassword()));
    }

    @Test
    void generatePassword_WithSpecificTimestamp_ShouldReturnCorrectPassword() {
        String businessShortCode = "174379";
        String passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
        String timestamp = "20231225143045";

        String password = StkPushPasswordUtil.generatePassword(businessShortCode, passkey, timestamp);

        // Expected: Base64(174379 + passkey + 20231225143045)
        String expectedPlaintext = businessShortCode + passkey + timestamp;
        String expectedPassword = Base64.getEncoder().encodeToString(expectedPlaintext.getBytes());

        assertEquals(expectedPassword, password);
    }

    @Test
    void generatePassword_WithNullBusinessShortCode_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StkPushPasswordUtil.generatePassword(null, "passkey", "20231225143045")
        );

        assertEquals("Business short code cannot be null or empty", exception.getMessage());
    }

    @Test
    void generatePassword_WithEmptyBusinessShortCode_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StkPushPasswordUtil.generatePassword("  ", "passkey", "20231225143045")
        );

        assertEquals("Business short code cannot be null or empty", exception.getMessage());
    }

    @Test
    void generatePassword_WithNullPasskey_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StkPushPasswordUtil.generatePassword("174379", null, "20231225143045")
        );

        assertEquals("Passkey cannot be null or empty", exception.getMessage());
    }

    @Test
    void generatePassword_WithEmptyPasskey_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StkPushPasswordUtil.generatePassword("174379", "", "20231225143045")
        );

        assertEquals("Passkey cannot be null or empty", exception.getMessage());
    }

    @Test
    void generatePassword_WithNullTimestamp_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StkPushPasswordUtil.generatePassword("174379", "passkey", null)
        );

        assertEquals("Timestamp cannot be null or empty", exception.getMessage());
    }

    @Test
    void generatePassword_WithInvalidTimestamp_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StkPushPasswordUtil.generatePassword("174379", "passkey", "invalid")
        );

        assertEquals("Timestamp must be in format yyyyMMddHHmmss", exception.getMessage());
    }

    @Test
    void generatePassword_WithShortTimestamp_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StkPushPasswordUtil.generatePassword("174379", "passkey", "202312251430")
        );

        assertEquals("Timestamp must be in format yyyyMMddHHmmss", exception.getMessage());
    }

    @Test
    void passwordResult_ShouldHaveCorrectToString() {
        String password = "testPassword";
        String timestamp = "20231225143045";

        StkPushPasswordUtil.PasswordResult result = new StkPushPasswordUtil.PasswordResult(
            password, timestamp
        );

        String toString = result.toString();
        assertTrue(toString.contains(password));
        assertTrue(toString.contains(timestamp));
        assertTrue(toString.contains("PasswordResult"));
    }

    @Test
    void passwordResult_ShouldReturnCorrectValues() {
        String password = "testPassword";
        String timestamp = "20231225143045";

        StkPushPasswordUtil.PasswordResult result = new StkPushPasswordUtil.PasswordResult(
            password, timestamp
        );

        assertEquals(password, result.getPassword());
        assertEquals(timestamp, result.getTimestamp());
    }
}