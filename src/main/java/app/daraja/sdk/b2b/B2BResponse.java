package app.daraja.sdk.b2b;

import com.fasterxml.jackson.annotation.JsonProperty;

public record B2BResponse(
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
