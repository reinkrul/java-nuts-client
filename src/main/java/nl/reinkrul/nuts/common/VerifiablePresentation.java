package nl.reinkrul.nuts.common;

import java.util.Map;

public class VerifiablePresentation extends com.danubetech.verifiablecredentials.VerifiablePresentation {
    public VerifiablePresentation(Map<String, Object> jsonObject, String source) {
        super(jsonObject);
        this.source = source;
    }

    /**
     * The raw source of the presentation, as it was deserialized.
     */
    public final String source;
}
