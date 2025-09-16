package io.github.wmnjuguna.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void constructor_ShouldSetAllFields() {
        String accessToken = "test_access_token";
        String expiresIn = "3599";

        AuthResponse response = new AuthResponse(accessToken, expiresIn);

        assertEquals(accessToken, response.accessToken());
        assertEquals(expiresIn, response.expiresIn());
    }

    @Test
    void getExpiresInAsInt_WithValidNumber_ShouldReturnInteger() {
        AuthResponse response = new AuthResponse("token", "3600");

        assertEquals(3600, response.getExpiresInAsInt());
    }

    @Test
    void getExpiresInAsInt_WithInvalidNumber_ShouldReturnMinusOne() {
        AuthResponse response = new AuthResponse("token", "invalid");

        assertEquals(-1, response.getExpiresInAsInt());
    }

    @Test
    void getExpiresInAsInt_WithNullExpiresIn_ShouldReturnMinusOne() {
        AuthResponse response = new AuthResponse("token", null);

        assertEquals(-1, response.getExpiresInAsInt());
    }

    @Test
    void getExpiresInAsInt_WithEmptyExpiresIn_ShouldReturnMinusOne() {
        AuthResponse response = new AuthResponse("token", "");

        assertEquals(-1, response.getExpiresInAsInt());
    }

    @Test
    void getExpirationTimestamp_WithValidExpiresIn_ShouldReturnFutureTimestamp() {
        long beforeCall = System.currentTimeMillis();
        AuthResponse response = new AuthResponse("token", "3600");
        long afterCall = System.currentTimeMillis();

        long expirationTimestamp = response.getExpirationTimestamp();

        // Should be approximately current time + 3600 seconds (3600000 milliseconds)
        long expectedMin = beforeCall + 3600000;
        long expectedMax = afterCall + 3600000;

        assertTrue(expirationTimestamp >= expectedMin);
        assertTrue(expirationTimestamp <= expectedMax);
    }

    @Test
    void getExpirationTimestamp_WithInvalidExpiresIn_ShouldReturnFutureTime() {
        long beforeCall = System.currentTimeMillis();
        AuthResponse response = new AuthResponse("token", "invalid");
        long afterCall = System.currentTimeMillis();

        long expirationTimestamp = response.getExpirationTimestamp();

        // Should be approximately current time + 3600 seconds (default fallback)
        long expectedMin = beforeCall + 3600000;
        long expectedMax = afterCall + 3600000;
        assertTrue(expirationTimestamp >= expectedMin);
        assertTrue(expirationTimestamp <= expectedMax);
    }

    @Test
    void getExpirationTimestamp_WithZeroExpiresIn_ShouldReturnCurrentTime() {
        long beforeCall = System.currentTimeMillis();
        AuthResponse response = new AuthResponse("token", "0");
        long afterCall = System.currentTimeMillis();

        long expirationTimestamp = response.getExpirationTimestamp();

        // Should be approximately current time
        assertTrue(expirationTimestamp >= beforeCall);
        assertTrue(expirationTimestamp <= afterCall);
    }

    @Test
    void getExpirationTimestamp_WithNegativeExpiresIn_ShouldReturnPastTime() {
        long beforeCall = System.currentTimeMillis();
        AuthResponse response = new AuthResponse("token", "-100");
        long afterCall = System.currentTimeMillis();

        long expirationTimestamp = response.getExpirationTimestamp();

        // Should be approximately 100 seconds before current time
        long expectedMin = beforeCall - 100000;
        long expectedMax = afterCall - 100000;
        assertTrue(expirationTimestamp >= expectedMin);
        assertTrue(expirationTimestamp <= expectedMax);
    }

    @Test
    void isExpired_WithFutureTimestamp_ShouldReturnFalse() {
        AuthResponse response = new AuthResponse("token", "3600");

        assertFalse(response.isExpired());
    }

    @Test
    void isExpired_WithPastTimestamp_ShouldReturnTrue() {
        // Create a response that expires immediately
        AuthResponse response = new AuthResponse("token", "0");

        // Wait a tiny bit to ensure it's expired
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(response.isExpired());
    }

    @Test
    void isExpired_WithInvalidExpiresIn_ShouldReturnFalse() {
        AuthResponse response = new AuthResponse("token", "invalid");

        // Since invalid input defaults to 3600 seconds, it should not be expired
        assertFalse(response.isExpired());
    }

    @Test
    void isExpiring_WithinBuffer_ShouldReturnTrue() {
        // Create response that expires in 30 seconds (within the 60-second buffer)
        AuthResponse response = new AuthResponse("token", "30");

        assertTrue(response.isExpiring());
    }

    @Test
    void isExpiring_OutsideBuffer_ShouldReturnFalse() {
        // Create response that expires in 120 seconds (outside the 60-second buffer)
        AuthResponse response = new AuthResponse("token", "120");

        assertFalse(response.isExpiring());
    }

    @Test
    void isExpiring_WithInvalidExpiresIn_ShouldReturnFalse() {
        AuthResponse response = new AuthResponse("token", "invalid");

        // Since invalid input defaults to 3600 seconds, it should not be expiring
        assertFalse(response.isExpiring());
    }

    @Test
    void hasValidToken_WithNonEmptyToken_ShouldReturnTrue() {
        AuthResponse response = new AuthResponse("valid_token", "3600");

        assertTrue(response.hasValidToken());
    }

    @Test
    void hasValidToken_WithNullToken_ShouldReturnFalse() {
        AuthResponse response = new AuthResponse(null, "3600");

        assertFalse(response.hasValidToken());
    }

    @Test
    void hasValidToken_WithEmptyToken_ShouldReturnFalse() {
        AuthResponse response = new AuthResponse("", "3600");

        assertFalse(response.hasValidToken());
    }

    @Test
    void hasValidToken_WithWhitespaceToken_ShouldReturnFalse() {
        AuthResponse response = new AuthResponse("   ", "3600");

        assertFalse(response.hasValidToken());
    }

    @Test
    void multipleMethodCalls_ShouldBeConsistent() {
        AuthResponse response = new AuthResponse("test_token", "3600");

        // Call methods multiple times to ensure consistency
        assertEquals(3600, response.getExpiresInAsInt());
        assertEquals(3600, response.getExpiresInAsInt());

        assertTrue(response.hasValidToken());
        assertTrue(response.hasValidToken());

        assertFalse(response.isExpired());
        assertFalse(response.isExpired());
    }
}