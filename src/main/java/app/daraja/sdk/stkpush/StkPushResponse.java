package app.daraja.sdk.stkpush;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for STK Push (M-Pesa Express) payment initiation.
 * Contains the response from the payment request to customer's phone.
 *
 * @param merchantRequestID   the merchant request ID for tracking
 * @param checkoutRequestID   the checkout request ID for tracking
 * @param responseCode       the response code from the API
 * @param responseDescription the response description
 * @param customerMessage    the message to display to the customer
 */
public record StkPushResponse(
    @JsonProperty("MerchantRequestID")
    String merchantRequestID,

    @JsonProperty("CheckoutRequestID")
    String checkoutRequestID,

    @JsonProperty("ResponseCode")
    String responseCode,

    @JsonProperty("ResponseDescription")
    String responseDescription,

    @JsonProperty("CustomerMessage")
    String customerMessage
) {

    /**
     * Checks if the STK push request was successful.
     *
     * @return true if response code indicates success, false otherwise
     */
    public boolean isSuccessful() {
        return "0".equals(responseCode);
    }

    /**
     * Gets the response code as an integer.
     *
     * @return the response code as integer, or -1 if parsing fails
     */
    public int getResponseCodeAsInt() {
        try {
            return Integer.parseInt(responseCode);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Checks if this is a duplicate request.
     * Common response code for duplicate requests is "1".
     *
     * @return true if this appears to be a duplicate request
     */
    public boolean isDuplicateRequest() {
        return "1".equals(responseCode) ||
               (responseDescription != null && responseDescription.toLowerCase().contains("duplicate"));
    }

    /**
     * Gets a user-friendly message based on the response.
     *
     * @return customer message if available, otherwise response description
     */
    public String getUserMessage() {
        if (customerMessage != null && !customerMessage.trim().isEmpty()) {
            return customerMessage;
        }
        return responseDescription != null ? responseDescription : "Unknown response";
    }
}