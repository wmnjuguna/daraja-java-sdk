package app.daraja.sdk;

import app.daraja.sdk.b2b.B2BRequest;
import app.daraja.sdk.b2b.B2BResponse;
import app.daraja.sdk.reversal.ReversalRequest;
import app.daraja.sdk.reversal.ReversalResponse;
import app.daraja.sdk.accountbalance.AccountBalanceRequest;
import app.daraja.sdk.accountbalance.AccountBalanceResponse;
import app.daraja.sdk.transactionstatus.TransactionStatusRequest;
import app.daraja.sdk.transactionstatus.TransactionStatusResponse;
import app.daraja.sdk.b2c.B2CRequest;
import app.daraja.sdk.b2c.B2CResponse;
import app.daraja.sdk.c2b.C2BRegisterUrlRequest;
import app.daraja.sdk.c2b.C2BRegisterUrlResponse;
import app.daraja.sdk.stkpush.StkPushRequest;
import app.daraja.sdk.stkpush.StkPushResponse;
import feign.Headers;
import feign.RequestLine;

/**
 * Main public interface for Daraja API operations.
 * Provides methods for STK Push, C2B, B2C, and other Daraja services.
 *
 * This interface is implemented by Feign and should not be implemented directly.
 * Use DarajaClientFactory to create instances of this interface.
 */
public interface DarajaApiClient {

    /**
     * Initiates an STK Push (M-Pesa Express) payment request.
     * Sends a payment prompt to the customer's phone for authorization.
     *
     * @param request the STK Push request containing payment details
     * @return StkPushResponse containing the request status and tracking IDs
     */
    @RequestLine("POST /mpesa/stkpush/v1/processrequest")
    @Headers({
        "Content-Type: application/json",
        "Accept: application/json"
    })
    StkPushResponse initiateStkPush(StkPushRequest request);

    /**
     * Registers the C2B validation and confirmation URLs.
     *
     * @param request the C2B URL registration request
     * @return C2BRegisterUrlResponse containing the registration status
     */
    @RequestLine("POST /mpesa/c2b/v1/registerurl")
    @Headers({
        "Content-Type: application/json",
        "Accept: application/json"
    })
    C2BRegisterUrlResponse registerC2BUrls(C2BRegisterUrlRequest request);

    /**
     * Initiates a B2C (Business to Customer) payment.
     *
     * @param request the B2C payment request
     * @return B2CResponse containing the payment status
     */
    @RequestLine("POST /mpesa/b2c/v1/paymentrequest")
    @Headers({
        "Content-Type: application/json",
        "Accept: application/json"
    })
    B2CResponse b2cPayment(B2CRequest request);

    /**
     * Initiates a B2B (Business to Business) payment.
     *
     * @param request the B2B payment request
     * @return B2BResponse containing the payment status
     */
    @RequestLine("POST /mpesa/b2b/v1/paymentrequest")
    @Headers({
        "Content-Type: application/json",
        "Accept: application/json"
    })
    B2BResponse b2bPayment(B2BRequest request);


    /**
     * Queries the status of a transaction.
     *
     * @param request the transaction status request
     * @return TransactionStatusResponse containing the transaction status
     */
    @RequestLine("POST /mpesa/transactionstatus/v1/query")
    @Headers({
        "Content-Type: application/json",
        "Accept: application/json"
    })
    TransactionStatusResponse transactionStatus(TransactionStatusRequest request);

    /**
     * Queries the balance of an M-Pesa account.
     *
     * @param request the account balance request
     * @return AccountBalanceResponse containing the account balance
     */
    @RequestLine("POST /mpesa/accountbalance/v1/query")
    @Headers({
        "Content-Type: application/json",
        "Accept: application/json"
    })
    AccountBalanceResponse accountBalance(AccountBalanceRequest request);

    /**
     * Reverses a transaction.
     *
     * @param request the reversal request
     * @return ReversalResponse containing the reversal status
     */
    @RequestLine("POST /mpesa/reversal/v1/request")
    @Headers({
        "Content-Type: application/json",
        "Accept: application/json"
    })
    ReversalResponse reversal(ReversalRequest request);

}