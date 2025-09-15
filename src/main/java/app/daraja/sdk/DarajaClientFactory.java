package app.daraja.sdk;

import app.daraja.sdk.auth.DarajaAuthClient;
import app.daraja.sdk.auth.DarajaAuthInterceptor;
import app.daraja.sdk.error.DarajaErrorDecoder;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

/**
 * Factory class for creating and configuring Daraja API clients.
 * Centralizes client construction and handles authentication setup.
 */
public class DarajaClientFactory {

    private final String baseUrl;
    private final String consumerKey;
    private final String consumerSecret;

    public DarajaClientFactory(DarajaEnvironment environment, String consumerKey, String consumerSecret) {
        this(environment.getBaseUrl(), consumerKey, consumerSecret);
    }

    public DarajaClientFactory(String baseUrl, String consumerKey, String consumerSecret) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Base URL cannot be null or empty");
        }
        if (consumerKey == null || consumerKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Consumer key cannot be null or empty");
        }
        if (consumerSecret == null || consumerSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("Consumer secret cannot be null or empty");
        }

        this.baseUrl = baseUrl.trim();
        this.consumerKey = consumerKey.trim();
        this.consumerSecret = consumerSecret.trim();
    }

    public DarajaApiClient createApiClient() {
        DarajaAuthClient authClient = createAuthClient();

        DarajaAuthInterceptor authInterceptor = new DarajaAuthInterceptor(
            authClient, consumerKey, consumerSecret
        );

        return Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .errorDecoder(new DarajaErrorDecoder())
            .logger(new Slf4jLogger(DarajaApiClient.class))
            .requestInterceptor(authInterceptor)
            .target(DarajaApiClient.class, baseUrl);
    }

    private DarajaAuthClient createAuthClient() {
        return Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .logger(new Slf4jLogger(DarajaAuthClient.class))
            .target(DarajaAuthClient.class, baseUrl);
    }
}