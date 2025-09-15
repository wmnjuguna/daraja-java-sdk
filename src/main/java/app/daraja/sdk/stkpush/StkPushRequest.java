package app.daraja.sdk.stkpush;

import app.daraja.sdk.util.CallbackUrlValidator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO for STK Push (M-Pesa Express) payment initiation.
 * Contains all required parameters for initiating a payment request to customer's phone.
 *
 * @param businessShortCode   the organization shortcode used to receive the transaction
 * @param password           Base64 encoded string (BusinessShortCode + Passkey + Timestamp)
 * @param timestamp          the timestamp of the transaction (YYYYMMDDHHmmss)
 * @param transactionType    the transaction type, always "CustomerPayBillOnline" for STK Push
 * @param amount            the amount to be transacted
 * @param partyA            the phone number sending money (format: 254XXXXXXXXX)
 * @param partyB            the organization shortcode receiving the funds
 * @param phoneNumber       the mobile number to receive the STK Pin Prompt (format: 254XXXXXXXXX)
 * @param callBackURL       the callback URL for the service
 * @param accountReference  the account reference for the transaction
 * @param transactionDesc   the description of the transaction
 */
public record StkPushRequest(
    @JsonProperty("BusinessShortCode")
    String businessShortCode,

    @JsonProperty("Password")
    String password,

    @JsonProperty("Timestamp")
    String timestamp,

    @JsonProperty("TransactionType")
    String transactionType,

    @JsonProperty("Amount")
    String amount,

    @JsonProperty("PartyA")
    String partyA,

    @JsonProperty("PartyB")
    String partyB,

    @JsonProperty("PhoneNumber")
    String phoneNumber,

    @JsonProperty("CallBackURL")
    String callBackURL,

    @JsonProperty("AccountReference")
    String accountReference,

    @JsonProperty("TransactionDesc")
    String transactionDesc
) {

    /**
     * Creates a new STK Push request builder.
     *
     * @return a new StkPushRequestBuilder instance
     */
    public static StkPushRequestBuilder builder() {
        return new StkPushRequestBuilder();
    }

    /**
     * Builder class for creating StkPushRequest instances.
     */
    public static class StkPushRequestBuilder {
        private String businessShortCode;
        private String password;
        private String timestamp;
        private String transactionType = "CustomerPayBillOnline"; // Default value
        private String amount;
        private String partyA;
        private String partyB;
        private String phoneNumber;
        private String callBackURL;
        private String accountReference;
        private String transactionDesc;

        private StkPushRequestBuilder() {}

        public StkPushRequestBuilder businessShortCode(String businessShortCode) {
            this.businessShortCode = businessShortCode;
            this.partyB = businessShortCode; // PartyB is typically same as BusinessShortCode
            return this;
        }

        public StkPushRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public StkPushRequestBuilder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public StkPushRequestBuilder amount(long amount) {
            this.amount = String.valueOf(amount);
            return this;
        }

        public StkPushRequestBuilder amount(String amount) {
            this.amount = amount;
            return this;
        }

        public StkPushRequestBuilder phoneNumber(String phoneNumber) {
            // Normalize phone number format
            String normalizedPhone = normalizePhoneNumber(phoneNumber);
            this.phoneNumber = normalizedPhone;
            this.partyA = normalizedPhone; // PartyA is same as PhoneNumber
            return this;
        }

        public StkPushRequestBuilder callBackURL(String callBackURL) {
            this.callBackURL = callBackURL;
            return this;
        }

        public StkPushRequestBuilder accountReference(String accountReference) {
            this.accountReference = accountReference;
            return this;
        }

        public StkPushRequestBuilder transactionDesc(String transactionDesc) {
            this.transactionDesc = transactionDesc;
            return this;
        }

        /**
         * Builds the StkPushRequest instance.
         *
         * @return a new StkPushRequest
         * @throws IllegalStateException if required fields are missing
         */
        public StkPushRequest build() {
            validateRequiredFields();
            return new StkPushRequest(
                businessShortCode, password, timestamp, transactionType,
                amount, partyA, partyB, phoneNumber, callBackURL,
                accountReference, transactionDesc
            );
        }

        private void validateRequiredFields() {
            if (businessShortCode == null || businessShortCode.trim().isEmpty()) {
                throw new IllegalStateException("Business short code is required");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalStateException("Password is required");
            }
            if (timestamp == null || timestamp.trim().isEmpty()) {
                throw new IllegalStateException("Timestamp is required");
            }
            if (amount == null || amount.trim().isEmpty()) {
                throw new IllegalStateException("Amount is required");
            }
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                throw new IllegalStateException("Phone number is required");
            }
            if (callBackURL == null || callBackURL.trim().isEmpty()) {
                throw new IllegalStateException("Callback URL is required");
            }

            // Validate callback URL format and accessibility
            CallbackUrlValidator.ValidationResult urlValidation = CallbackUrlValidator.validate(callBackURL);
            if (!urlValidation.isValid()) {
                throw new IllegalStateException("Invalid callback URL: " + urlValidation.getMessage());
            }
            if (accountReference == null || accountReference.trim().isEmpty()) {
                throw new IllegalStateException("Account reference is required");
            }
            if (transactionDesc == null || transactionDesc.trim().isEmpty()) {
                throw new IllegalStateException("Transaction description is required");
            }
        }

        private String normalizePhoneNumber(String phoneNumber) {
            if (phoneNumber == null) return null;

            // Remove any spaces, dashes, or other non-digit characters except +
            String cleaned = phoneNumber.replaceAll("[^+\\d]", "");

            // Handle different formats
            if (cleaned.startsWith("+254")) {
                return cleaned.substring(1); // Remove +, keep 254XXXXXXXXX
            } else if (cleaned.startsWith("254")) {
                return cleaned; // Already in correct format
            } else if (cleaned.startsWith("0") && cleaned.length() == 10) {
                return "254" + cleaned.substring(1); // Convert 0XXXXXXXXX to 254XXXXXXXXX
            } else if (cleaned.length() == 9) {
                return "254" + cleaned; // Convert XXXXXXXXX to 254XXXXXXXXX
            }

            return cleaned; // Return as-is if format is unclear
        }
    }
}