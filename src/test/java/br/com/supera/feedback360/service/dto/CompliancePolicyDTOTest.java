package br.com.supera.feedback360.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompliancePolicyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompliancePolicyDTO.class);
        CompliancePolicyDTO compliancePolicyDTO1 = new CompliancePolicyDTO();
        compliancePolicyDTO1.setId(1L);
        CompliancePolicyDTO compliancePolicyDTO2 = new CompliancePolicyDTO();
        assertThat(compliancePolicyDTO1).isNotEqualTo(compliancePolicyDTO2);
        compliancePolicyDTO2.setId(compliancePolicyDTO1.getId());
        assertThat(compliancePolicyDTO1).isEqualTo(compliancePolicyDTO2);
        compliancePolicyDTO2.setId(2L);
        assertThat(compliancePolicyDTO1).isNotEqualTo(compliancePolicyDTO2);
        compliancePolicyDTO1.setId(null);
        assertThat(compliancePolicyDTO1).isNotEqualTo(compliancePolicyDTO2);
    }
}
