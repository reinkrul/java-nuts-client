package nl.reinkrul.nuts.common;

import com.danubetech.verifiablecredentials.jwt.JwtVerifiablePresentation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;

public class VerifiablePresentationDeserializer extends StdDeserializer<VerifiablePresentation> {

    public VerifiablePresentationDeserializer() {
        super(VerifiablePresentation.class);
    }

    @Override
    public VerifiablePresentation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var token = jsonParser.nextValue();
        String valueAsString = jsonParser.getValueAsString();
        if (token == JsonToken.START_OBJECT) {
            // Parse as JSON-LD
            return new VerifiablePresentation(com.danubetech.verifiablecredentials.VerifiablePresentation.fromJson(valueAsString).getJsonObject(), valueAsString);
        } else if (token == JsonToken.VALUE_STRING) {
            try {
                return new VerifiablePresentation(JwtVerifiablePresentation.fromCompactSerialization(token.asString()).getPayloadObject().getJsonObject(), valueAsString);
            } catch (ParseException e) {
                throw new IOException(e);
            }
        }
        throw new IOException("Unexpected token: " + token);
    }
}
