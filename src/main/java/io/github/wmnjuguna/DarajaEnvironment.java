package io.github.wmnjuguna;

/**
 * Enumeration representing the different Daraja API environments.
 * Provides easy switching between sandbox and production environments.
 * All endpoint paths are standardized as per Daraja API documentation.
 */
public enum DarajaEnvironment {

    /**
     * Sandbox environment for testing and development.
     */
    SANDBOX("https://sandbox.safaricom.co.ke"),

    /**
     * Production environment for live transactions.
     */
    PRODUCTION("https://api.safaricom.co.ke");

    private final String baseUrl;

    /**
     * Constructs a DarajaEnvironment with the specified base URL.
     *
     * @param baseUrl the base URL for the environment
     */
    DarajaEnvironment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Gets the base URL for this environment.
     *
     * @return the base URL as a string
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Gets the OAuth endpoint URL for this environment.
     *
     * @return the complete OAuth endpoint URL
     */
    public String getOAuthUrl() {
        return baseUrl + "/oauth/v1/generate";
    }

    /**
     * Gets the OAuth endpoint URL for this environment with grant type.
     *
     * @return the complete OAuth endpoint URL with grant type parameter
     */
    public String getAuthUrl() {
        return baseUrl + "/oauth/v1/generate?grant_type=client_credentials";
    }

    /**
     * Gets the STK Push endpoint URL for this environment.
     *
     * @return the complete STK Push endpoint URL
     */
    public String getStkPushUrl() {
        return baseUrl + "/mpesa/stkpush/v1/processrequest";
    }

    /**
     * Gets the C2B register URL endpoint for this environment.
     *
     * @return the complete C2B register URL endpoint
     */
    public String getC2BRegisterUrl() {
        return baseUrl + "/mpesa/c2b/v1/registerurl";
    }

    /**
     * Gets the transaction status endpoint URL for this environment.
     *
     * @return the complete transaction status endpoint URL
     */
    public String getTransactionStatusUrl() {
        return baseUrl + "/mpesa/transactionstatus/v1/query";
    }

    /**
     * Gets the account balance endpoint URL for this environment.
     *
     * @return the complete account balance endpoint URL
     */
    public String getAccountBalanceUrl() {
        return baseUrl + "/mpesa/accountbalance/v1/query";
    }

    /**
     * Gets the B2C endpoint URL for this environment.
     *
     * @return the complete B2C endpoint URL
     */
    public String getB2CUrl() {
        return baseUrl + "/mpesa/b2c/v1/paymentrequest";
    }

    /**
     * Gets the STK Push query endpoint URL for this environment.
     *
     * @return the complete STK Push query endpoint URL
     */
    public String getStkPushQueryUrl() {
        return baseUrl + "/mpesa/stkpushquery/v1/query";
    }

    /**
     * Gets the C2B simulate endpoint URL for this environment.
     *
     * @return the complete C2B simulate endpoint URL
     */
    public String getC2BSimulateUrl() {
        return baseUrl + "/mpesa/c2b/v1/simulate";
    }

    /**
     * Gets the reversal endpoint URL for this environment.
     *
     * @return the complete reversal endpoint URL
     */
    public String getReversalUrl() {
        return baseUrl + "/mpesa/reversal/v1/request";
    }
}