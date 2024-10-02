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
        if (jsonParser.getCurrentToken().isScalarValue()) {
            var valueAsString = jsonParser.getValueAsString();
            try {
                return new VerifiablePresentation(JwtVerifiablePresentation.fromCompactSerialization(valueAsString).getPayloadObject().getJsonObject(), valueAsString);
            } catch (ParseException e) {
                throw new IOException(e);
            }
        }
        if (jsonParser.getCurrentToken().isStructStart()) {
            // Parse as JSON-LD
            var valueAsString = jsonParser.readValueAsTree().toString();
            return new VerifiablePresentation(com.danubetech.verifiablecredentials.VerifiablePresentation.fromJson(valueAsString).getJsonObject(), valueAsString);

        }
        throw new IOException("Unexpected token: " + jsonParser.getCurrentToken().id());
    }
}
