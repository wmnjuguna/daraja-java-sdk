package io.github.wmnjuguna.exception;

/**
 * Exception thrown when the Daraja API returns an error response.
 * This includes server errors, service unavailable, or other
 * API-specific error conditions.
 */
public class DarajaApiException extends DarajaException {

    private final String responseBody;

    /**
     * Constructs a new DarajaApiException with the specified detail message.
     *
     * @param message the detail message
     */
    public DarajaApiException(String message) {
        this(message, null, null, 0);
    }

    /**
     * Constructs a new DarajaApiException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public DarajaApiException(String message, Throwable cause) {
        this(message, cause, null, null, 0);
    }

    /**
     * Constructs a new DarajaApiException with the specified detail message, response body, error code and HTTP status.
     *
     * @param message      the detail message
     * @param responseBody the API response body
     * @param errorCode    the Daraja API error code
     * @param httpStatus   the HTTP status code
     */
    public DarajaApiException(String message, String responseBody, String errorCode, int httpStatus) {
        this(message, null, responseBody, errorCode, httpStatus);
    }

    /**
     * Constructs a new DarajaApiException with the specified detail message, cause, response body, error code and HTTP status.
     *
     * @param message      the detail message
     * @param cause        the cause
     * @param responseBody the API response body
     * @param errorCode    the Daraja API error code
     * @param httpStatus   the HTTP status code
     */
    public DarajaApiException(String message, Throwable cause, String responseBody, String errorCode, int httpStatus) {
        super(message, cause, errorCode, httpStatus);
        this.responseBody = responseBody;
    }

    /**
     * Gets the API response body if available.
     *
     * @return the response body, or null if not available
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Checks if this exception has a response body.
     *
     * @return true if response body is not null and not empty
     */
    public boolean hasResponseBody() {
        return responseBody != null && !responseBody.trim().isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (hasResponseBody()) {
            sb.append(" [Response Body: ").append(responseBody).append("]");
        }
        return sb.toString();
    }
}