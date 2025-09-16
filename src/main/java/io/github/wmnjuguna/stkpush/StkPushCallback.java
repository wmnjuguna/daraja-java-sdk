package io.github.wmnjuguna.stkpush;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Callback DTO for STK Push (M-Pesa Express) payment completion notifications.
 * This represents the structure of the callback request sent by Safaricom
 * to the specified callback URL after payment processing.
 */
public record StkPushCallback(
    @JsonProperty("Body")
    CallbackBody body
) {

    /**
     * The main body of the STK Push callback.
     */
    public record CallbackBody(
        @JsonProperty("stkCallback")
        StkCallback stkCallback
    ) {}

    /**
     * The STK callback details containing transaction information.
     */
    public record StkCallback(
        @JsonProperty("MerchantRequestID")
        String merchantRequestID,

        @JsonProperty("CheckoutRequestID")
        String checkoutRequestID,

        @JsonProperty("ResultCode")
        int resultCode,

        @JsonProperty("ResultDesc")
        String resultDesc,

        @JsonProperty("CallbackMetadata")
        CallbackMetadata callbackMetadata
    ) {

        /**
         * Checks if the transaction was successful.
         *
         * @return true if result code is 0 (success), false otherwise
         */
        public boolean isSuccessful() {
            return resultCode == 0;
        }

        /**
         * Checks if the transaction was cancelled by the user.
         *
         * @return true if result code indicates user cancellation
         */
        public boolean wasCancelled() {
            return resultCode == 1032; // User cancelled transaction
        }

        /**
         * Checks if the transaction timed out.
         *
         * @return true if result code indicates timeout
         */
        public boolean wasTimedOut() {
            return resultCode == 1037; // Timeout
        }
    }

    /**
     * Metadata containing transaction details for successful payments.
     * This will be null for failed transactions.
     */
    public record CallbackMetadata(
        @JsonProperty("Item")
        List<CallbackItem> items
    ) {

        /**
         * Gets the transaction amount.
         *
         * @return the amount as a double, or 0.0 if not found
         */
        public double getAmount() {
            return getItemValue("Amount", 0.0);
        }

        /**
         * Gets the M-Pesa receipt number.
         *
         * @return the receipt number, or null if not found
         */
        public String getReceiptNumber() {
            return getItemValue("MpesaReceiptNumber", null);
        }

        /**
         * Gets the transaction date.
         *
         * @return the transaction date as a long timestamp, or 0 if not found
         */
        public long getTransactionDate() {
            return getItemValue("TransactionDate", 0L);
        }

        /**
         * Gets the phone number that made the payment.
         *
         * @return the phone number, or null if not found
         */
        public String getPhoneNumber() {
            return getItemValue("PhoneNumber", null);
        }

        /**
         * Gets a specific item value by name.
         *
         * @param name the name of the item to find
         * @param defaultValue the default value if item is not found
         * @return the item value or default value
         */
        @SuppressWarnings("unchecked")
        public <T> T getItemValue(String name, T defaultValue) {
            if (items == null) return defaultValue;

            return items.stream()
                .filter(item -> name.equals(item.name()))
                .map(item -> {
                    try {
                        if (defaultValue instanceof Double) {
                            return (T) Double.valueOf(item.value().toString());
                        } else if (defaultValue instanceof Long) {
                            return (T) Long.valueOf(item.value().toString());
                        } else if (defaultValue instanceof Integer) {
                            return (T) Integer.valueOf(item.value().toString());
                        } else {
                            return (T) item.value();
                        }
                    } catch (Exception e) {
                        return defaultValue;
                    }
                })
                .findFirst()
                .orElse(defaultValue);
        }
    }

    /**
     * Individual callback metadata item.
     */
    public record CallbackItem(
        @JsonProperty("Name")
        String name,

        @JsonProperty("Value")
        Object value
    ) {}

    /**
     * Checks if the overall callback indicates a successful transaction.
     *
     * @return true if transaction was successful, false otherwise
     */
    public boolean isSuccessful() {
        return body != null &&
               body.stkCallback() != null &&
               body.stkCallback().isSuccessful();
    }

    /**
     * Gets the result description from the callback.
     *
     * @return the result description, or null if not available
     */
    public String getResultDescription() {
        return body != null && body.stkCallback() != null ?
               body.stkCallback().resultDesc() : null;
    }

    /**
     * Gets the merchant request ID from the callback.
     *
     * @return the merchant request ID, or null if not available
     */
    public String getMerchantRequestID() {
        return body != null && body.stkCallback() != null ?
               body.stkCallback().merchantRequestID() : null;
    }

    /**
     * Gets the checkout request ID from the callback.
     *
     * @return the checkout request ID, or null if not available
     */
    public String getCheckoutRequestID() {
        return body != null && body.stkCallback() != null ?
               body.stkCallback().checkoutRequestID() : null;
    }
}