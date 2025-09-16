package io.github.wmnjuguna.stkpush;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StkPushResponseTest {

    @Test
    void isSuccessful_WithResponseCodeZero_ShouldReturnTrue() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "0",
            "Success. Request accepted for processing",
            "Success. Request accepted for processing"
        );

        assertTrue(response.isSuccessful());
    }

    @Test
    void isSuccessful_WithNonZeroResponseCode_ShouldReturnFalse() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "1",
            "Duplicate request",
            "Duplicate request"
        );

        assertFalse(response.isSuccessful());
    }

    @Test
    void isSuccessful_WithNullResponseCode_ShouldReturnFalse() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            null,
            "Error",
            "Error message"
        );

        assertFalse(response.isSuccessful());
    }

    @Test
    void getResponseCodeAsInt_WithValidResponseCode_ShouldReturnInteger() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "123",
            "Some response",
            "Some message"
        );

        assertEquals(123, response.getResponseCodeAsInt());
    }

    @Test
    void getResponseCodeAsInt_WithInvalidResponseCode_ShouldReturnMinusOne() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "invalid",
            "Some response",
            "Some message"
        );

        assertEquals(-1, response.getResponseCodeAsInt());
    }

    @Test
    void getResponseCodeAsInt_WithNullResponseCode_ShouldReturnMinusOne() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            null,
            "Some response",
            "Some message"
        );

        assertEquals(-1, response.getResponseCodeAsInt());
    }

    @Test
    void isDuplicateRequest_WithResponseCodeOne_ShouldReturnTrue() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "1",
            "Duplicate request",
            "Duplicate request"
        );

        assertTrue(response.isDuplicateRequest());
    }

    @Test
    void isDuplicateRequest_WithDuplicateInDescription_ShouldReturnTrue() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "0",
            "This is a DUPLICATE transaction",
            "Duplicate detected"
        );

        assertTrue(response.isDuplicateRequest());
    }

    @Test
    void isDuplicateRequest_WithNormalResponse_ShouldReturnFalse() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "0",
            "Success. Request accepted for processing",
            "Success. Request accepted for processing"
        );

        assertFalse(response.isDuplicateRequest());
    }

    @Test
    void getUserMessage_WithCustomerMessage_ShouldReturnCustomerMessage() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "0",
            "Success. Request accepted for processing",
            "Customer: Please complete payment on your phone"
        );

        assertEquals("Customer: Please complete payment on your phone", response.getUserMessage());
    }

    @Test
    void getUserMessage_WithoutCustomerMessage_ShouldReturnResponseDescription() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "0",
            "Success. Request accepted for processing",
            null
        );

        assertEquals("Success. Request accepted for processing", response.getUserMessage());
    }

    @Test
    void getUserMessage_WithEmptyCustomerMessage_ShouldReturnResponseDescription() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "0",
            "Success. Request accepted for processing",
            "   "
        );

        assertEquals("Success. Request accepted for processing", response.getUserMessage());
    }

    @Test
    void getUserMessage_WithNullBoth_ShouldReturnUnknownResponse() {
        StkPushResponse response = new StkPushResponse(
            "ws_CO_123456789",
            "ws_CO_123456789",
            "0",
            null,
            null
        );

        assertEquals("Unknown response", response.getUserMessage());
    }

    @Test
    void recordComponents_ShouldHaveCorrectValues() {
        String merchantRequestID = "ws_CO_123456789";
        String checkoutRequestID = "ws_CO_987654321";
        String responseCode = "0";
        String responseDescription = "Success";
        String customerMessage = "Please complete payment";

        StkPushResponse response = new StkPushResponse(
            merchantRequestID,
            checkoutRequestID,
            responseCode,
            responseDescription,
            customerMessage
        );

        assertEquals(merchantRequestID, response.merchantRequestID());
        assertEquals(checkoutRequestID, response.checkoutRequestID());
        assertEquals(responseCode, response.responseCode());
        assertEquals(responseDescription, response.responseDescription());
        assertEquals(customerMessage, response.customerMessage());
    }
}