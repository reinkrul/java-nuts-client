package nl.reinkrul.nuts.common;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.danubetech.verifiablecredentials.jwt.JwtVerifiableCredential;
import com.danubetech.verifiablecredentials.jwt.JwtVerifiablePresentation;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

public class VerifiablePresentationDeserializer extends StdDeserializer<VerifiablePresentation> {

    public VerifiablePresentationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public VerifiablePresentation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var token = jsonParser.nextValue();
        if (token == JsonToken.START_OBJECT) {
            // Parse as JSON-LD
            return com.danubetech.verifiablecredentials.VerifiablePresentation.fromJson(new InputStreamReader(new ByteArrayInputStream(token.asByteArray())));
        } else if (token == JsonToken.VALUE_STRING) {
            try {
                return JwtVerifiablePresentation.fromCompactSerialization(token.asString()).getPayloadObject();
            } catch (ParseException e) {
                throw new IOException(e);
            }
        }
        throw new IOException("Unexpected token: " + token);
    }
}
