package app.daraja.sdk.webhook;

import app.daraja.sdk.stkpush.StkPushCallback;

/**
 * Interface for handling STK Push callback notifications from Safaricom.
 * Implement this interface to process payment completion events.
 */
public interface StkPushCallbackHandler {

    /**
     * Called when a successful STK Push payment is completed.
     *
     * @param callback the callback data from Safaricom
     */
    void onPaymentSuccess(StkPushCallback callback);

    /**
     * Called when an STK Push payment fails or is cancelled.
     *
     * @param callback the callback data from Safaricom
     */
    void onPaymentFailure(StkPushCallback callback);

    /**
     * Called for any callback received, regardless of success/failure.
     * This method is called before onPaymentSuccess or onPaymentFailure.
     *
     * @param callback the callback data from Safaricom
     */
    default void onCallbackReceived(StkPushCallback callback) {
        // Default implementation does nothing
        // Override if you need to log or process all callbacks
    }
}