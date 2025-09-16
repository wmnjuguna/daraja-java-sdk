package io.github.wmnjuguna.accountbalance;

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
