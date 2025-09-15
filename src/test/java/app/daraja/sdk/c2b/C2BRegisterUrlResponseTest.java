package app.daraja.sdk.c2b;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class C2BRegisterUrlResponseTest {

    @Test
    void testC2BRegisterUrlResponse() {
        C2BRegisterUrlResponse response = new C2BRegisterUrlResponse(
                "AG_20250915_000000000000000000000001",
                "AG_20250915_000000000000000000000001",
                "Success"
        );

        assertEquals("AG_20250915_000000000000000000000001", response.conversationID());
        assertEquals("AG_20250915_000000000000000000000001", response.originatorConversationID());
        assertEquals("Success", response.responseDescription());
    }
}
