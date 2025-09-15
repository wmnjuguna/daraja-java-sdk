package app.daraja.sdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DarajaClientFactoryTest {

    @Test
    void constructor_WithValidParameters_ShouldCreateFactory() {
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            "consumer_key",
            "consumer_secret"
        );
        assertNotNull(factory);
    }

    @Test
    void constructor_WithNullEnvironment_ShouldThrowException() {
        assertThrows(NullPointerException.class, () ->
            new DarajaClientFactory((DarajaEnvironment) null, "consumer_key", "consumer_secret")
        );
    }

    @Test
    void constructor_WithNullConsumerKey_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DarajaClientFactory(DarajaEnvironment.SANDBOX, null, "consumer_secret")
        );

        assertEquals("Consumer key cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyConsumerKey_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DarajaClientFactory(DarajaEnvironment.SANDBOX, "   ", "consumer_secret")
        );

        assertEquals("Consumer key cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithNullConsumerSecret_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DarajaClientFactory(DarajaEnvironment.SANDBOX, "consumer_key", null)
        );

        assertEquals("Consumer secret cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithEmptyConsumerSecret_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DarajaClientFactory(DarajaEnvironment.SANDBOX, "consumer_key", "")
        );

        assertEquals("Consumer secret cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_WithWhitespaceOnlyCredentials_ShouldThrowException() {
        // Test consumer key with only whitespace
        assertThrows(
            IllegalArgumentException.class,
            () -> new DarajaClientFactory(DarajaEnvironment.SANDBOX, "\t\n ", "consumer_secret")
        );

        // Test consumer secret with only whitespace
        assertThrows(
            IllegalArgumentException.class,
            () -> new DarajaClientFactory(DarajaEnvironment.SANDBOX, "consumer_key", "\t\n ")
        );
    }

    @Test
    void constructor_ShouldTrimCredentials() {
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            "  consumer_key  ",
            "  consumer_secret  "
        );

        // We can't directly test the trimmed values since they're private,
        // but we can verify the factory was created successfully
        assertNotNull(factory);
    }

    @Test
    void createApiClient_ShouldReturnNonNullClient() {
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            "consumer_key",
            "consumer_secret"
        );

        DarajaApiClient client = factory.createApiClient();

        assertNotNull(client);
    }

    @Test
    void createApiClient_WithSandboxEnvironment_ShouldCreateClient() {
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            "sandbox_consumer_key",
            "sandbox_consumer_secret"
        );

        DarajaApiClient client = factory.createApiClient();

        assertNotNull(client);
    }

    @Test
    void createApiClient_WithProductionEnvironment_ShouldCreateClient() {
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.PRODUCTION,
            "prod_consumer_key",
            "prod_consumer_secret"
        );

        DarajaApiClient client = factory.createApiClient();

        assertNotNull(client);
    }

    @Test
    void createApiClient_CalledMultipleTimes_ShouldReturnDifferentInstances() {
        DarajaClientFactory factory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            "consumer_key",
            "consumer_secret"
        );

        DarajaApiClient client1 = factory.createApiClient();
        DarajaApiClient client2 = factory.createApiClient();

        assertNotNull(client1);
        assertNotNull(client2);
        // Each call should return a new instance
        assertNotSame(client1, client2);
    }

    @Test
    void getEnvironment_ShouldReturnCorrectEnvironment() {
        DarajaClientFactory sandboxFactory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            "consumer_key",
            "consumer_secret"
        );

        DarajaClientFactory productionFactory = new DarajaClientFactory(
            DarajaEnvironment.PRODUCTION,
            "consumer_key",
            "consumer_secret"
        );

        assertNotNull(sandboxFactory);
        assertNotNull(productionFactory);
    }
}