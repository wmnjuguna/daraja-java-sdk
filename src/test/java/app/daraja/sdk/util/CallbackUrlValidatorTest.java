package app.daraja.sdk.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallbackUrlValidatorTest {

    @Test
    void validate_WithValidHttpsUrl_ShouldReturnValid() {
        String url = "https://api.example.com/daraja/callback";

        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate(url);

        assertTrue(result.isValid());
        assertFalse(result.isWarning());
    }

    @Test
    void validate_WithValidHttpUrl_ShouldReturnWarning() {
        String url = "http://api.example.com/daraja/callback";

        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate(url);

        assertTrue(result.isValid());
        assertTrue(result.isWarning());
        assertTrue(result.getMessage().contains("HTTPS is strongly recommended"));
    }

    @Test
    void validate_WithLocalhostUrl_ShouldReturnInvalid() {
        String[] localhostUrls = {
            "http://localhost:8080/callback",
            "https://localhost/callback",
            "http://127.0.0.1:3000/callback",
            "https://0.0.0.0:8080/callback"
        };

        for (String url : localhostUrls) {
            CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate(url);

            assertFalse(result.isValid(), "Should reject localhost URL: " + url);
            assertTrue(result.getMessage().contains("localhost"));
        }
    }

    @Test
    void validate_WithPrivateIpUrl_ShouldReturnInvalid() {
        String[] privateIpUrls = {
            "http://10.0.0.1/callback",
            "https://172.16.0.1/callback",
            "http://192.168.1.100/callback",
            "https://172.31.255.255/callback"
        };

        for (String url : privateIpUrls) {
            CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate(url);

            assertFalse(result.isValid(), "Should reject private IP URL: " + url);
            assertTrue(result.getMessage().contains("private"));
        }
    }

    @Test
    void validate_WithInvalidProtocol_ShouldReturnInvalid() {
        String url = "ftp://example.com/callback";

        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate(url);

        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("HTTP or HTTPS"));
    }

    @Test
    void validate_WithMalformedUrl_ShouldReturnInvalid() {
        String url = "not-a-valid-url";

        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate(url);

        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("Invalid URL format"));
    }

    @Test
    void validate_WithNullUrl_ShouldReturnInvalid() {
        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate(null);

        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("cannot be null"));
    }

    @Test
    void validate_WithEmptyUrl_ShouldReturnInvalid() {
        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate("");

        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("cannot be null or empty"));
    }

    @Test
    void validate_WithRootPath_ShouldReturnWarning() {
        String url = "https://api.example.com/";

        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validate(url);

        assertTrue(result.isValid());
        assertTrue(result.isWarning());
        assertTrue(result.getMessage().contains("specific callback path"));
    }

    @Test
    void validateForProduction_WithHttpsUrl_ShouldReturnValid() {
        String url = "https://api.example.com/daraja/callback";

        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validateForProduction(url);

        assertTrue(result.isValid());
        assertFalse(result.isWarning());
    }

    @Test
    void validateForProduction_WithHttpUrl_ShouldReturnInvalid() {
        String url = "http://api.example.com/daraja/callback";

        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validateForProduction(url);

        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("must use HTTPS"));
    }

    @Test
    void validateForProduction_WithRootPath_ShouldReturnInvalid() {
        String url = "https://api.example.com/";

        CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validateForProduction(url);

        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("specific callback path"));
    }

    @Test
    void validateForProduction_WithTestDomain_ShouldReturnWarning() {
        String[] testDomains = {
            "https://test.example.com/callback",
            "https://dev.example.com/callback",
            "https://staging.example.com/callback"
        };

        for (String url : testDomains) {
            CallbackUrlValidator.ValidationResult result = CallbackUrlValidator.validateForProduction(url);

            assertTrue(result.isValid(), "Should be valid but with warning: " + url);
            assertTrue(result.isWarning(), "Should have warning for test domain: " + url);
        }
    }

    @Test
    void validationResult_ToString_ShouldIncludeStatus() {
        CallbackUrlValidator.ValidationResult valid = CallbackUrlValidator.ValidationResult.valid("All good");
        CallbackUrlValidator.ValidationResult warning = CallbackUrlValidator.ValidationResult.warning("Be careful");
        CallbackUrlValidator.ValidationResult invalid = CallbackUrlValidator.ValidationResult.invalid("Bad URL");

        assertTrue(valid.toString().contains("VALID"));
        assertTrue(warning.toString().contains("WARNING"));
        assertTrue(invalid.toString().contains("INVALID"));

        assertTrue(valid.toString().contains("All good"));
        assertTrue(warning.toString().contains("Be careful"));
        assertTrue(invalid.toString().contains("Bad URL"));
    }
}