package nl.reinkrul.nuts.common;

import com.danubetech.verifiablecredentials.jwt.JwtVerifiableCredential;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.ParseException;

public class VerifiableCredentialSerializer extends StdSerializer<VerifiableCredential> {

    public VerifiableCredentialSerializer() {
        super(VerifiableCredential.class);
    }

    @Override
    public void serialize(VerifiableCredential value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if ("".equals(value.source)) {
            throw new IOException("Can only unmarshal VerifiableCredential which has been deserialized earlier.");
        }
        gen.writeRawValue(value.source);
    }
}
