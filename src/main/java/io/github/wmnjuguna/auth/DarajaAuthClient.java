package io.github.wmnjuguna.auth;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * Internal Feign client for Daraja OAuth2 authentication.
 * This interface is used internally by the SDK and should not be used directly.
 */
public interface DarajaAuthClient {

    /**
     * Generates an OAuth2 access token using client credentials.
     *
     * @param authorizationHeader Base64 encoded "consumerKey:consumerSecret"
     * @return AuthResponse containing the access token and expiration
     */
    @RequestLine("GET /oauth/v1/generate?grant_type=client_credentials")
    @Headers({
        "Authorization: Basic {authorizationHeader}",
        "Content-Type: application/json"
    })
    AuthResponse generateAccessToken(@Param("authorizationHeader") String authorizationHeader);
}