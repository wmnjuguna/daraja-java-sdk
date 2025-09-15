package app.daraja.sdk.c2b;

import com.fasterxml.jackson.annotation.JsonProperty;

public record C2BRegisterUrlRequest(
    @JsonProperty("ShortCode")
    String shortCode,

    @JsonProperty("ResponseType")
    String responseType,

    @JsonProperty("ConfirmationURL")
    String confirmationURL,

    @JsonProperty("ValidationURL")
    String validationURL
) {
}
