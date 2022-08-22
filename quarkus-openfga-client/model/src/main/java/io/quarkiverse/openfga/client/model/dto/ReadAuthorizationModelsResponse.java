package io.quarkiverse.openfga.client.model.dto;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.openfga.client.model.AuthorizationModel;
import io.quarkiverse.openfga.client.model.utils.Preconditions;

public final class ReadAuthorizationModelsResponse {
    @JsonProperty("authorization_models")
    private final List<AuthorizationModel> authorizationModels;
    @JsonProperty("continuation_token")
    @Nullable
    private final String continuationToken;

    public ReadAuthorizationModelsResponse(@JsonProperty("authorization_models") List<AuthorizationModel> authorizationModels,
            @JsonProperty("continuation_token") @Nullable String continuationToken) {
        this.authorizationModels = Preconditions.parameterNonNull(authorizationModels, "authorizationModels");
        this.continuationToken = continuationToken;
    }

    @JsonProperty("authorization_models")
    public List<AuthorizationModel> getAuthorizationModels() {
        return authorizationModels;
    }

    @JsonProperty("continuation_token")
    @Nullable
    public String getContinuationToken() {
        return continuationToken;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (ReadAuthorizationModelsResponse) obj;
        return Objects.equals(this.authorizationModels, that.authorizationModels) &&
                Objects.equals(this.continuationToken, that.continuationToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationModels, continuationToken);
    }

    @Override
    public String toString() {
        return "ReadAuthorizationModelsResponse[" +
                "authorizationModels=" + authorizationModels + ", " +
                "continuationToken=" + continuationToken + ']';
    }

}
