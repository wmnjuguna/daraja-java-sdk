package io.github.wmnjuguna.integration;

import io.github.wmnjuguna.DarajaApiClient;
import io.github.wmnjuguna.DarajaClientFactory;
import io.github.wmnjuguna.DarajaEnvironment;
import io.github.wmnjuguna.stkpush.StkPushRequest;
import io.github.wmnjuguna.stkpush.StkPushResponse;
import io.github.wmnjuguna.util.StkPushPasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test demonstrating how to use the Daraja SDK.
 * These tests don't make actual API calls but demonstrate the SDK usage patterns.
 */
class DarajaSdkIntegrationTest {

    private DarajaClientFactory clientFactory;
    private DarajaApiClient apiClient;

    @BeforeEach
    void setUp() {
        // Initialize the client factory with sandbox environment
        clientFactory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            "test_consumer_key",
            "test_consumer_secret"
        );

        // Create the API client
        apiClient = clientFactory.createApiClient();
    }

    @Test
    void stkPushWorkflow_ShouldDemonstrateCompleteUsage() {
        // Step 1: Generate password and timestamp
        String businessShortCode = "174379";
        String passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";

        StkPushPasswordUtil.PasswordResult passwordResult = StkPushPasswordUtil.generatePassword(
            businessShortCode, passkey
        );

        assertNotNull(passwordResult.getPassword());
        assertNotNull(passwordResult.getTimestamp());

        // Step 2: Build the STK Push request
        StkPushRequest request = StkPushRequest.builder()
            .businessShortCode(businessShortCode)
            .password(passwordResult.getPassword())
            .timestamp(passwordResult.getTimestamp())
            .amount(1000L)
            .phoneNumber("254708374149")
            .callBackURL("https://yourdomain.com/callback")
            .accountReference("TestAccount123")
            .transactionDesc("Payment for Test Service")
            .build();

        // Verify request structure
        assertEquals(businessShortCode, request.businessShortCode());
        assertEquals("254708374149", request.phoneNumber());
        assertEquals("254708374149", request.partyA());
        assertEquals(businessShortCode, request.partyB());
        assertEquals("1000", request.amount());
        assertEquals("CustomerPayBillOnline", request.transactionType());

        // Step 3: The client is ready for API calls
        // In a real scenario, you would call:
        // StkPushResponse response = apiClient.initiateStkPush(request);

        // For this test, we just verify the client and request are properly constructed
        assertNotNull(apiClient);
        assertNotNull(request);
    }

    @Test
    void alternativePhoneNumberFormats_ShouldBeNormalized() {
        String businessShortCode = "174379";
        String passkey = "test_passkey";
        String timestamp = "20231225143045";

        // Test different phone number formats
        String[] phoneFormats = {
            "+254708374149",
            "254708374149",
            "0708374149",
            "708374149"
        };

        for (String phoneNumber : phoneFormats) {
            StkPushRequest request = StkPushRequest.builder()
                .businessShortCode(businessShortCode)
                .password(StkPushPasswordUtil.generatePassword(businessShortCode, passkey, timestamp))
                .timestamp(timestamp)
                .amount(500L)
                .phoneNumber(phoneNumber)
                .callBackURL("https://callback.url")
                .accountReference("TestRef")
                .transactionDesc("Test Transaction")
                .build();

            // All formats should be normalized to 254708374149
            assertEquals("254708374149", request.phoneNumber());
            assertEquals("254708374149", request.partyA());
        }
    }

    @Test
    void multipleEnvironments_ShouldCreateDifferentClients() {
        // Create sandbox client factory
        DarajaClientFactory sandboxFactory = new DarajaClientFactory(
            DarajaEnvironment.SANDBOX,
            "sandbox_key",
            "sandbox_secret"
        );

        // Create production client factory
        DarajaClientFactory productionFactory = new DarajaClientFactory(
            DarajaEnvironment.PRODUCTION,
            "prod_key",
            "prod_secret"
        );

        // Create clients
        DarajaApiClient sandboxClient = sandboxFactory.createApiClient();
        DarajaApiClient productionClient = productionFactory.createApiClient();

        // Verify both clients are created and different
        assertNotNull(sandboxClient);
        assertNotNull(productionClient);
        assertNotSame(sandboxClient, productionClient);

        // Verify factory environments

    }

    @Test
    void passwordGeneration_ShouldBeConsistent() {
        String businessShortCode = "174379";
        String passkey = "test_passkey";
        String timestamp = "20231225143045";

        // Generate password multiple times with same inputs
        String password1 = StkPushPasswordUtil.generatePassword(businessShortCode, passkey, timestamp);
        String password2 = StkPushPasswordUtil.generatePassword(businessShortCode, passkey, timestamp);

        // Should be identical
        assertEquals(password1, password2);

        // Generate with different timestamp
        String password3 = StkPushPasswordUtil.generatePassword(businessShortCode, passkey, "20231225143046");

        // Should be different
        assertNotEquals(password1, password3);
    }

    @Test
    void requestValidation_ShouldEnforceRequiredFields() {
        // Test missing business short code
        assertThrows(IllegalStateException.class, () ->
            StkPushRequest.builder()
                .password("password")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://callback.url")
                .accountReference("TestRef")
                .transactionDesc("Test Transaction")
                .build()
        );

        // Test missing password
        assertThrows(IllegalStateException.class, () ->
            StkPushRequest.builder()
                .businessShortCode("174379")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://callback.url")
                .accountReference("TestRef")
                .transactionDesc("Test Transaction")
                .build()
        );

        // Test missing amount
        assertThrows(IllegalStateException.class, () ->
            StkPushRequest.builder()
                .businessShortCode("174379")
                .password("password")
                .timestamp("20231225143045")
                .phoneNumber("254708374149")
                .callBackURL("https://callback.url")
                .accountReference("TestRef")
                .transactionDesc("Test Transaction")
                .build()
        );
    }

    @Test
    void factoryValidation_ShouldEnforceRequiredCredentials() {
        // Test null environment
        assertThrows(NullPointerException.class, () ->
            new DarajaClientFactory((DarajaEnvironment) null, "key", "secret")
        );

        // Test null consumer key
        assertThrows(IllegalArgumentException.class, () ->
            new DarajaClientFactory(DarajaEnvironment.SANDBOX, null, "secret")
        );

        // Test null consumer secret
        assertThrows(IllegalArgumentException.class, () ->
            new DarajaClientFactory(DarajaEnvironment.SANDBOX, "key", null)
        );

        // Test empty consumer key
        assertThrows(IllegalArgumentException.class, () ->
            new DarajaClientFactory(DarajaEnvironment.SANDBOX, "", "secret")
        );

        // Test empty consumer secret
        assertThrows(IllegalArgumentException.class, () ->
            new DarajaClientFactory(DarajaEnvironment.SANDBOX, "key", "")
        );
    }
}