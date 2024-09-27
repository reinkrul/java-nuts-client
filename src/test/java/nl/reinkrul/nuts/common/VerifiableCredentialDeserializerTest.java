package nl.reinkrul.nuts.common;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VerifiableCredentialDeserializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    public VerifiableCredentialDeserializerTest() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(nl.reinkrul.nuts.common.VerifiableCredential.class, new VerifiableCredentialDeserializer());
        mapper.registerModule(module);
    }

    @Test
    void deserializeJWT() throws IOException {
        var jwt = "\"eyJhbGciOiJFUzI1NiIsImtpZCI6ImRpZDp3ZWI6bnV0cy5ubDppYW06YmRhMDI0ZTMtYjk0My00ZDRkLTk0MjQtYTEwNjM3NmE1YmM3IzY4ODA2ZjU1LWVkMzEtNGQ3Yy1hNmMxLWJkYjc1YzIxMTc0NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkaWQ6d2ViOm51dHMubmw6aWFtOmJkYTAyNGUzLWI5NDMtNGQ0ZC05NDI0LWExMDYzNzZhNWJjNyIsImp0aSI6ImRpZDp3ZWI6bnV0cy5ubDppYW06YmRhMDI0ZTMtYjk0My00ZDRkLTk0MjQtYTEwNjM3NmE1YmM3IzlhMDRkMWM0LWE4YTItNDg1Zi1hMWJmLWVkNGVhMTQyNTdmOSIsIm5iZiI6MTcyNzQ0Mjg0Mywic3ViIjoiZGlkOndlYjpudXRzLm5sOmlhbTpiZGEwMjRlMy1iOTQzLTRkNGQtOTQyNC1hMTA2Mzc2YTViYzciLCJ2YyI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vbnV0cy5ubC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vdzNpZC5vcmcvdmMvc3RhdHVzLWxpc3QvMjAyMS92MSJdLCJjcmVkZW50aWFsU3RhdHVzIjpbeyJpZCI6Imh0dHBzOi8vbnV0cy5ubC9zdGF0dXNsaXN0L2RpZDp3ZWI6bnV0cy5ubDppYW06YmRhMDI0ZTMtYjk0My00ZDRkLTk0MjQtYTEwNjM3NmE1YmM3LzEjMCIsInR5cGUiOiJTdGF0dXNMaXN0MjAyMUVudHJ5Iiwic3RhdHVzUHVycG9zZSI6InJldm9jYXRpb24iLCJzdGF0dXNMaXN0SW5kZXgiOiIwIiwic3RhdHVzTGlzdENyZWRlbnRpYWwiOiJodHRwczovL251dHMubmwvc3RhdHVzbGlzdC9kaWQ6d2ViOm51dHMubmw6aWFtOmJkYTAyNGUzLWI5NDMtNGQ0ZC05NDI0LWExMDYzNzZhNWJjNy8xIn1dLCJjcmVkZW50aWFsU3ViamVjdCI6W3siaWQiOiJkaWQ6d2ViOm51dHMubmw6aWFtOmJkYTAyNGUzLWI5NDMtNGQ0ZC05NDI0LWExMDYzNzZhNWJjNyIsImlkZW50aWZpZXIiOiIxMjM0IiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZU5hbWUiOiJTb2Z0d2FyZSBFbmdpbmVlciJ9XSwidHlwZSI6WyJFbXBsb3llZUNyZWRlbnRpYWwiLCJWZXJpZmlhYmxlQ3JlZGVudGlhbCJdfX0.SglTXKcIhYFXlXyMEn8hBidoG7Ho21_t6pvl4CC-UYWuQyZZs6kjgBgtgV6dt_iwHqx3CqRQKuNJGwK48L6k8g\"";
        var result = mapper.readValue(jwt, nl.reinkrul.nuts.common.VerifiableCredential.class);
        assertEquals(jwt, result.source);
        assertEquals("did:web:nuts.nl:iam:bda024e3-b943-4d4d-9424-a106376a5bc7#9a04d1c4-a8a2-485f-a1bf-ed4ea14257f9", result.getId().toString());
    }

    @Test
    void deserializeJSONLD() {
        var raw = """
{
  "@context": [
    "https://w3c-ccg.github.io/lds-jws2020/contexts/lds-jws2020-v1.json",
    "https://www.w3.org/2018/credentials/v1",
    "https://nuts.nl/credentials/2024",
    "https://w3id.org/vc/status-list/2021/v1"
  ],
  "credentialStatus": {
    "id": "https://zorgbijjou.test.integration.zorgbijjou.com/nuts/statuslist/did:web:zorgbijjou.test.integration.zorgbijjou.com:nuts:iam:4f6e2a8b-fb82-4b2e-aae7-283060d05167/1#1",
    "statusListCredential": "https://zorgbijjou.test.integration.zorgbijjou.com/nuts/statuslist/did:web:zorgbijjou.test.integration.zorgbijjou.com:nuts:iam:4f6e2a8b-fb82-4b2e-aae7-283060d05167/1",
    "statusListIndex": "1",
    "statusPurpose": "revocation",
    "type": "StatusList2021Entry"
  },
  "credentialSubject": {
    "id": "did:web:zorgbijjou.test.integration.zorgbijjou.com:nuts:iam:914ab62d-9ae4-4dd0-bf76-022c7bec0f6a",
    "organization": {
      "city": "111",
      "name": "11111",
      "ura": "111"
    }
  },
  "expirationDate": "2025-09-27T14:27:27.023Z",
  "id": "did:web:zorgbijjou.test.integration.zorgbijjou.com:nuts:iam:4f6e2a8b-fb82-4b2e-aae7-283060d05167#9475dfac-1581-41e4-97e4-bdd81b65945e",
  "issuanceDate": "2024-09-27T14:27:27.113601538Z",
  "issuer": "did:web:zorgbijjou.test.integration.zorgbijjou.com:nuts:iam:4f6e2a8b-fb82-4b2e-aae7-283060d05167",
  "proof": {
    "created": "2024-09-27T14:27:27.113601538Z",
    "jws": "eyJhbGciOiJFUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il0sImtpZCI6ImRpZDp3ZWI6em9yZ2JpampvdS50ZXN0LmludGVncmF0aW9uLnpvcmdiaWpqb3UuY29tOm51dHM6aWFtOjRmNmUyYThiLWZiODItNGIyZS1hYWU3LTI4MzA2MGQwNTE2NyMxNjczMjYwNC1lYzI2LTQ4YTctODI4OC0xYTJmMTUyOGY1MjIifQ..ovZs_rJrW6ScqNlekJpelOmpf2nD9Ak2q_unTdNZVj5602VZkVik6KOrGf7JBNkRQlHGnetd2auCUQtwhJ7yEg",
    "proofPurpose": "assertionMethod",
    "type": "JsonWebSignature2020",
    "verificationMethod": "did:web:zorgbijjou.test.integration.zorgbijjou.com:nuts:iam:4f6e2a8b-fb82-4b2e-aae7-283060d05167#16732604-ec26-48a7-8288-1a2f1528f522"
  },
  "type": [
    "NutsUraCredential",
    "VerifiableCredential"
  ]
}
""";
        assertDoesNotThrow(() -> mapper.readValue(raw, nl.reinkrul.nuts.common.VerifiableCredential.class));
    }
}