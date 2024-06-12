package nl.reinkrul.nuts;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.databind.module.SimpleModule;
import nl.reinkrul.nuts.common.VerifiableCredentialDeserializer;
import nl.reinkrul.nuts.common.VerifiablePresentationDeserializer;

public class Configuration {

    private static ApiClient defaultApiClient = create();

    /**
     * Get the default API client, which would be used when creating API
     * instances without providing an API client.
     *
     * @return Default API client
     */
    public static ApiClient getDefaultApiClient() {
        return defaultApiClient;
    }

    /**
     * Set the default API client, which would be used when creating API
     * instances without providing an API client.
     *
     * @param apiClient API client
     */
    public static void setDefaultApiClient(ApiClient apiClient) {
        defaultApiClient = apiClient;
    }

    public static ApiClient create() {
        var result = new ApiClient();
        result.setUserAgent("nuts-java-client");
        var module = new SimpleModule();
        module.addDeserializer(VerifiableCredential.class, new VerifiableCredentialDeserializer(VerifiableCredential.class));
        module.addDeserializer(VerifiablePresentation.class, new VerifiablePresentationDeserializer(VerifiablePresentation.class));
        result.getJSON().getMapper().registerModule(module);
        return result;
    }
}
