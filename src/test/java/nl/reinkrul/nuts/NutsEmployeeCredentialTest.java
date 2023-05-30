package nl.reinkrul.nuts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.reinkrul.nuts.extra.NutsEmployeeCredential;
import nl.reinkrul.nuts.extra.OrganizationMember;
import nl.reinkrul.nuts.extra.OrganizationMemberMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NutsEmployeeCredentialTest {

    @Test
    public void testMarshalling() throws JsonProcessingException {
        var credential = new NutsEmployeeCredential()
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

                );

        var expected = "{\"id\":\"did:nuts:some-did\",\"type\":\"Organization\",\"member\":{\"identifier\":\"12345678\",\"roleName\":\"Verpleegkundige niveau 2\",\"type\":\"EmployeeRole\",\"member\":{\"type\":\"Person\",\"familyName\":\"van der Zorg\",\"initials\":\"A.B.\"}}}";
        var actual = new ObjectMapper().writeValueAsString(credential);

        Assertions.assertEquals(expected, actual);
    }
}
