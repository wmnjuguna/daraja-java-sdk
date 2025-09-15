package app.daraja.sdk.accountbalance;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountBalanceResponse(
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
