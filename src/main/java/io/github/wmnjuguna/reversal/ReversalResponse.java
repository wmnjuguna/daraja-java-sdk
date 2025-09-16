package io.github.wmnjuguna.reversal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReversalResponse(
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
