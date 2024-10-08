package nl.reinkrul.nuts;

import nl.reinkrul.nuts.auth.v2.AuthApi;
import nl.reinkrul.nuts.auth.v2.ServiceAccessTokenRequest;
import nl.reinkrul.nuts.auth.v2.TokenIntrospectionResponse;
import nl.reinkrul.nuts.auth.v2.TokenResponse;
import nl.reinkrul.nuts.credentials.NutsEmployeeCredential;
import nl.reinkrul.nuts.discovery.DiscoveryApi;
import nl.reinkrul.nuts.discovery.ServiceActivationRequest;
import nl.reinkrul.nuts.vcr.CredentialApi;
import nl.reinkrul.nuts.vcr.IssueVCRequest;
import nl.reinkrul.nuts.vcr.IssueVCRequestContext;
import nl.reinkrul.nuts.vcr.IssueVCRequestType;
import nl.reinkrul.nuts.vdr.CreateSubjectOptions;
import nl.reinkrul.nuts.vdr.SubjectApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

// To run this integration test, start the Docker Compose file in the nutsnode directory.
public class IntegrationTest {

    //@Test
    public void createSubjectIssueVC() throws ApiException {
        var apiClient = Configuration.getDefaultApiClient();
        apiClient.setBasePath("http://localhost:8081");

        var subjectApi = new SubjectApi(apiClient);
        var authApi = new AuthApi(apiClient);
        var discoveryApi = new DiscoveryApi(apiClient);

        // Admin: Create subject
        var subjectCreationResult = subjectApi.createSubject(new CreateSubjectOptions());
        System.out.println(subjectCreationResult.getSubject());
        var subjectDID = subjectCreationResult.getDocuments().get(0).getId();
        var credentialApi = new CredentialApi(apiClient);

        // Admin: Issue VC
        var nutsUraCredential = credentialApi.issueVC(new IssueVCRequest()
                .atContext(new IssueVCRequestContext("https://nuts.nl/credentials/2024"))
                .type(new IssueVCRequestType("NutsUraCredential"))
                .issuer(subjectDID.toString())
                .withStatusList2021Revocation(false)
                .publishToNetwork(null)
                .visibility(null)
                .format(IssueVCRequest.FormatEnum.JWT_VC)
                .credentialSubject(
                        Map.of(
                                "id", subjectDID.toString(),
                                "organization", Map.of(
                                        "ura", "12345",
                                        "name", "Extra Careful B.V.",
                                        "city", "Zorgdorp"
                                )
                        )
                )
        );
        Assertions.assertNotNull(nutsUraCredential.getId());

        // Load it into wallet
        var subjectID = subjectCreationResult.getSubject();
        credentialApi.loadVC(subjectID, nutsUraCredential);

        // List it
        var vcs = credentialApi.getCredentialsInWallet(subjectID);
        Assertions.assertEquals(1, vcs.size());
        Assertions.assertEquals(nutsUraCredential.source, vcs.get(0).source);

        // Application: Request Access Token
        var employeeCredential = new NutsEmployeeCredential(
                "12345",
                "E",
                "Careful",
                "Caregiver"
        );
        TokenResponse accessTokenResponse = authApi.requestServiceAccessToken(subjectID, new ServiceAccessTokenRequest()
                .tokenType(ServiceAccessTokenRequest.TokenTypeEnum.DPOP)
                .addCredentialsItem(employeeCredential.getCredential())
                .scope("test")
                .authorizationServer("http://localhost:8080/oauth2/" + subjectID)
        );

        // PEP: Check access token
        TokenIntrospectionResponse tokenIntrospectionResponse = authApi.introspectAccessToken(accessTokenResponse.getAccessToken());
        Assertions.assertTrue(tokenIntrospectionResponse.getActive());
        Assertions.assertEquals("12345", tokenIntrospectionResponse.getAdditionalProperty("organization_ura"));
        Assertions.assertEquals(employeeCredential.identifier, tokenIntrospectionResponse.getAdditionalProperty("employee_identifier"));
        Assertions.assertEquals(employeeCredential.role, tokenIntrospectionResponse.getAdditionalProperty("employee_role"));
        Assertions.assertEquals(employeeCredential.initials, tokenIntrospectionResponse.getAdditionalProperty("employee_initials"));
        Assertions.assertEquals(employeeCredential.name, tokenIntrospectionResponse.getAdditionalProperty("employee_name"));

        // Application: Register on Discovery Service
        discoveryApi.activateServiceForSubject("test", subjectID, new ServiceActivationRequest()
                .registrationParameters(Map.of("fhir-url", "https://example.com/fhir"))
        );

        // Application: Search on Discovery Service
        var services = discoveryApi.searchPresentations("test", Map.of("credentialSubject.organization.ura", "12345"));
        Assertions.assertNotEquals(0, services.size());
    }
}
