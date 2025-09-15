package app.daraja.sdk.c2b;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class C2BRegisterUrlRequestTest {

    @Test
    void testC2BRegisterUrlRequest() {
        C2BRegisterUrlRequest request = new C2BRegisterUrlRequest(
                "600988",
                "Completed",
                "https://example.com/confirmation",
                "https://example.com/validation"
        );

        assertEquals("600988", request.shortCode());
        assertEquals("Completed", request.responseType());
        assertEquals("https://example.com/confirmation", request.confirmationURL());
        assertEquals("https://example.com/validation", request.validationURL());
    }
}
