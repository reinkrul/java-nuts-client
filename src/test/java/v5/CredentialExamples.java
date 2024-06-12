package v5;

import com.danubetech.verifiablecredentials.CredentialSubject;
import com.danubetech.verifiablecredentials.VerifiableCredential;
import nl.reinkrul.nuts.ApiException;
import nl.reinkrul.nuts.extra.*;
import nl.reinkrul.nuts.vcr.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class CredentialExamples {

    public void searchNutsOrganizationCredential() throws ApiException, URISyntaxException {
        var client = new CredentialApi();
        var query = VerifiableCredential.builder()
                .type("NutsOrganizationCredential")
                .contexts(List.of(new URI("https://nuts.nl/credentials/v1"), new URI("https://www.w3.org/2018/credentials/v1")))
                .issuer(new URI("did:nuts:some-did")) // the DID of the issuer of the credential (DID of software vendor)
                .credentialSubject(
                        CredentialSubject.builder()
                                .id(new URI("did:nuts:some-other-did")) // the DID of the receiver of the credential
                                .claims(Map.of("organization", Map.of(
                                        "name", "Extra Careful B.V.",
                                        "city", "Zorgdorp"
                                )))
                                .build()
                ).build();
        var issuedVC = client.searchVCs(new SearchVCRequest()
                // Search options
                .searchOptions(new SearchOptions().allowUntrustedIssuer(false))
                // VC to match
                .query(query)
        );

        for (SearchVCResult result : issuedVC.getVerifiableCredentials()) {
            System.out.println(result.getVerifiableCredential().getId());
        }
    }

    public void issueNutsOrganizationCredential() throws ApiException {
        var client = new CredentialApi();
        var issuedVC = client.issueVC(new IssueVCRequest()
                // General VC properties
                .type(new IssueVCRequestType("NutsAuthenticationCredential"))
                .issuer("did:nuts:some-did") // the DID of the issuer of the credential (DID of software vendor)
                .visibility(IssueVCRequest.VisibilityEnum.PUBLIC) // publish on network, anyone can read it
                // Subject of the credential
                .credentialSubject(
                        new NutsOrganizationCredential()
                                .id("did:nuts:some-other-did") // the DID of the receiver of the credential
                                .organization(new Organization()
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
                .type(new IssueVCRequestType("NutsAuthenticationCredential"))
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
                                                .userContext(true)
                                                .assuranceLevel(FHIRResource.AssuranceLevelEnum.LOW)
                                                .addOperationsItem("read")
                                )
                )
        );

        System.out.println("VC has been issued, ID: " + issuedVC.getId());
    }

    public void issueNutsEmployeeCredential() throws ApiException {
        var client = new CredentialApi();
        var issuedVC = client.issueVC(new IssueVCRequest()
                // General VC properties
                .type(new IssueVCRequestType("NutsAuthenticationCredential"))
                .issuer("did:nuts:some-did") // the DID of the issuer of the credential
                // Subject of the credential
                .credentialSubject(
                        new NutsEmployeeCredential()
                                .id("did:nuts:some-did") // the DID of the receiver of the credential, equal to the issuer for NutsEmployeeCredential
                                .type(NutsEmployeeCredential.TypeEnum.ORGANIZATION) // hardcoded
                                .member(
                                        new OrganizationMember()
                                                .identifier("12345678")
                                                .type(OrganizationMember.TypeEnum.EMPLOYEEROLE) // hardcoded
                                                .roleName("Verpleegkundige niveau 2") // optional
                                                .member(
                                                        new OrganizationMemberMember()
                                                                .type(OrganizationMemberMember.TypeEnum.PERSON) // hardcoded
                                                                .initials("A.B.")
                                                                .familyName("van der Zorg")
                                                )

                                )
                )
        );

        System.out.println("VC has been issued, ID: " + issuedVC.getId());
    }
}
