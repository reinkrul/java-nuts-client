package v6;

import nl.reinkrul.nuts.ApiException;
import nl.reinkrul.nuts.vcr.CredentialApi;
import nl.reinkrul.nuts.vcr.IssueVCRequest;
import nl.reinkrul.nuts.vcr.IssueVCRequestType;

import java.util.Map;

public class CredentialExamples {

    public void issueNutsOrganizationCredential() throws ApiException {
        var client = new CredentialApi();
        var issuedVC = client.issueVC(new IssueVCRequest()
                // General VC properties
                .type(new IssueVCRequestType("NutsOrganizationCredential"))
                .issuer("did:web:example.com:issuer") // the DID of the issuer of the credential (DID of software vendor)
                .withStatusList2021Revocation(true) // enable revocation
                // Subject of the credential
                .credentialSubject(
                        Map.of(
                                "id", "did:web:example.com:holder",
                                "organization", Map.of(
                                        "name", "Extra Careful B.V.",
                                        "city", "Zorgdorp"
                                )
                        )
                )
        );

        System.out.println("VC has been issued, ID: " + issuedVC.getId());
    }
}
