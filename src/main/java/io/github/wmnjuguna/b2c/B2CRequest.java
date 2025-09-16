package io.github.wmnjuguna.b2c;

import com.fasterxml.jackson.annotation.JsonProperty;

public record B2CRequest(
    @JsonProperty("InitiatorName")
    String initiatorName,

    @JsonProperty("SecurityCredential")
    String securityCredential,

    @JsonProperty("CommandID")
    String commandID,

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

    @JsonProperty("Occassion")
    String occassion
) {
}
