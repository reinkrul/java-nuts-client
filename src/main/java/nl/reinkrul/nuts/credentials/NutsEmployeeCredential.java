package nl.reinkrul.nuts.credentials;

import com.danubetech.verifiablecredentials.CredentialSubject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.reinkrul.nuts.common.VerifiableCredential;

import java.net.URI;
import java.util.Map;

public class NutsEmployeeCredential {
    public final String identifier;
    public final String initials;
    public final String name;
    public final String role;

    public NutsEmployeeCredential(String identifier, String initials, String name, String role) {
        this.identifier = identifier;
        this.initials = initials;
        this.name = name;
        this.role = role;
    }

    public VerifiableCredential getCredential() {
        var inner = com.danubetech.verifiablecredentials.VerifiableCredential.builder()
                .context(Context.NutsV1)
                .type("NutsEmployeeCredential")
                .credentialSubject(CredentialSubject
                        .builder()
                        .type("Organization")
                        .claims(Map.of("member", Map.of(
                                "identifier", identifier,
                                "type", "EmployeeRole",
                                "roleName", role,
                                "member", Map.of(
                                        "type", "Person",
                                        "initials", initials,
                                        "familyName", name
                                )
                        )))
                        .build())
                .build();
        final String source;
        try {
            source = new ObjectMapper().writeValueAsString(inner);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new VerifiableCredential(inner.getJsonObject(), source);
    }
}
