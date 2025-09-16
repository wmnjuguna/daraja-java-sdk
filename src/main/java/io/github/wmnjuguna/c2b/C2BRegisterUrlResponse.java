package io.github.wmnjuguna.c2b;

import com.fasterxml.jackson.annotation.JsonProperty;

public record C2BRegisterUrlResponse(
    @JsonProperty("ConversationID")
    String conversationID,

    @JsonProperty("OriginatorConversationID")
    String originatorConversationID,

    @JsonProperty("ResponseDescription")
    String responseDescription
) {
}
