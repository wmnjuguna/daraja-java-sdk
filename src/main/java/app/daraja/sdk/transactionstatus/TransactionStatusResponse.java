package app.daraja.sdk.transactionstatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionStatusResponse(
    @JsonProperty("ConversationID")
    String conversationID,

    @JsonProperty("OriginatorConversationID")
    String originatorConversationID,

    @JsonProperty("ResponseCode")
    String responseCode,

    @JsonProperty("ResponseDescription")
    String responseDescription
) {
}
