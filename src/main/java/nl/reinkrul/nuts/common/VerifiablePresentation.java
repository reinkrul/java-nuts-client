package nl.reinkrul.nuts.common;

import java.util.Map;

public class VerifiablePresentation extends com.danubetech.verifiablecredentials.VerifiablePresentation {
    public VerifiablePresentation(Map<String, Object> jsonObject, String source) {
        super(jsonObject);
        this.source = source;
    }

    public final String source;
}
