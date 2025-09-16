package io.github.wmnjuguna.reversal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReversalRequestTest {

    @Test
    void testReversalRequest() {
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

        assertEquals("testuser", request.initiator());
        assertEquals("credential", request.securityCredential());
        assertEquals("TransactionReversal", request.commandID());
        assertEquals("ABCDEFGHI", request.transactionID());
        assertEquals("100", request.amount());
        assertEquals("600988", request.receiverParty());
        assertEquals("4", request.recieverIdentifierType());
        assertEquals("https://example.com/result", request.resultURL());
        assertEquals("https://example.com/timeout", request.queueTimeOutURL());
        assertEquals("remarks", request.remarks());
        assertEquals("occasion", request.occasion());
    }
}
