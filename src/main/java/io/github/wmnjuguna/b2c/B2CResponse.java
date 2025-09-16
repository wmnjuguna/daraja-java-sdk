package io.github.wmnjuguna.b2c;

import com.fasterxml.jackson.annotation.JsonProperty;

public record B2CResponse(
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
