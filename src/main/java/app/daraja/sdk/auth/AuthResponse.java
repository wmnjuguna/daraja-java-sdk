package app.daraja.sdk.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for Daraja OAuth2 authentication.
 * Maps the JSON response from the /oauth/v1/generate endpoint.
 *
 * @param accessToken the OAuth2 access token
 * @param expiresIn   the token expiration time in seconds
 */
public record AuthResponse(
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("expires_in")
    String expiresIn
) {

    /**
     * Gets the token expiration as an integer value in seconds.
     *
     * @return expiration time in seconds, or 3600 as default if parsing fails
     */
    public int getExpiresInSeconds() {
        try {
            return Integer.parseInt(expiresIn);
        } catch (NumberFormatException e) {
            // Default to 1 hour if parsing fails
            return 3600;
        }
    }

    /**
     * Calculates the absolute expiration time in milliseconds from now.
     *
     * @return the timestamp when this token expires
     */
    public long getExpirationTimestamp() {
        return System.currentTimeMillis() + (getExpiresInSeconds() * 1000L);
    }

    /**
     * Gets the token expiration as an integer value in seconds.
     * Alias for getExpiresInSeconds() for compatibility.
     *
     * @return expiration time in seconds, or -1 if parsing fails
     */
    public int getExpiresInAsInt() {
        try {
            return Integer.parseInt(expiresIn);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Checks if the token is currently expired.
     *
     * @return true if the token is expired, false otherwise
     */
    public boolean isExpired() {
        return System.currentTimeMillis() >= getExpirationTimestamp();
    }

    /**
     * Checks if the token is expiring soon (within 60 seconds).
     *
     * @return true if the token expires within 60 seconds, false otherwise
     */
    public boolean isExpiring() {
        long bufferTime = 60000; // 60 seconds in milliseconds
        return System.currentTimeMillis() >= (getExpirationTimestamp() - bufferTime);
    }

    /**
     * Checks if the token value is valid (not null or empty).
     *
     * @return true if the token is valid, false otherwise
     */
    public boolean hasValidToken() {
        return accessToken != null && !accessToken.trim().isEmpty();
    }
}