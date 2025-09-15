package app.daraja.sdk.transactionstatus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionStatusRequestTest {

    @Test
    void testTransactionStatusRequest() {
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

        assertEquals("testuser", request.initiator());
        assertEquals("credential", request.securityCredential());
        assertEquals("TransactionStatusQuery", request.commandID());
        assertEquals("ABCDEFGHI", request.transactionID());
        assertEquals("600988", request.partyA());
        assertEquals("1", request.identifierType());
        assertEquals("https://example.com/result", request.resultURL());
        assertEquals("https://example.com/timeout", request.queueTimeOutURL());
        assertEquals("remarks", request.remarks());
        assertEquals("occasion", request.occasion());
    }
}
