package app.daraja.sdk.exception;

/**
 * Base exception class for all Daraja API related exceptions.
 * All specific Daraja exceptions should extend this class.
 */
public class DarajaException extends RuntimeException {

    private final String errorCode;
    private final int httpStatus;

    /**
     * Constructs a new DarajaException with the specified detail message.
     *
     * @param message the detail message
     */
    public DarajaException(String message) {
        this(message, null, 0);
    }

    /**
     * Constructs a new DarajaException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public DarajaException(String message, Throwable cause) {
        this(message, cause, null, 0);
    }

    /**
     * Constructs a new DarajaException with the specified detail message, error code and HTTP status.
     *
     * @param message    the detail message
     * @param errorCode  the Daraja API error code
     * @param httpStatus the HTTP status code
     */
    public DarajaException(String message, String errorCode, int httpStatus) {
        this(message, null, errorCode, httpStatus);
    }

    /**
     * Constructs a new DarajaException with the specified detail message, cause, error code and HTTP status.
     *
     * @param message    the detail message
     * @param cause      the cause
     * @param errorCode  the Daraja API error code
     * @param httpStatus the HTTP status code
     */
    public DarajaException(String message, Throwable cause, String errorCode, int httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets the Daraja API error code if available.
     *
     * @return the error code, or null if not available
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the HTTP status code if available.
     *
     * @return the HTTP status code, or 0 if not available
     */
    public int getHttpStatus() {
        return httpStatus;
    }

    /**
     * Checks if this exception has an error code.
     *
     * @return true if error code is not null and not empty
     */
    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.trim().isEmpty();
    }

    /**
     * Checks if this exception has an HTTP status code.
     *
     * @return true if HTTP status code is greater than 0
     */
    public boolean hasHttpStatus() {
        return httpStatus > 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (hasErrorCode()) {
            sb.append(" [Error Code: ").append(errorCode).append("]");
        }
        if (hasHttpStatus()) {
            sb.append(" [HTTP Status: ").append(httpStatus).append("]");
        }
        return sb.toString();
    }
}