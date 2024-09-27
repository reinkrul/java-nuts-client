package nl.reinkrul.nuts.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class VerifiableCredential extends com.danubetech.verifiablecredentials.VerifiableCredential {
    public VerifiableCredential(Map<String, Object> jsonObject, String source) {
        super(jsonObject);
        this.source = source;
    }

    public final String source;

    public VerifiableCredential(com.danubetech.verifiablecredentials.VerifiableCredential employeeCredential) {
        super(employeeCredential.getJsonObject());
        try {
            source = new ObjectMapper().writeValueAsString(employeeCredential);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
