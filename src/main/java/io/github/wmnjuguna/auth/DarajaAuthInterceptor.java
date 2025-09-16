package io.github.wmnjuguna.auth;

import io.github.wmnjuguna.exception.DarajaAuthenticationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Feign request interceptor that automatically handles Daraja API authentication.
 * Manages OAuth2 token lifecycle including caching, refresh, and Base64 encoding of credentials.
 */
public class DarajaAuthInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DarajaAuthInterceptor.class);

    private final DarajaAuthClient authClient;
    private final String encodedCredentials;
    private final ReentrantLock tokenLock = new ReentrantLock();

    private volatile String cachedAccessToken;
    private volatile long tokenExpirationTime;

    /**
     * Creates a new DarajaAuthInterceptor.
     *
     * @param authClient      the Feign client for authentication requests
     * @param consumerKey     the Daraja API consumer key
     * @param consumerSecret  the Daraja API consumer secret
     */
    public DarajaAuthInterceptor(DarajaAuthClient authClient, String consumerKey, String consumerSecret) {
        this.authClient = authClient;
        this.encodedCredentials = Base64.getEncoder().encodeToString(
            (consumerKey + ":" + consumerSecret).getBytes()
        );
    }

    @Override
    public void apply(RequestTemplate template) {
        // Skip authentication for auth requests to prevent infinite loops
        if (template.url().contains("/oauth/v1/generate")) {
            return;
        }

        String accessToken = getValidAccessToken();
        template.header("Authorization", "Bearer " + accessToken);
    }

    /**
     * Gets a valid access token, refreshing if necessary.
     *
     * @return a valid access token
     * @throws RuntimeException if token acquisition fails
     */
    private String getValidAccessToken() {
        // Quick check without locking
        if (isTokenValid()) {
            return cachedAccessToken;
        }

        // Acquire lock for token refresh
        tokenLock.lock();
        try {
            // Double-check pattern - another thread might have refreshed the token
            if (isTokenValid()) {
                return cachedAccessToken;
            }

            logger.debug("Refreshing Daraja access token");
            AuthResponse authResponse = authClient.generateAccessToken(encodedCredentials);

            if (authResponse == null || authResponse.accessToken() == null || authResponse.accessToken().isEmpty()) {
                throw new DarajaAuthenticationException("Failed to obtain access token from Daraja API");
            }

            cachedAccessToken = authResponse.accessToken();
            tokenExpirationTime = authResponse.getExpirationTimestamp();

            logger.debug("Successfully refreshed Daraja access token, expires at: {}", tokenExpirationTime);
            return cachedAccessToken;

        } catch (DarajaAuthenticationException e) {
            // Re-throw authentication exceptions as-is
            throw e;
        } catch (Exception e) {
            logger.error("Failed to refresh Daraja access token", e);
            throw new DarajaAuthenticationException("Authentication failed", e);
        } finally {
            tokenLock.unlock();
        }
    }

    /**
     * Checks if the current cached token is valid and not expired.
     *
     * @return true if token is valid, false otherwise
     */
    private boolean isTokenValid() {
        return cachedAccessToken != null
            && !cachedAccessToken.isEmpty()
            && System.currentTimeMillis() < (tokenExpirationTime - 60000); // 1 minute buffer
    }

    /**
     * Clears the cached token, forcing a refresh on next use.
     * Useful for testing or when credentials change.
     */
    public void clearCache() {
        tokenLock.lock();
        try {
            cachedAccessToken = null;
            tokenExpirationTime = 0;
            logger.debug("Cleared cached Daraja access token");
        } finally {
            tokenLock.unlock();
        }
    }
}