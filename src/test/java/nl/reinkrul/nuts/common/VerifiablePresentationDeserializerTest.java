package nl.reinkrul.nuts.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerifiablePresentationDeserializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    public VerifiablePresentationDeserializerTest() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(nl.reinkrul.nuts.common.VerifiablePresentation.class, new VerifiablePresentationDeserializer());
        mapper.registerModule(module);
    }

    @Test
    void deserializeJWT() throws JsonProcessingException {
        var jwt = "\"eyJhbGciOiJFUzI1NiIsImtpZCI6ImRpZDp3ZWI6bG9jYWxob3N0JTNBODA4MDppYW06NDI1MDA4ZTQtZWE2Zi00ZWQyLTljZDItNTUzMDlhM2E1MjA1I2U5YTIyMTQzLTQ3NjktNGI5Mi04MzQ3LWEwMGEwMzUwMzdlMSIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdCJdLCJleHAiOjE3MzAyNjQxNjgsImp0aSI6ImRpZDp3ZWI6bG9jYWxob3N0JTNBODA4MDppYW06NDI1MDA4ZTQtZWE2Zi00ZWQyLTljZDItNTUzMDlhM2E1MjA1Izc1Yjg4YmYxLWZlM2ItNDI4MC1hYjJiLWY5NDE1MjY0MWY5OCIsIm5iZiI6MTcyNzQ5OTM2OSwibm9uY2UiOiJ0eHhkSXVnQkpRa3ByQTNoNGtEWHdLbmllNzZFMENwb3gzOFdxekRrd3pzIiwic3ViIjoiZGlkOndlYjpsb2NhbGhvc3QlM0E4MDgwOmlhbTo0MjUwMDhlNC1lYTZmLTRlZDItOWNkMi01NTMwOWEzYTUyMDUiLCJ2cCI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSJdLCJob2xkZXIiOiJkaWQ6d2ViOmxvY2FsaG9zdCUzQTgwODA6aWFtOjQyNTAwOGU0LWVhNmYtNGVkMi05Y2QyLTU1MzA5YTNhNTIwNSIsInR5cGUiOiJWZXJpZmlhYmxlUHJlc2VudGF0aW9uIiwidmVyaWZpYWJsZUNyZWRlbnRpYWwiOlt7IkBjb250ZXh0IjpbImh0dHBzOi8vdzNjLWNjZy5naXRodWIuaW8vbGRzLWp3czIwMjAvY29udGV4dHMvbGRzLWp3czIwMjAtdjEuanNvbiIsImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL3YxIiwiaHR0cHM6Ly9udXRzLm5sL2NyZWRlbnRpYWxzLzIwMjQiXSwiY3JlZGVudGlhbFN1YmplY3QiOnsiaWQiOiJkaWQ6d2ViOmxvY2FsaG9zdCUzQTgwODA6aWFtOjQyNTAwOGU0LWVhNmYtNGVkMi05Y2QyLTU1MzA5YTNhNTIwNSIsIm9yZ2FuaXphdGlvbiI6eyJjaXR5IjoiWm9yZ2RvcnAiLCJuYW1lIjoiRXh0cmEgQ2FyZWZ1bCBCLlYuIiwidXJhIjoiMTIzNDUifX0sImlkIjoiZGlkOndlYjpsb2NhbGhvc3QlM0E4MDgwOmlhbTo0MjUwMDhlNC1lYTZmLTRlZDItOWNkMi01NTMwOWEzYTUyMDUjNzQ3MzVlMWItYzIyYi00ODVlLWIxNTYtZTk0MmQ4YzhkYmZmIiwiaXNzdWFuY2VEYXRlIjoiMjAyNC0wOS0yOFQwNDo1NjowOS4xOTczNzAzOFoiLCJpc3N1ZXIiOiJkaWQ6d2ViOmxvY2FsaG9zdCUzQTgwODA6aWFtOjQyNTAwOGU0LWVhNmYtNGVkMi05Y2QyLTU1MzA5YTNhNTIwNSIsInByb29mIjp7ImNyZWF0ZWQiOiIyMDI0LTA5LTI4VDA0OjU2OjA5LjE5NzM3MDM4WiIsImp3cyI6ImV5SmhiR2NpT2lKRlV6STFOaUlzSW1JMk5DSTZabUZzYzJVc0ltTnlhWFFpT2xzaVlqWTBJbDBzSW10cFpDSTZJbVJwWkRwM1pXSTZiRzlqWVd4b2IzTjBKVE5CT0RBNE1EcHBZVzA2TkRJMU1EQTRaVFF0WldFMlppMDBaV1F5TFRsalpESXROVFV6TURsaE0yRTFNakExSTJVNVlUSXlNVFF6TFRRM05qa3ROR0k1TWkwNE16UTNMV0V3TUdFd016VXdNemRsTVNKOS4uNVljZ3ZpUjNsU3RrUG9QdlplcjAtR0ZoYTc2UjVmUHlqZ0xlSzBSYUw0cWFOZElZVFhfN3ZNbkNjUEdTbG9YZ0xSSnpobTB6SFBPR215cllhOGFLbFEiLCJwcm9vZlB1cnBvc2UiOiJhc3NlcnRpb25NZXRob2QiLCJ0eXBlIjoiSnNvbldlYlNpZ25hdHVyZTIwMjAiLCJ2ZXJpZmljYXRpb25NZXRob2QiOiJkaWQ6d2ViOmxvY2FsaG9zdCUzQTgwODA6aWFtOjQyNTAwOGU0LWVhNmYtNGVkMi05Y2QyLTU1MzA5YTNhNTIwNSNlOWEyMjE0My00NzY5LTRiOTItODM0Ny1hMDBhMDM1MDM3ZTEifSwidHlwZSI6WyJOdXRzVXJhQ3JlZGVudGlhbCIsIlZlcmlmaWFibGVDcmVkZW50aWFsIl19LHsiQGNvbnRleHQiOlsiaHR0cHM6Ly93d3cudzMub3JnLzIwMTgvY3JlZGVudGlhbHMvdjEiLCJodHRwczovL251dHMubmwvY3JlZGVudGlhbHMvdjEiXSwiY3JlZGVudGlhbFN1YmplY3QiOnsiYXV0aFNlcnZlclVSTCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9vYXV0aDIvMTdhNjE2ODgtNGJiOS00ZjA4LTg1MWQtNTIxMmJiMTliN2JiIiwiZmhpci11cmwiOiJodHRwczovL2V4YW1wbGUuY29tL2ZoaXIiLCJpZCI6ImRpZDp3ZWI6bG9jYWxob3N0JTNBODA4MDppYW06NDI1MDA4ZTQtZWE2Zi00ZWQyLTljZDItNTUzMDlhM2E1MjA1In0sImlkIjoiZGMyNzk5MzktZjViMi00YWU4LWE3MmUtYjRhNzU4ODYzYmYxIiwiaXNzdWFuY2VEYXRlIjoiMjAyNC0wOS0yOFQwNDo1NjowOVoiLCJpc3N1ZXIiOiJkaWQ6d2ViOmxvY2FsaG9zdCUzQTgwODA6aWFtOjQyNTAwOGU0LWVhNmYtNGVkMi05Y2QyLTU1MzA5YTNhNTIwNSIsInByb29mIjpudWxsLCJ0eXBlIjpbIlZlcmlmaWFibGVDcmVkZW50aWFsIiwiRGlzY292ZXJ5UmVnaXN0cmF0aW9uQ3JlZGVudGlhbCJdfV19fQ.m6H4eDhqaL2WQmrkdpsoEE2g7xeV5ZghR2hrd22GADfLfYyK2oky-SB7pdErMRiSkDKmPQXDHUWsCeM-hWPOyA\"";
        var result = mapper.readValue(jwt, nl.reinkrul.nuts.common.VerifiablePresentation.class);
        assertEquals(jwt, "\"" + result.source + "\"");
    }

    @Test
    void deserializeJSONLD() {
        var raw = """
{
  "@context": [
    "https://w3c-ccg.github.io/lds-jws2020/contexts/lds-jws2020-v1.json",
    "https://nuts.nl/credentials/v1",
    "https://www.w3.org/2018/credentials/v1"
  ],
  "proof": {
    "challenge": "EN:PractitionerLogin:v3 I hereby declare to act on behalf of CareBears located in Caretown. This declaration is valid from Wednesday, 19 April 2023 12:20:00 until Thursday, 20 April 2023 13:20:00.",
    "created": "2023-04-20T09:53:03Z",
    "expires": "2023-04-24T09:53:03Z",
    "jws": "eyJhbGciOiJFUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il0sImtpZCI6ImRpZDpudXRzOjhOWXpmc25kWkpIaDZHcXpLaVNCcHlFUnJGeHVYNjR6NnRFNXJhYTduRWptI2JZY3VldDZFSG9qTWxhTXF3Tm9DM2M2ZXRLbFVIb0o5clJ2VXUzWktFRXcifQ..IqGTyxmKgQ2HQ6RuYSn2B0sFh-okj8aEYC1VGTtlm1eiLBVr2wnnp1fX9oifhWHocuEKURkuSubENeW-Z3nMHQ",
    "proofPurpose": "assertionMethod",
    "type": "JsonWebSignature2020",
    "verificationMethod": "did:nuts:8NYzfsndZJHh6GqzKiSBpyERrFxuX64z6tE5raa7nEjm#bYcuet6EHojMlaMqwNoC3c6etKlUHoJ9rRvUu3ZKEEw"
  },
  "type": [
    "VerifiablePresentation",
    "NutsSelfSignedPresentation"
  ],
  "verifiableCredential": [
    {
      "@context": [
        "https://nuts.nl/credentials/v1",
        "https://www.w3.org/2018/credentials/v1",
        "https://w3c-ccg.github.io/lds-jws2020/contexts/lds-jws2020-v1.json"
      ],
      "credentialSubject": [
        {
          "id": "did:nuts:8NYzfsndZJHh6GqzKiSBpyERrFxuX64z6tE5raa7nEjm",
          "member": {
            "identifier": "user@example.com",
            "member": {
              "familyName": "Tester",
              "initials": "T",
              "type": "Person"
            },
            "roleName": "Verpleegkundige niveau 2",
            "type": "EmployeeRole"
          },
          "type": "Organization"
        }
      ],
      "id": "did:nuts:8NYzfsndZJHh6GqzKiSBpyERrFxuX64z6tE5raa7nEjm#dde77e76-7e3c-483f-a813-2b851a6a969c",
      "issuanceDate": "2023-04-20T08:52:45.941461+02:00",
      "issuer": "did:nuts:8NYzfsndZJHh6GqzKiSBpyERrFxuX64z6tE5raa7nEjm",
      "proof": {
        "created": "2023-04-20T09:53:03Z",
        "expires": "2023-04-24T09:53:03Z",
        "jws": "eyJhbGciOiJFUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il0sImtpZCI6ImRpZDpudXRzOjhOWXpmc25kWkpIaDZHcXpLaVNCcHlFUnJGeHVYNjR6NnRFNXJhYTduRWptI2JZY3VldDZFSG9qTWxhTXF3Tm9DM2M2ZXRLbFVIb0o5clJ2VXUzWktFRXcifQ..VhEbDoth8GrAni_LhZm-12VnlJToAbX0FDg1Rf7u7qIy3W54IcxAxkZP28YxGG681WpufwPeqHrtnYLsW8Fh7w",
        "proofPurpose": "assertionMethod",
        "type": "JsonWebSignature2020",
        "verificationMethod": "did:nuts:8NYzfsndZJHh6GqzKiSBpyERrFxuX64z6tE5raa7nEjm#bYcuet6EHojMlaMqwNoC3c6etKlUHoJ9rRvUu3ZKEEw"
      },
      "type": [
        "NutsEmployeeCredential",
        "VerifiableCredential"
      ]
    }
  ]
}
""";
        assertDoesNotThrow(() -> mapper.readValue(raw, nl.reinkrul.nuts.common.VerifiablePresentation.class));
    }
}