package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.CompliancePolicyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompliancePolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompliancePolicy.class);
        CompliancePolicy compliancePolicy1 = getCompliancePolicySample1();
        CompliancePolicy compliancePolicy2 = new CompliancePolicy();
        assertThat(compliancePolicy1).isNotEqualTo(compliancePolicy2);

        compliancePolicy2.setId(compliancePolicy1.getId());
        assertThat(compliancePolicy1).isEqualTo(compliancePolicy2);

        compliancePolicy2 = getCompliancePolicySample2();
        assertThat(compliancePolicy1).isNotEqualTo(compliancePolicy2);
    }
}
