package io.github.wmnjuguna.stkpush;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StkPushCallbackTest {

    @Test
    void isSuccessful_WithSuccessfulCallback_ShouldReturnTrue() {
        StkPushCallback callback = createSuccessfulCallback();
        assertTrue(callback.isSuccessful());
    }

    @Test
    void isSuccessful_WithFailedCallback_ShouldReturnFalse() {
        StkPushCallback callback = createFailedCallback();
        assertFalse(callback.isSuccessful());
    }

    @Test
    void isSuccessful_WithNullBody_ShouldReturnFalse() {
        StkPushCallback callback = new StkPushCallback(null);
        assertFalse(callback.isSuccessful());
    }

    @Test
    void stkCallback_isSuccessful_WithResultCodeZero_ShouldReturnTrue() {
        StkPushCallback.StkCallback stkCallback = new StkPushCallback.StkCallback(
            "ws_CO_123456789",
            "ws_CO_123456789",
            0,
            "The service request is processed successfully.",
            createSuccessfulMetadata()
        );

        assertTrue(stkCallback.isSuccessful());
    }

    @Test
    void stkCallback_wasCancelled_WithCancelCode_ShouldReturnTrue() {
        StkPushCallback.StkCallback stkCallback = new StkPushCallback.StkCallback(
            "ws_CO_123456789",
            "ws_CO_123456789",
            1032,
            "Request cancelled by user",
            null
        );

        assertTrue(stkCallback.wasCancelled());
        assertFalse(stkCallback.isSuccessful());
    }

    @Test
    void stkCallback_wasTimedOut_WithTimeoutCode_ShouldReturnTrue() {
        StkPushCallback.StkCallback stkCallback = new StkPushCallback.StkCallback(
            "ws_CO_123456789",
            "ws_CO_123456789",
            1037,
            "Transaction timeout",
            null
        );

        assertTrue(stkCallback.wasTimedOut());
        assertFalse(stkCallback.isSuccessful());
    }

    @Test
    void callbackMetadata_getAmount_ShouldReturnCorrectValue() {
        StkPushCallback.CallbackMetadata metadata = createSuccessfulMetadata();
        assertEquals(1000.0, metadata.getAmount());
    }

    @Test
    void callbackMetadata_getReceiptNumber_ShouldReturnCorrectValue() {
        StkPushCallback.CallbackMetadata metadata = createSuccessfulMetadata();
        assertEquals("NLJ7RT61SV", metadata.getReceiptNumber());
    }

    @Test
    void callbackMetadata_getTransactionDate_ShouldReturnCorrectValue() {
        StkPushCallback.CallbackMetadata metadata = createSuccessfulMetadata();
        assertEquals(20231225143045L, metadata.getTransactionDate());
    }

    @Test
    void callbackMetadata_getPhoneNumber_ShouldReturnCorrectValue() {
        StkPushCallback.CallbackMetadata metadata = createSuccessfulMetadata();
        assertEquals("254708374149", metadata.getPhoneNumber());
    }

    @Test
    void callbackMetadata_getItemValue_WithNonExistentItem_ShouldReturnDefault() {
        StkPushCallback.CallbackMetadata metadata = createSuccessfulMetadata();
        String defaultValue = "default";

        String result = metadata.getItemValue("NonExistentField", defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    void callbackMetadata_getItemValue_WithDifferentTypes_ShouldConvertCorrectly() {
        List<StkPushCallback.CallbackItem> items = List.of(
            new StkPushCallback.CallbackItem("StringField", "stringValue"),
            new StkPushCallback.CallbackItem("IntField", 42),
            new StkPushCallback.CallbackItem("DoubleField", 3.14),
            new StkPushCallback.CallbackItem("LongField", 123456789L)
        );

        StkPushCallback.CallbackMetadata metadata = new StkPushCallback.CallbackMetadata(items);

        assertEquals("stringValue", metadata.getItemValue("StringField", "default"));
        assertEquals(Integer.valueOf(42), metadata.getItemValue("IntField", 0));
        assertEquals(Double.valueOf(3.14), metadata.getItemValue("DoubleField", 0.0));
        assertEquals(Long.valueOf(123456789L), metadata.getItemValue("LongField", 0L));
    }

    @Test
    void callbackMetadata_WithNullItems_ShouldReturnDefaults() {
        StkPushCallback.CallbackMetadata metadata = new StkPushCallback.CallbackMetadata(null);

        assertEquals(0.0, metadata.getAmount());
        assertNull(metadata.getReceiptNumber());
        assertEquals(0L, metadata.getTransactionDate());
        assertNull(metadata.getPhoneNumber());
    }

    @Test
    void getResultDescription_ShouldReturnCorrectValue() {
        StkPushCallback callback = createSuccessfulCallback();
        assertEquals("The service request is processed successfully.", callback.getResultDescription());
    }

    @Test
    void getMerchantRequestID_ShouldReturnCorrectValue() {
        StkPushCallback callback = createSuccessfulCallback();
        assertEquals("ws_CO_123456789", callback.getMerchantRequestID());
    }

    @Test
    void getCheckoutRequestID_ShouldReturnCorrectValue() {
        StkPushCallback callback = createSuccessfulCallback();
        assertEquals("ws_CO_987654321", callback.getCheckoutRequestID());
    }

    @Test
    void getters_WithNullBody_ShouldReturnNull() {
        StkPushCallback callback = new StkPushCallback(null);

        assertNull(callback.getResultDescription());
        assertNull(callback.getMerchantRequestID());
        assertNull(callback.getCheckoutRequestID());
    }

    private StkPushCallback createSuccessfulCallback() {
        List<StkPushCallback.CallbackItem> items = List.of(
            new StkPushCallback.CallbackItem("Amount", 1000.0),
            new StkPushCallback.CallbackItem("MpesaReceiptNumber", "NLJ7RT61SV"),
            new StkPushCallback.CallbackItem("TransactionDate", 20231225143045L),
            new StkPushCallback.CallbackItem("PhoneNumber", "254708374149")
        );

        StkPushCallback.CallbackMetadata metadata = new StkPushCallback.CallbackMetadata(items);

        StkPushCallback.StkCallback stkCallback = new StkPushCallback.StkCallback(
            "ws_CO_123456789",
            "ws_CO_987654321",
            0,
            "The service request is processed successfully.",
            metadata
        );

        StkPushCallback.CallbackBody body = new StkPushCallback.CallbackBody(stkCallback);

        return new StkPushCallback(body);
    }

    private StkPushCallback createFailedCallback() {
        StkPushCallback.StkCallback stkCallback = new StkPushCallback.StkCallback(
            "ws_CO_123456789",
            "ws_CO_987654321",
            1032,
            "Request cancelled by user",
            null
        );

        StkPushCallback.CallbackBody body = new StkPushCallback.CallbackBody(stkCallback);

        return new StkPushCallback(body);
    }

    private StkPushCallback.CallbackMetadata createSuccessfulMetadata() {
        List<StkPushCallback.CallbackItem> items = List.of(
            new StkPushCallback.CallbackItem("Amount", 1000.0),
            new StkPushCallback.CallbackItem("MpesaReceiptNumber", "NLJ7RT61SV"),
            new StkPushCallback.CallbackItem("TransactionDate", 20231225143045L),
            new StkPushCallback.CallbackItem("PhoneNumber", "254708374149")
        );

        return new StkPushCallback.CallbackMetadata(items);
    }
}