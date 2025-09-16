package io.github.wmnjuguna.integration;

import io.github.wmnjuguna.DarajaApiClient;
import io.github.wmnjuguna.DarajaClientFactory;
import io.github.wmnjuguna.DarajaEnvironment;
import io.github.wmnjuguna.reversal.ReversalRequest;
import io.github.wmnjuguna.reversal.ReversalResponse;
import io.github.wmnjuguna.accountbalance.AccountBalanceRequest;
import io.github.wmnjuguna.accountbalance.AccountBalanceResponse;
import io.github.wmnjuguna.transactionstatus.TransactionStatusRequest;
import io.github.wmnjuguna.transactionstatus.TransactionStatusResponse;
import io.github.wmnjuguna.b2b.B2BRequest;
import io.github.wmnjuguna.b2b.B2BResponse;
import io.github.wmnjuguna.b2c.B2CRequest;
import io.github.wmnjuguna.b2c.B2CResponse;
import io.github.wmnjuguna.c2b.C2BRegisterUrlRequest;
import io.github.wmnjuguna.c2b.C2BRegisterUrlResponse;
import io.github.wmnjuguna.stkpush.StkPushRequest;
import io.github.wmnjuguna.stkpush.StkPushResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DarajaApiClientIntegrationTest {

    private WireMockServer wireMockServer;
    private DarajaApiClient darajaApiClient;
    private DarajaClientFactory clientFactory;

    @BeforeEach
    void setUp() {
        // Configure WireMock with dynamic port allocation to avoid conflicts
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();

        // Configure WireMock client to use this server instance
        configureFor("localhost", wireMockServer.port());

        // Create client factory with WireMock base URL
        clientFactory = new DarajaClientFactory(
                wireMockServer.baseUrl(),
                "test_consumer_key",
                "test_consumer_secret"
        );

        // Create the API client - this will include authentication interceptor
        darajaApiClient = clientFactory.createApiClient();
    }

    @AfterEach
    void tearDown() {
        // Reset WireMock to clear any stubs and recorded requests
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.resetAll();
            wireMockServer.stop();
        }

        // Clean up client references
        darajaApiClient = null;
        clientFactory = null;
    }

    @Test
    void testInitiateStkPush() {
        // Stub the auth endpoint
        stubFor(get(urlEqualTo("/oauth/v1/generate?grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"test_token\",\"expires_in\":\"3600\"}")));

        // Stub the STK Push endpoint
        stubFor(post(urlEqualTo("/mpesa/stkpush/v1/processrequest"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"MerchantRequestID\":\"test_merchant_id\",\"CheckoutRequestID\":\"test_checkout_id\",\"ResponseCode\":\"0\",\"ResponseDescription\":\"Success\",\"CustomerMessage\":\"Success\"}")));

        // Create the request
        StkPushRequest request = StkPushRequest.builder()
                .businessShortCode("174379")
                .password("test_password")
                .timestamp("20250915100000")
                .amount(1)
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .accountReference("account")
                .transactionDesc("description")
                .build();

        // Make the call
        StkPushResponse response = darajaApiClient.initiateStkPush(request);

        // Verify the response
        assertEquals("test_merchant_id", response.merchantRequestID());
        assertEquals("test_checkout_id", response.checkoutRequestID());
        assertEquals("0", response.responseCode());
    }

    @Test
    void testRegisterC2BUrls() {
        // Stub the auth endpoint
        stubFor(get(urlEqualTo("/oauth/v1/generate?grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"test_token\",\"expires_in\":\"3600\"}")));

        // Stub the C2B URL registration endpoint
        stubFor(post(urlEqualTo("/mpesa/c2b/v1/registerurl"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"ConversationID\":\"test_conversation_id\",\"OriginatorConversationID\":\"test_originator_conversation_id\",\"ResponseDescription\":\"Success\"}")));

        // Create the request
        C2BRegisterUrlRequest request = new C2BRegisterUrlRequest(
                "600988",
                "Completed",
                "https://example.com/confirmation",
                "https://example.com/validation"
        );

        // Make the call
        C2BRegisterUrlResponse response = darajaApiClient.registerC2BUrls(request);

        // Verify the response
        assertEquals("test_conversation_id", response.conversationID());
        assertEquals("test_originator_conversation_id", response.originatorConversationID());
        assertEquals("Success", response.responseDescription());
    }

    @Test
    void testB2CPayment() {
        // Stub the auth endpoint
        stubFor(get(urlEqualTo("/oauth/v1/generate?grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"test_token\",\"expires_in\":\"3600\"}")));

        // Stub the B2C payment endpoint
        stubFor(post(urlEqualTo("/mpesa/b2c/v1/paymentrequest"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"ConversationID\":\"test_conversation_id\",\"OriginatorConversationID\":\"test_originator_conversation_id\",\"ResponseCode\":\"0\",\"ResponseDescription\":\"Success\"}")));

        // Create the request
        B2CRequest request = new B2CRequest(
                "testuser",
                "credential",
                "BusinessPayment",
                "100",
                "600988",
                "254708374149",
                "remarks",
                "https://example.com/timeout",
                "https://example.com/result",
                "occasion"
        );

        // Make the call
        B2CResponse response = darajaApiClient.b2cPayment(request);

        // Verify the response
        assertEquals("test_conversation_id", response.conversationID());
        assertEquals("test_originator_conversation_id", response.originatorConversationID());
        assertEquals("0", response.responseCode());
        assertEquals("Success", response.responseDescription());
    }

    @Test
    void testB2BPayment() {
        // Stub the auth endpoint
        stubFor(get(urlEqualTo("/oauth/v1/generate?grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"test_token\",\"expires_in\":\"3600\"}")));

        // Stub the B2B payment endpoint
        stubFor(post(urlEqualTo("/mpesa/b2b/v1/paymentrequest"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"ConversationID\":\"test_conversation_id\",\"OriginatorConversationID\":\"test_originator_conversation_id\",\"ResponseCode\":\"0\",\"ResponseDescription\":\"Success\"}")));

        // Create the request
        B2BRequest request = new B2BRequest(
                "testuser",
                "credential",
                "BusinessToBusinessTransfer",
                "4",
                "4",
                "100",
                "600988",
                "600988",
                "remarks",
                "https://example.com/timeout",
                "https://example.com/result",
                "reference"
        );

        // Make the call
        B2BResponse response = darajaApiClient.b2bPayment(request);

        // Verify the response
        assertEquals("test_conversation_id", response.conversationID());
        assertEquals("test_originator_conversation_id", response.originatorConversationID());
        assertEquals("0", response.responseCode());
        assertEquals("Success", response.responseDescription());
    }

    @Test
    void testTransactionStatus() {
        // Stub the auth endpoint
        stubFor(get(urlEqualTo("/oauth/v1/generate?grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"test_token\",\"expires_in\":\"3600\"}")));

        // Stub the transaction status endpoint
        stubFor(post(urlEqualTo("/mpesa/transactionstatus/v1/query"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"ConversationID\":\"test_conversation_id\",\"OriginatorConversationID\":\"test_originator_conversation_id\",\"ResponseCode\":\"0\",\"ResponseDescription\":\"Success\"}")));

        // Create the request
        TransactionStatusRequest request = new TransactionStatusRequest(
                "testuser",
                "credential",
                "TransactionStatusQuery",
                "ABCDEFGHI",
                "600988",
                "1",
                "https://example.com/result",
                "https://example.com/timeout",
                "remarks",
                "occasion"
        );

        // Make the call
        TransactionStatusResponse response = darajaApiClient.transactionStatus(request);

        // Verify the response
        assertEquals("test_conversation_id", response.conversationID());
        assertEquals("test_originator_conversation_id", response.originatorConversationID());
        assertEquals("0", response.responseCode());
        assertEquals("Success", response.responseDescription());
    }

    @Test
    void testAccountBalance() {
        // Stub the auth endpoint
        stubFor(get(urlEqualTo("/oauth/v1/generate?grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"test_token\",\"expires_in\":\"3600\"}")));

        // Stub the account balance endpoint
        stubFor(post(urlEqualTo("/mpesa/accountbalance/v1/query"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"ConversationID\":\"test_conversation_id\",\"OriginatorConversationID\":\"test_originator_conversation_id\",\"ResponseCode\":\"0\",\"ResponseDescription\":\"Success\"}")));

        // Create the request
        AccountBalanceRequest request = new AccountBalanceRequest(
                "testuser",
                "credential",
                "AccountBalance",
                "600988",
                "4",
                "remarks",
                "https://example.com/timeout",
                "https://example.com/result"
        );

        // Make the call
        AccountBalanceResponse response = darajaApiClient.accountBalance(request);

        // Verify the response
        assertEquals("test_conversation_id", response.conversationID());
        assertEquals("test_originator_conversation_id", response.originatorConversationID());
        assertEquals("0", response.responseCode());
        assertEquals("Success", response.responseDescription());
    }

    @Test
    void testReversal() {
        // Stub the auth endpoint
        stubFor(get(urlEqualTo("/oauth/v1/generate?grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"test_token\",\"expires_in\":\"3600\"}")));

        // Stub the reversal endpoint
        stubFor(post(urlEqualTo("/mpesa/reversal/v1/request"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"ConversationID\":\"test_conversation_id\",\"OriginatorConversationID\":\"test_originator_conversation_id\",\"ResponseCode\":\"0\",\"ResponseDescription\":\"Success\"}")));

        // Create the request
        ReversalRequest request = new ReversalRequest(
                "testuser",
                "credential",
                "TransactionReversal",
                "ABCDEFGHI",
                "100",
                "600988",
                "4",
                "https://example.com/result",
                "https://example.com/timeout",
                "remarks",
                "occasion"
        );

        // Make the call
        ReversalResponse response = darajaApiClient.reversal(request);

        // Verify the response
        assertEquals("test_conversation_id", response.conversationID());
        assertEquals("test_originator_conversation_id", response.originatorConversationID());
        assertEquals("0", response.responseCode());
        assertEquals("Success", response.responseDescription());
    }
}
