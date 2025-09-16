package io.github.wmnjuguna.b2b;

import com.fasterxml.jackson.annotation.JsonProperty;

public record B2BRequest(
    @JsonProperty("Initiator")
    String initiator,

    @JsonProperty("SecurityCredential")
    String securityCredential,

    @JsonProperty("CommandID")
    String commandID,

    @JsonProperty("SenderIdentifierType")
    String senderIdentifierType,

    @JsonProperty("RecieverIdentifierType")
    String recieverIdentifierType,

    @JsonProperty("Amount")
    String amount,

    @JsonProperty("PartyA")
    String partyA,

    @JsonProperty("PartyB")
    String partyB,

    @JsonProperty("Remarks")
    String remarks,

    @JsonProperty("QueueTimeOutURL")
    String queueTimeOutURL,

    @JsonProperty("ResultURL")
    String resultURL,

    @JsonProperty("AccountReference")
    String accountReference
) {
}
