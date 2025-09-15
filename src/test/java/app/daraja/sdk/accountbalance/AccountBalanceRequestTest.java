package app.daraja.sdk.accountbalance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountBalanceRequestTest {

    @Test
    void testAccountBalanceRequest() {
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

        assertEquals("testuser", request.initiator());
        assertEquals("credential", request.securityCredential());
        assertEquals("AccountBalance", request.commandID());
        assertEquals("600988", request.partyA());
        assertEquals("4", request.identifierType());
        assertEquals("remarks", request.remarks());
        assertEquals("https://example.com/timeout", request.queueTimeOutURL());
        assertEquals("https://example.com/result", request.resultURL());
    }
}
