package io.github.wmnjuguna.accountbalance;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountBalanceRequest(
    @JsonProperty("Initiator")
    String initiator,

    @JsonProperty("SecurityCredential")
    String securityCredential,

    @JsonProperty("CommandID")
    String commandID,

    @JsonProperty("PartyA")
    String partyA,

    @JsonProperty("IdentifierType")
    String identifierType,

    @JsonProperty("Remarks")
    String remarks,

    @JsonProperty("QueueTimeOutURL")
    String queueTimeOutURL,

    @JsonProperty("ResultURL")
    String resultURL
) {
}
