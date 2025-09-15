package app.daraja.sdk.error;

import app.daraja.sdk.exception.DarajaApiException;
import app.daraja.sdk.exception.DarajaAuthenticationException;
import app.daraja.sdk.exception.DarajaException;
import app.daraja.sdk.exception.InvalidDarajaRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Custom Feign ErrorDecoder for Daraja API responses.
 * Converts HTTP error responses into specific Daraja exception types.
 */
public class DarajaErrorDecoder implements ErrorDecoder {

    private static final Logger logger = LoggerFactory.getLogger(DarajaErrorDecoder.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = null;
        try {
            responseBody = extractResponseBody(response);
        } catch (IOException e) {
            logger.warn("Failed to read error response body", e);
        }

        int status = response.status();
        String errorCode = null;
        String errorMessage = "API request failed";

        // Try to parse error details from response body
        if (responseBody != null && !responseBody.trim().isEmpty()) {
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                errorCode = extractErrorCode(jsonNode);
                String parsedMessage = extractErrorMessage(jsonNode);
                if (parsedMessage != null && !parsedMessage.trim().isEmpty()) {
                    errorMessage = parsedMessage;
                }
            } catch (Exception e) {
                logger.warn("Failed to parse error response JSON", e);
                // Use the raw response body as the error message if JSON parsing fails
                errorMessage = responseBody;
            }
        }

        // Create appropriate exception based on status code
        return switch (status) {
            case 400 -> new InvalidDarajaRequestException(
                    "Bad Request: " + errorMessage, errorCode, status
            );
            case 401 -> new DarajaAuthenticationException(
                    "Unauthorized: " + errorMessage, errorCode, status
            );
            case 403 -> new DarajaAuthenticationException(
                    "Forbidden: " + errorMessage, errorCode, status
            );
            case 404 -> new DarajaApiException(
                    "Not Found: " + errorMessage, responseBody, errorCode, status
            );
            case 422 -> new InvalidDarajaRequestException(
                    "Unprocessable Entity: " + errorMessage, errorCode, status
            );
            case 429 -> new DarajaApiException(
                    "Rate Limited: " + errorMessage, responseBody, errorCode, status
            );
            case 500 -> new DarajaApiException(
                    "Internal Server Error: " + errorMessage, responseBody, errorCode, status
            );
            case 502 -> new DarajaApiException(
                    "Bad Gateway: " + errorMessage, responseBody, errorCode, status
            );
            case 503 -> new DarajaApiException(
                    "Service Unavailable: " + errorMessage, responseBody, errorCode, status
            );
            case 504 -> new DarajaApiException(
                    "Gateway Timeout: " + errorMessage, responseBody, errorCode, status
            );
            default -> {
                if (status >= 400 && status < 500) {
                    yield new InvalidDarajaRequestException(
                            "Client Error (" + status + "): " + errorMessage, errorCode, status
                    );
                } else if (status >= 500) {
                    yield new DarajaApiException(
                            "Server Error (" + status + "): " + errorMessage, responseBody, errorCode, status
                    );
                } else {
                    yield new DarajaException(
                            "Unexpected Error (" + status + "): " + errorMessage, errorCode, status
                    );
                }
            }
        };
    }

    /**
     * Extracts the response body as a string from the Feign response.
     *
     * @param response the Feign response
     * @return the response body as a string
     * @throws IOException if reading the response body fails
     */
    private String extractResponseBody(Response response) throws IOException {
        if (response.body() == null) {
            return null;
        }

        try (InputStream inputStream = response.body().asInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Extracts the error code from the JSON response.
     * Tries common field names used in Daraja API responses.
     *
     * @param jsonNode the JSON response
     * @return the error code, or null if not found
     */
    private String extractErrorCode(JsonNode jsonNode) {
        // Try different possible field names for error codes
        String[] errorCodeFields = {
                "errorCode", "error_code", "code",
                "responseCode", "response_code",
                "resultCode", "result_code"
        };

        for (String field : errorCodeFields) {
            JsonNode codeNode = jsonNode.get(field);
            if (codeNode != null && !codeNode.isNull()) {
                return codeNode.asText();
            }
        }

        return null;
    }

    /**
     * Extracts the error message from the JSON response.
     * Tries common field names used in Daraja API responses.
     *
     * @param jsonNode the JSON response
     * @return the error message, or null if not found
     */
    private String extractErrorMessage(JsonNode jsonNode) {
        // Try different possible field names for error messages
        String[] errorMessageFields = {
                "errorMessage", "error_message", "message",
                "responseDescription", "response_description",
                "resultDesc", "result_desc", "description",
                "error", "reason"
        };

        for (String field : errorMessageFields) {
            JsonNode messageNode = jsonNode.get(field);
            if (messageNode != null && !messageNode.isNull()) {
                String message = messageNode.asText();
                if (message != null && !message.trim().isEmpty()) {
                    return message;
                }
            }
        }

        return null;
    }
}