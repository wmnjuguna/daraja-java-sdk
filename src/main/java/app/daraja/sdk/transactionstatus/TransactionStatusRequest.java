package app.daraja.sdk.transactionstatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionStatusRequest(
    @JsonProperty("Initiator")
    String initiator,

    @JsonProperty("SecurityCredential")
    String securityCredential,

    @JsonProperty("CommandID")
    String commandID,

    @JsonProperty("TransactionID")
    String transactionID,

    @JsonProperty("PartyA")
    String partyA,

    @JsonProperty("IdentifierType")
    String identifierType,

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
