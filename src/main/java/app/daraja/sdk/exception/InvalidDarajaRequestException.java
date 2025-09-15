package app.daraja.sdk.exception;

/**
 * Exception thrown when a request to the Daraja API is invalid.
 * This includes validation errors, missing required fields,
 * or malformed request data.
 */
public class InvalidDarajaRequestException extends DarajaException {

    /**
     * Constructs a new InvalidDarajaRequestException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidDarajaRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidDarajaRequestException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public InvalidDarajaRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new InvalidDarajaRequestException with the specified detail message, error code and HTTP status.
     *
     * @param message    the detail message
     * @param errorCode  the Daraja API error code
     * @param httpStatus the HTTP status code
     */
    public InvalidDarajaRequestException(String message, String errorCode, int httpStatus) {
        super(message, errorCode, httpStatus);
    }

    /**
     * Constructs a new InvalidDarajaRequestException with the specified detail message, cause, error code and HTTP status.
     *
     * @param message    the detail message
     * @param cause      the cause
     * @param errorCode  the Daraja API error code
     * @param httpStatus the HTTP status code
     */
    public InvalidDarajaRequestException(String message, Throwable cause, String errorCode, int httpStatus) {
        super(message, cause, errorCode, httpStatus);
    }
}