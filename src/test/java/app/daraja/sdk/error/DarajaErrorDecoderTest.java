package app.daraja.sdk.error;

import app.daraja.sdk.exception.DarajaApiException;
import app.daraja.sdk.exception.DarajaAuthenticationException;
import app.daraja.sdk.exception.DarajaException;
import app.daraja.sdk.exception.InvalidDarajaRequestException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DarajaErrorDecoderTest {

    private DarajaErrorDecoder errorDecoder;
    private Request request;

    @BeforeEach
    void setUp() {
        errorDecoder = new DarajaErrorDecoder();
        request = Request.create(Request.HttpMethod.POST, "http://example.com", Collections.emptyMap(), null, StandardCharsets.UTF_8, null);
    }

    @Test
    void decode_Status400_ShouldReturnInvalidDarajaRequestException() {
        Response response = createResponse(400, "{\"errorMessage\": \"Bad request\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(InvalidDarajaRequestException.class, exception);
        assertTrue(exception.getMessage().contains("Bad Request"));
        assertTrue(exception.getMessage().contains("Bad request"));
    }

    @Test
    void decode_Status401_ShouldReturnDarajaAuthenticationException() {
        Response response = createResponse(401, "{\"error\": \"Unauthorized access\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaAuthenticationException.class, exception);
        assertTrue(exception.getMessage().contains("Unauthorized"));
        assertTrue(exception.getMessage().contains("Unauthorized access"));
    }

    @Test
    void decode_Status403_ShouldReturnDarajaAuthenticationException() {
        Response response = createResponse(403, "{\"message\": \"Forbidden\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaAuthenticationException.class, exception);
        assertTrue(exception.getMessage().contains("Forbidden"));
    }

    @Test
    void decode_Status404_ShouldReturnDarajaApiException() {
        Response response = createResponse(404, "{\"description\": \"Resource not found\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaApiException.class, exception);
        assertTrue(exception.getMessage().contains("Not Found"));
        assertTrue(exception.getMessage().contains("Resource not found"));
    }

    @Test
    void decode_Status422_ShouldReturnInvalidDarajaRequestException() {
        Response response = createResponse(422, "{\"resultDesc\": \"Validation failed\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(InvalidDarajaRequestException.class, exception);
        assertTrue(exception.getMessage().contains("Unprocessable Entity"));
        assertTrue(exception.getMessage().contains("Validation failed"));
    }

    @Test
    void decode_Status429_ShouldReturnDarajaApiException() {
        Response response = createResponse(429, "{\"reason\": \"Too many requests\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaApiException.class, exception);
        assertTrue(exception.getMessage().contains("Rate Limited"));
        assertTrue(exception.getMessage().contains("Too many requests"));
    }

    @Test
    void decode_Status500_ShouldReturnDarajaApiException() {
        Response response = createResponse(500, "{\"responseDescription\": \"Internal server error\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaApiException.class, exception);
        assertTrue(exception.getMessage().contains("Internal Server Error"));
        assertTrue(exception.getMessage().contains("Internal server error"));
    }

    @Test
    void decode_Status502_ShouldReturnDarajaApiException() {
        Response response = createResponse(502, "{\"error_message\": \"Bad gateway\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaApiException.class, exception);
        assertTrue(exception.getMessage().contains("Bad Gateway"));
        assertTrue(exception.getMessage().contains("Bad gateway"));
    }

    @Test
    void decode_Status503_ShouldReturnDarajaApiException() {
        Response response = createResponse(503, "{\"message\": \"Service unavailable\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaApiException.class, exception);
        assertTrue(exception.getMessage().contains("Service Unavailable"));
        assertTrue(exception.getMessage().contains("Service unavailable"));
    }

    @Test
    void decode_Status504_ShouldReturnDarajaApiException() {
        Response response = createResponse(504, "{\"description\": \"Gateway timeout\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaApiException.class, exception);
        assertTrue(exception.getMessage().contains("Gateway Timeout"));
        assertTrue(exception.getMessage().contains("Gateway timeout"));
    }

    @Test
    void decode_UnknownClientError_ShouldReturnInvalidDarajaRequestException() {
        Response response = createResponse(418, "{\"message\": \"I'm a teapot\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(InvalidDarajaRequestException.class, exception);
        assertTrue(exception.getMessage().contains("Client Error (418)"));
        assertTrue(exception.getMessage().contains("I'm a teapot"));
    }

    @Test
    void decode_UnknownServerError_ShouldReturnDarajaApiException() {
        Response response = createResponse(599, "{\"message\": \"Unknown server error\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaApiException.class, exception);
        assertTrue(exception.getMessage().contains("Server Error (599)"));
        assertTrue(exception.getMessage().contains("Unknown server error"));
    }

    @Test
    void decode_UnknownError_ShouldReturnDarajaException() {
        Response response = createResponse(300, "{\"message\": \"Redirection\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(DarajaException.class, exception);
        assertFalse(exception instanceof DarajaApiException);
        assertFalse(exception instanceof InvalidDarajaRequestException);
        assertFalse(exception instanceof DarajaAuthenticationException);
        assertTrue(exception.getMessage().contains("Unexpected Error (300)"));
    }

    @Test
    void decode_WithErrorCode_ShouldExtractErrorCode() {
        Response response = createResponse(400, "{\"errorCode\": \"ERR001\", \"message\": \"Bad request\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(InvalidDarajaRequestException.class, exception);
        DarajaException darajaException = (DarajaException) exception;
        assertEquals("ERR001", darajaException.getErrorCode());
        assertEquals(400, darajaException.getHttpStatus());
    }

    @Test
    void decode_WithDifferentErrorCodeFields_ShouldExtractCorrectly() {
        // Test different error code field names
        String[] errorCodeFields = {"responseCode", "resultCode", "code"};
        String[] expectedCodes = {"RESP001", "RES001", "CODE001"};

        for (int i = 0; i < errorCodeFields.length; i++) {
            String json = String.format("{\"%s\": \"%s\", \"message\": \"Error\"}",
                errorCodeFields[i], expectedCodes[i]);
            Response response = createResponse(400, json);

            Exception exception = errorDecoder.decode("testMethod", response);

            DarajaException darajaException = (DarajaException) exception;
            assertEquals(expectedCodes[i], darajaException.getErrorCode());
        }
    }

    @Test
    void decode_WithEmptyResponseBody_ShouldUseDefaultMessage() {
        Response response = createResponse(400, "");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(InvalidDarajaRequestException.class, exception);
        assertTrue(exception.getMessage().contains("Bad Request: API request failed"));
    }

    @Test
    void decode_WithNullResponseBody_ShouldUseDefaultMessage() {
        Response response = createResponse(400, null);

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(InvalidDarajaRequestException.class, exception);
        assertTrue(exception.getMessage().contains("Bad Request: API request failed"));
    }

    @Test
    void decode_WithInvalidJson_ShouldUseRawBodyAsMessage() {
        Response response = createResponse(400, "Invalid JSON content");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertInstanceOf(InvalidDarajaRequestException.class, exception);
        assertTrue(exception.getMessage().contains("Invalid JSON content"));
    }

    @Test
    void decode_WithMultipleMessageFields_ShouldExtractFirst() {
        Response response = createResponse(400, "{\"errorMessage\": \"Primary error\", \"message\": \"Secondary error\"}");

        Exception exception = errorDecoder.decode("testMethod", response);

        assertTrue(exception.getMessage().contains("Primary error"));
        assertFalse(exception.getMessage().contains("Secondary error"));
    }

    private Response createResponse(int status, String body) {
        return Response.builder()
            .status(status)
            .reason("Test Reason")
            .request(request)
            .headers(Collections.emptyMap())
            .body(body, StandardCharsets.UTF_8)
            .build();
    }
}