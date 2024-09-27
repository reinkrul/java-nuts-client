package nl.reinkrul.nuts.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class VerifiablePresentationSerializer extends StdSerializer<VerifiablePresentation> {

    public VerifiablePresentationSerializer() {
        super(VerifiablePresentation.class);
    }

    @Override
    public void serialize(VerifiablePresentation value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if ("".equals(value.source)) {
            throw new IOException("Can only unmarshal VerifiablePresentation which has been deserialized earlier.");
        }
        gen.writeRawValue(value.source);
    }
}