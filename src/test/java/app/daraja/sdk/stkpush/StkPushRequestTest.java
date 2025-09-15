package app.daraja.sdk.stkpush;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StkPushRequestTest {

    @Test
    void builder_ShouldCreateValidRequest() {
        StkPushRequest request = StkPushRequest.builder()
            .businessShortCode("174379")
            .password("MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjMxMjI1MTQzMDQ1")
            .timestamp("20231225143045")
            .amount(1000L)
            .phoneNumber("254708374149")
            .callBackURL("https://example.com/callback")
            .accountReference("TestAccount")
            .transactionDesc("Test Payment")
            .build();

        assertEquals("174379", request.businessShortCode());
        assertEquals("MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjMxMjI1MTQzMDQ1", request.password());
        assertEquals("20231225143045", request.timestamp());
        assertEquals("CustomerPayBillOnline", request.transactionType());
        assertEquals("1000", request.amount());
        assertEquals("254708374149", request.partyA());
        assertEquals("174379", request.partyB());
        assertEquals("254708374149", request.phoneNumber());
        assertEquals("https://example.com/callback", request.callBackURL());
        assertEquals("TestAccount", request.accountReference());
        assertEquals("Test Payment", request.transactionDesc());
    }

    @Test
    void builder_WithStringAmount_ShouldWork() {
        StkPushRequest request = StkPushRequest.builder()
            .businessShortCode("174379")
            .password("password")
            .timestamp("20231225143045")
            .amount("1500")
            .phoneNumber("254708374149")
            .callBackURL("https://example.com/callback")
            .accountReference("TestAccount")
            .transactionDesc("Test Payment")
            .build();

        assertEquals("1500", request.amount());
    }

    @Test
    void builder_ShouldNormalizePhoneNumbers() {
        // Test various phone number formats
        String[] phoneNumbers = {
            "+254708374149",
            "254708374149",
            "0708374149",
            "708374149"
        };

        for (String phoneNumber : phoneNumbers) {
            StkPushRequest request = StkPushRequest.builder()
                .businessShortCode("174379")
                .password("password")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber(phoneNumber)
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build();

            assertEquals("254708374149", request.phoneNumber());
            assertEquals("254708374149", request.partyA());
        }
    }

    @Test
    void builder_WithMissingBusinessShortCode_ShouldThrowException() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> StkPushRequest.builder()
                .password("password")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build()
        );

        assertEquals("Business short code is required", exception.getMessage());
    }

    @Test
    void builder_WithMissingPassword_ShouldThrowException() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> StkPushRequest.builder()
                .businessShortCode("174379")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build()
        );

        assertEquals("Password is required", exception.getMessage());
    }

    @Test
    void builder_WithMissingTimestamp_ShouldThrowException() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> StkPushRequest.builder()
                .businessShortCode("174379")
                .password("password")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build()
        );

        assertEquals("Timestamp is required", exception.getMessage());
    }

    @Test
    void builder_WithMissingAmount_ShouldThrowException() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> StkPushRequest.builder()
                .businessShortCode("174379")
                .password("password")
                .timestamp("20231225143045")
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build()
        );

        assertEquals("Amount is required", exception.getMessage());
    }

    @Test
    void builder_WithMissingPhoneNumber_ShouldThrowException() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> StkPushRequest.builder()
                .businessShortCode("174379")
                .password("password")
                .timestamp("20231225143045")
                .amount(1000L)
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build()
        );

        assertEquals("Phone number is required", exception.getMessage());
    }

    @Test
    void builder_WithMissingCallbackURL_ShouldThrowException() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> StkPushRequest.builder()
                .businessShortCode("174379")
                .password("password")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build()
        );

        assertEquals("Callback URL is required", exception.getMessage());
    }

    @Test
    void builder_WithMissingAccountReference_ShouldThrowException() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> StkPushRequest.builder()
                .businessShortCode("174379")
                .password("password")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .transactionDesc("Test Payment")
                .build()
        );

        assertEquals("Account reference is required", exception.getMessage());
    }

    @Test
    void builder_WithMissingTransactionDesc_ShouldThrowException() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> StkPushRequest.builder()
                .businessShortCode("174379")
                .password("password")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .build()
        );

        assertEquals("Transaction description is required", exception.getMessage());
    }

    @Test
    void builder_WithEmptyFields_ShouldThrowExceptions() {
        // Test empty business short code
        assertThrows(IllegalStateException.class, () ->
            StkPushRequest.builder()
                .businessShortCode("   ")
                .password("password")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build()
        );

        // Test empty password
        assertThrows(IllegalStateException.class, () ->
            StkPushRequest.builder()
                .businessShortCode("174379")
                .password("   ")
                .timestamp("20231225143045")
                .amount(1000L)
                .phoneNumber("254708374149")
                .callBackURL("https://example.com/callback")
                .accountReference("TestAccount")
                .transactionDesc("Test Payment")
                .build()
        );
    }

    @Test
    void builder_ShouldSetPartyBToBusinessShortCode() {
        StkPushRequest request = StkPushRequest.builder()
            .businessShortCode("174379")
            .password("password")
            .timestamp("20231225143045")
            .amount(1000L)
            .phoneNumber("254708374149")
            .callBackURL("https://example.com/callback")
            .accountReference("TestAccount")
            .transactionDesc("Test Payment")
            .build();

        assertEquals("174379", request.partyB());
    }
}