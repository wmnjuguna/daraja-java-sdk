package io.github.wmnjuguna.b2c;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class B2CRequestTest {

    @Test
    void testB2CRequest() {
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

        assertEquals("testuser", request.initiatorName());
        assertEquals("credential", request.securityCredential());
        assertEquals("BusinessPayment", request.commandID());
        assertEquals("100", request.amount());
        assertEquals("600988", request.partyA());
        assertEquals("254708374149", request.partyB());
        assertEquals("remarks", request.remarks());
        assertEquals("https://example.com/timeout", request.queueTimeOutURL());
        assertEquals("https://example.com/result", request.resultURL());
        assertEquals("occasion", request.occassion());
    }
}
