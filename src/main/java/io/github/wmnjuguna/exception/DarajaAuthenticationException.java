package io.github.wmnjuguna.exception;

/**
 * Exception thrown when authentication with the Daraja API fails.
 * This includes issues with OAuth2 token requests, expired tokens,
 * or invalid credentials.
 */
public class DarajaAuthenticationException extends DarajaException {

    /**
     * Constructs a new DarajaAuthenticationException with the specified detail message.
     *
     * @param message the detail message
     */
    public DarajaAuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new DarajaAuthenticationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public DarajaAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DarajaAuthenticationException with the specified detail message, error code and HTTP status.
     *
     * @param message    the detail message
     * @param errorCode  the Daraja API error code
     * @param httpStatus the HTTP status code
     */
    public DarajaAuthenticationException(String message, String errorCode, int httpStatus) {
        super(message, errorCode, httpStatus);
    }

    /**
     * Constructs a new DarajaAuthenticationException with the specified detail message, cause, error code and HTTP status.
     *
     * @param message    the detail message
     * @param cause      the cause
     * @param errorCode  the Daraja API error code
     * @param httpStatus the HTTP status code
     */
    public DarajaAuthenticationException(String message, Throwable cause, String errorCode, int httpStatus) {
        super(message, cause, errorCode, httpStatus);
    }
}