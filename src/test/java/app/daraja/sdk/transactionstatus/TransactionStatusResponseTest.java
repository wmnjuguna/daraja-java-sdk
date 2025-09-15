package app.daraja.sdk.transactionstatus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionStatusResponseTest {

    @Test
    void testTransactionStatusResponse() {
        TransactionStatusResponse response = new TransactionStatusResponse(
                "AG_20250915_000000000000000000000001",
                "AG_20250915_000000000000000000000001",
                "0",
                "Success"
        );

        assertEquals("AG_20250915_000000000000000000000001", response.conversationID());
        assertEquals("AG_20250915_000000000000000000000001", response.originatorConversationID());
        assertEquals("0", response.responseCode());
        assertEquals("Success", response.responseDescription());
    }
}
