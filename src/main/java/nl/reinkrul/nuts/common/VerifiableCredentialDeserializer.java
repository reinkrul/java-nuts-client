package nl.reinkrul.nuts.common;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.jwt.JwtVerifiableCredential;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;

public class VerifiableCredentialDeserializer extends StdDeserializer<VerifiableCredential> {

    public VerifiableCredentialDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public VerifiableCredential deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        if (jsonParser.getCurrentToken().isScalarValue()) {
            try {
                return JwtVerifiableCredential.fromCompactSerialization(jsonParser.getValueAsString()).getPayloadObject();
            } catch (ParseException e) {
                throw new IOException(e);
            }
        }
        if (jsonParser.getCurrentToken().isStructStart()) {

        }
        throw new IOException("Unexpected token: " + jsonParser.getCurrentToken().toString());
    }
}
