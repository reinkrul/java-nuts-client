package nl.reinkrul.nuts.credentials;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.reinkrul.nuts.Configuration;
import nl.reinkrul.nuts.common.VerifiableCredential;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NutsEmployeeCredentialTest {

    @Test
    void getCredential() throws JsonProcessingException {
        var employeeIdentifier = "12345";
        var employeeInitials = "A.B.";
        var employeeName = "John Doe";
        var employeeRole = "Manager";

        var credential = new NutsEmployeeCredential(employeeIdentifier, employeeInitials, employeeName, employeeRole).getCredential();

        var expected = "{\"@context\":[\"https://www.w3.org/2018/credentials/v1\",\"https://nuts.nl/credentials/v1\"],\"type\":[\"VerifiableCredential\",\"NutsEmployeeCredential\"],\"credentialSubject\":{\"type\":\"Organization\",\"member\":{\"type\":\"EmployeeRole\",\"identifier\":\"12345\",\"member\":{\"type\":\"Person\",\"familyName\":\"John Doe\",\"initials\":\"A.B.\"},\"roleName\":\"Manager\"}}}";
        var expectedVC = Configuration.create().getJSON().getMapper().readValue(expected, VerifiableCredential.class);
        var actualVC = Configuration.create().getJSON().getMapper().readValue(credential.source, VerifiableCredential.class);
        assertEquals(expectedVC, actualVC);
    }
}