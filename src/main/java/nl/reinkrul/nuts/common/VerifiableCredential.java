package nl.reinkrul.nuts.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class VerifiableCredential extends com.danubetech.verifiablecredentials.VerifiableCredential {
    public VerifiableCredential(Map<String, Object> jsonObject, String source) {
        super(jsonObject);
        this.source = source;
    }

    /**
     * The raw source of the credential, as it was deserialized.
     */
    public final String source;
}
