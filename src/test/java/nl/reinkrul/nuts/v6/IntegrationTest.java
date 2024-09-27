package nl.reinkrul.nuts.v6;

import com.danubetech.verifiablecredentials.CredentialSubject;
import nl.reinkrul.nuts.ApiException;
import nl.reinkrul.nuts.Configuration;
import nl.reinkrul.nuts.auth.v2.AuthApi;
import nl.reinkrul.nuts.auth.v2.ServiceAccessTokenRequest;
import nl.reinkrul.nuts.auth.v2.TokenIntrospectionResponse;
import nl.reinkrul.nuts.auth.v2.TokenResponse;
import nl.reinkrul.nuts.common.DIDDocument;
import nl.reinkrul.nuts.common.VerifiableCredential;
import nl.reinkrul.nuts.vcr.CredentialApi;
import nl.reinkrul.nuts.vcr.IssueVCRequest;
import nl.reinkrul.nuts.vcr.IssueVCRequestContext;
import nl.reinkrul.nuts.vcr.IssueVCRequestType;
import nl.reinkrul.nuts.vdr.CreateSubjectOptions;
import nl.reinkrul.nuts.vdr.SubjectApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IntegrationTest {

    @Test
    public void createSubjectIssueVC() throws ApiException {
        var apiClient = Configuration.getDefaultApiClient();
        apiClient.setBasePath("http://localhost:8081");

        var subjectApi = new SubjectApi(apiClient);
        var authApi = new AuthApi(apiClient);

        // Create subject
        var subjectCreationResult = subjectApi.createSubject(new CreateSubjectOptions());
        System.out.println(subjectCreationResult.getSubject());
        var subjectDID = subjectCreationResult.getDocuments().get(0).getId();
        var credentialApi = new CredentialApi(apiClient);

        // Issue VC
        var nutsUraCredential = credentialApi.issueVC(new IssueVCRequest()
                .atContext(new IssueVCRequestContext("https://nuts.nl/credentials/2024"))
                .type(new IssueVCRequestType("NutsUraCredential"))
                .issuer(subjectDID.toString())
                .withStatusList2021Revocation(false)
                .publishToNetwork(null)
                .visibility(null)
                .format(IssueVCRequest.FormatEnum.LDP_VC)
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

        // Request Access Token
        com.danubetech.verifiablecredentials.VerifiableCredential employeeCredential = VerifiableCredential
                .builder()
                .credentialSubject(CredentialSubject
                        .builder()
                        .id(subjectDID)
                        .build()
                )
                .build();
        TokenResponse accessTokenResponse = authApi.requestServiceAccessToken(subjectID, new ServiceAccessTokenRequest()
                .tokenType(ServiceAccessTokenRequest.TokenTypeEnum.BEARER)
                .addCredentialsItem(new VerifiableCredential(employeeCredential))
                .scope("test")
                .authorizationServer("http://localhost:8080/oauth2/" + subjectID)
        );

        // Check access token
        TokenIntrospectionResponse tokenIntrospectionResponse = authApi.introspectAccessToken(accessTokenResponse.getAccessToken());
        Assertions.assertTrue(tokenIntrospectionResponse.getActive());
    }
}
