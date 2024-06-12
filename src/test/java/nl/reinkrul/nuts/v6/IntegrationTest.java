package nl.reinkrul.nuts.v6;

import nl.reinkrul.nuts.ApiException;
import nl.reinkrul.nuts.Configuration;
import nl.reinkrul.nuts.vcr.CredentialApi;
import nl.reinkrul.nuts.vcr.IssueVCRequest;
import nl.reinkrul.nuts.vcr.IssueVCRequestType;
import nl.reinkrul.nuts.vdr.v2.CreateDIDOptions;
import nl.reinkrul.nuts.vdr.v2.DidApi;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IntegrationTest {

    @Test
    public void createDIDDocumentIssueVC() throws ApiException {
        var apiClient = Configuration.getDefaultApiClient();
        apiClient.setBasePath("http://localhost:8081");

        var didApi = new DidApi(apiClient);
        var didDocument = didApi.createDID(new CreateDIDOptions());

        var credentialApi = new CredentialApi(apiClient);
        var issuedVC = credentialApi.issueVC(new IssueVCRequest()
                .type(new IssueVCRequestType("EmployeeCredential"))
                .issuer(didDocument.getId().toString())
                .withStatusList2021Revocation(true)
                .publishToNetwork(null)
                .visibility(null)
                .format(IssueVCRequest.FormatEnum.JWT_VC)
                .credentialSubject(
                        Map.of(
                                "id", "did:jwk:1234",
                                "name", "John Doe",
                                "roleName", "Software Engineer",
                                "identifier", "1234"
                        )
                )
        );
        System.out.println(issuedVC.getId());
    }
}
