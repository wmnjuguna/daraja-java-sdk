package io.github.wmnjuguna.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Utility class for generating STK Push passwords and timestamps.
 * The password is a Base64 encoded string of: BusinessShortCode + Passkey + Timestamp
 */
public final class StkPushPasswordUtil {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private StkPushPasswordUtil() {
        // Utility class - prevent instantiation
    }

    /**
     * Generates a timestamp in the required format for STK Push requests.
     * Format: yyyyMMddHHmmss
     *
     * @return the current timestamp as a formatted string
     */
    public static String generateTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    /**
     * Generates a timestamp from the specified LocalDateTime.
     *
     * @param dateTime the date and time to format
     * @return the timestamp as a formatted string
     */
    public static String generateTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime cannot be null");
        }
        return dateTime.format(TIMESTAMP_FORMATTER);
    }

    /**
     * Generates the STK Push password using current timestamp.
     * Password = Base64(BusinessShortCode + Passkey + Timestamp)
     *
     * @param businessShortCode the business short code
     * @param passkey          the passkey from Daraja portal
     * @return a PasswordResult containing both password and timestamp
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    public static PasswordResult generatePassword(String businessShortCode, String passkey) {
        String timestamp = generateTimestamp();
        String password = generatePassword(businessShortCode, passkey, timestamp);
        return new PasswordResult(password, timestamp);
    }

    /**
     * Generates the STK Push password using the specified timestamp.
     * Password = Base64(BusinessShortCode + Passkey + Timestamp)
     *
     * @param businessShortCode the business short code
     * @param passkey          the passkey from Daraja portal
     * @param timestamp        the timestamp to use (format: yyyyMMddHHmmss)
     * @return the Base64 encoded password
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    public static String generatePassword(String businessShortCode, String passkey, String timestamp) {
        validateParameter(businessShortCode, "Business short code");
        validateParameter(passkey, "Passkey");
        validateParameter(timestamp, "Timestamp");

        if (!isValidTimestamp(timestamp)) {
            throw new IllegalArgumentException("Timestamp must be in format yyyyMMddHHmmss");
        }

        String concatenated = businessShortCode + passkey + timestamp;
        return Base64.getEncoder().encodeToString(concatenated.getBytes());
    }

    /**
     * Validates that a parameter is not null or empty.
     *
     * @param parameter the parameter to validate
     * @param name      the name of the parameter for error messages
     * @throws IllegalArgumentException if parameter is null or empty
     */
    private static void validateParameter(String parameter, String name) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
    }

    /**
     * Validates that a timestamp string matches the expected format.
     *
     * @param timestamp the timestamp to validate
     * @return true if valid, false otherwise
     */
    private static boolean isValidTimestamp(String timestamp) {
        if (timestamp == null || timestamp.length() != 14) {
            return false;
        }

        try {
            LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Result class containing both the generated password and timestamp.
     * This is useful when you need both values and want to ensure they match.
     */
    public static class PasswordResult {
        private final String password;
        private final String timestamp;

        /**
         * Creates a new PasswordResult.
         *
         * @param password  the generated password
         * @param timestamp the timestamp used for password generation
         */
        public PasswordResult(String password, String timestamp) {
            this.password = password;
            this.timestamp = timestamp;
        }

        /**
         * Gets the generated password.
         *
         * @return the Base64 encoded password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Gets the timestamp used for password generation.
         *
         * @return the timestamp in yyyyMMddHHmmss format
         */
        public String getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return "PasswordResult{" +
                   "password='" + password + '\'' +
                   ", timestamp='" + timestamp + '\'' +
                   '}';
        }
    }
}