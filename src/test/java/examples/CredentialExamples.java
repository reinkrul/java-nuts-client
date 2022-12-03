package examples;

import nl.reinkrul.nuts.ApiException;
import nl.reinkrul.nuts.common.VerifiableCredential;
import nl.reinkrul.nuts.extra.FHIRResource;
import nl.reinkrul.nuts.extra.NutsAuthorizationCredential;
import nl.reinkrul.nuts.extra.NutsOrganizationCredential;
import nl.reinkrul.nuts.vcr.*;

import java.util.List;

public class CredentialExamples {

    public void searchNutsOrganizationCredential() throws ApiException {
        var client = new CredentialApi();
        var issuedVC = client.searchVCs(new SearchVCRequest()
                // Search options
                .searchOptions(new SearchOptions().allowUntrustedIssuer(false))
                // VC to match
                .query(new VerifiableCredential()
                        .type(List.of("VerifiableCredential", "NutsOrganizationCredential"))
                        .atContext(List.of("https://nuts.nl/credentials/v1", "https://www.w3.org/2018/credentials/v1"))
                        .issuer("did:nuts:some-did") // the DID of the issuer of the credential (DID of software vendor)
                        .credentialSubject(
                                new NutsOrganizationCredential()
                                        .organization(new nl.reinkrul.nuts.extra.Organization()
                                                .name("Extra Careful B.V.")
                                                .city("Zorgdorp")
                                        ))
                ));

        for (SearchVCResult result : issuedVC.getVerifiableCredentials()) {
            System.out.println(result.getVerifiableCredential().getId());
        }
    }

    public void issueNutsOrganizationCredential() throws ApiException {
        var client = new CredentialApi();
        var issuedVC = client.issueVC(new IssueVCRequest()
                // General VC properties
                .type("NutsOrganizationCredential")
                .issuer("did:nuts:some-did") // the DID of the issuer of the credential (DID of software vendor)
                .visibility(IssueVCRequest.VisibilityEnum.PUBLIC) // publish on network, anyone can read it
                // Subject of the credential
                .credentialSubject(
                        new NutsOrganizationCredential()
                                .id("did:nuts:some-other-did") // the DID of the receiver of the credential
                                .organization(new nl.reinkrul.nuts.extra.Organization()
                                        .name("Extra Careful B.V.")
                                        .city("Zorgdorp")
                                )
                )
        );

        System.out.println("VC has been issued, ID: " + issuedVC.getId());
    }

    public void issueNutsAuthorizationCredential() throws ApiException {
        var client = new CredentialApi();
        var issuedVC = client.issueVC(new IssueVCRequest()
                // General VC properties
                .type("NutsAuthenticationCredential")
                .issuer("did:nuts:some-did") // the DID of the issuer of the credential
                .visibility(IssueVCRequest.VisibilityEnum.PRIVATE) // publish on network, but keep it private
                // Subject of the credential
                .credentialSubject(
                        new NutsAuthorizationCredential()
                                .id("did:nuts:some-other-did") // the DID of the receiver of the credential
                                .subject("1234567890") // social security number of the patient
                                .addResourcesItem(
                                        // The FHIR resources that can be accessed using the credential
                                        new FHIRResource()
                                                .path("/Task/1")
                                                .addOperationsItem("read")
                                )
                )
        );

        System.out.println("VC has been issued, ID: " + issuedVC.getId());
    }
}
