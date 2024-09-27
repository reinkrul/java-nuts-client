package nl.reinkrul.nuts.common;

import com.danubetech.verifiablecredentials.jwt.JwtVerifiableCredential;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public class VerifiableCredentialDeserializer extends StdDeserializer<VerifiableCredential> {

    public VerifiableCredentialDeserializer() {
        super(VerifiableCredential.class);
    }

    @Override
    public VerifiableCredential deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        if (jsonParser.getCurrentToken().isScalarValue()) {
            try {
                final String valueAsString = jsonParser.getValueAsString();
                JwtVerifiableCredential jwtVC = JwtVerifiableCredential.fromCompactSerialization(valueAsString);
                Map<String, Object> claims = jwtVC.getPayload().getClaims();
                VerifiableCredential vc = new VerifiableCredential(jwtVC.getPayloadObject().getJsonObject(), "\"" + valueAsString + "\"");
                if (vc.getId() == null) {
                    // Take from jti claim
                    vc.setJsonObjectKeyValue("id", (String) claims.get("jti"));
                }
                return vc;
            } catch (ParseException e) {
                throw new IOException(e);
            }
        }
        if (jsonParser.getCurrentToken().isStructStart()) {
            var asString = jsonParser.readValueAsTree().toString();
            return new VerifiableCredential(VerifiableCredential.fromJson(asString).getJsonObject(), asString);
        }
        throw new IOException("Unexpected token: " + jsonParser.getCurrentToken().toString());
    }
}
