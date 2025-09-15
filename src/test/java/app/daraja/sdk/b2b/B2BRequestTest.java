package app.daraja.sdk.b2b;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class B2BRequestTest {

    @Test
    void testB2BRequest() {
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

        assertEquals("testuser", request.initiator());
        assertEquals("credential", request.securityCredential());
        assertEquals("BusinessToBusinessTransfer", request.commandID());
        assertEquals("4", request.senderIdentifierType());
        assertEquals("4", request.recieverIdentifierType());
        assertEquals("100", request.amount());
        assertEquals("600988", request.partyA());
        assertEquals("600988", request.partyB());
        assertEquals("remarks", request.remarks());
        assertEquals("https://example.com/timeout", request.queueTimeOutURL());
        assertEquals("https://example.com/result", request.resultURL());
        assertEquals("reference", request.accountReference());
    }
}
