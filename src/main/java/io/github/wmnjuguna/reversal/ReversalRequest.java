package io.github.wmnjuguna.reversal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReversalRequest(
    @JsonProperty("Initiator")
    String initiator,

    @JsonProperty("SecurityCredential")
    String securityCredential,

    @JsonProperty("CommandID")
    String commandID,

    @JsonProperty("TransactionID")
    String transactionID,

    @JsonProperty("Amount")
    String amount,

    @JsonProperty("ReceiverParty")
    String receiverParty,

    @JsonProperty("RecieverIdentifierType")
    String recieverIdentifierType,

    @JsonProperty("ResultURL")
    String resultURL,

    @JsonProperty("QueueTimeOutURL")
    String queueTimeOutURL,

    @JsonProperty("Remarks")
    String remarks,

    @JsonProperty("Occasion")
    String occasion
) {
}
