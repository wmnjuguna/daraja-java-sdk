package app.daraja.sdk.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Utility class for validating callback URLs used in Daraja API requests.
 * Ensures URLs meet Safaricom's requirements and security standards.
 */
public final class CallbackUrlValidator {

    private static final Pattern LOCALHOST_PATTERN = Pattern.compile(
        "^https?://(localhost|127\\.0\\.0\\.1|0\\.0\\.0\\.0)(:[0-9]+)?.*"
    );

    private static final Pattern PRIVATE_IP_PATTERN = Pattern.compile(
        "^https?://((10\\.)|(172\\.(1[6-9]|2[0-9]|3[01])\\.)|(192\\.168\\.)).*"
    );

    private CallbackUrlValidator() {
        // Utility class - prevent instantiation
    }

    /**
     * Validates a callback URL for use with Daraja API.
     * Safaricom servers must be able to reach the URL, so localhost and private IPs are not allowed.
     *
     * @param url the URL to validate
     * @return ValidationResult containing validation status and details
     */
    public static ValidationResult validate(String url) {
        if (url == null || url.trim().isEmpty()) {
            return ValidationResult.invalid("Callback URL cannot be null or empty");
        }

        String trimmedUrl = url.trim();

        // Check basic URL format
        try {
            URL parsedUrl = new URL(trimmedUrl);

            // Must use HTTP or HTTPS
            String protocol = parsedUrl.getProtocol().toLowerCase();
            if (!protocol.equals("http") && !protocol.equals("https")) {
                return ValidationResult.invalid("URL must use HTTP or HTTPS protocol");
            }

            // Localhost is never allowed - Safaricom servers cannot reach it
            if (isLocalhost(trimmedUrl)) {
                return ValidationResult.invalid("Localhost URLs are not allowed - Safaricom servers cannot reach localhost");
            }

            // Private IPs are not allowed - Safaricom servers cannot reach them
            if (isPrivateIP(trimmedUrl)) {
                return ValidationResult.invalid("Private IP addresses are not allowed - Safaricom servers cannot reach private networks");
            }

            // HTTPS is strongly recommended for security
            if (protocol.equals("http")) {
                return ValidationResult.warning("HTTPS is strongly recommended for security");
            }

            // Should have a specific callback path
            String path = parsedUrl.getPath();
            if (path == null || path.isEmpty() || path.equals("/")) {
                return ValidationResult.warning("URL should include a specific callback path (e.g., /daraja/callback)");
            }

            return ValidationResult.valid("URL is valid for Daraja callbacks");

        } catch (MalformedURLException e) {
            return ValidationResult.invalid("Invalid URL format: " + e.getMessage());
        }
    }

    /**
     * Validates a callback URL with strict production requirements.
     * Enforces HTTPS and proper URL structure.
     *
     * @param url the URL to validate
     * @return ValidationResult with strict validation
     */
    public static ValidationResult validateForProduction(String url) {
        ValidationResult basicValidation = validate(url);

        if (!basicValidation.isValid()) {
            return basicValidation;
        }

        try {
            URL parsedUrl = new URL(url.trim());

            // Require HTTPS for production
            if (!parsedUrl.getProtocol().equalsIgnoreCase("https")) {
                return ValidationResult.invalid("Production URLs must use HTTPS");
            }

            // Must have a meaningful path
            String path = parsedUrl.getPath();
            if (path == null || path.isEmpty() || path.equals("/")) {
                return ValidationResult.invalid("Production URLs must include a specific callback path");
            }

            // Check for common testing domains that shouldn't be used in production
            String host = parsedUrl.getHost().toLowerCase();
            if (host.contains("test") || host.contains("dev") || host.contains("staging")) {
                return ValidationResult.warning("URL appears to be a testing/development domain");
            }

            return ValidationResult.valid("URL meets production requirements");

        } catch (MalformedURLException e) {
            return ValidationResult.invalid("Invalid URL format: " + e.getMessage());
        }
    }

    /**
     * Checks if URL points to localhost.
     */
    private static boolean isLocalhost(String url) {
        return LOCALHOST_PATTERN.matcher(url.toLowerCase()).matches();
    }

    /**
     * Checks if URL points to a private IP address.
     */
    private static boolean isPrivateIP(String url) {
        return PRIVATE_IP_PATTERN.matcher(url.toLowerCase()).matches();
    }

    /**
     * Result of URL validation containing status and message.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final boolean warning;
        private final String message;

        private ValidationResult(boolean valid, boolean warning, String message) {
            this.valid = valid;
            this.warning = warning;
            this.message = message;
        }

        public static ValidationResult valid(String message) {
            return new ValidationResult(true, false, message);
        }

        public static ValidationResult warning(String message) {
            return new ValidationResult(true, true, message);
        }

        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, false, message);
        }

        /**
         * @return true if URL is valid for use
         */
        public boolean isValid() {
            return valid;
        }

        /**
         * @return true if URL is valid but has warnings
         */
        public boolean isWarning() {
            return warning;
        }

        /**
         * @return validation message
         */
        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            String status = valid ? (warning ? "WARNING" : "VALID") : "INVALID";
            return String.format("[%s] %s", status, message);
        }
    }
}