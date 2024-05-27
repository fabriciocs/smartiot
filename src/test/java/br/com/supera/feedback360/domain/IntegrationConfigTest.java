package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.ExternalSystemTestSamples.*;
import static br.com.supera.feedback360.domain.IntegrationConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegrationConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegrationConfig.class);
        IntegrationConfig integrationConfig1 = getIntegrationConfigSample1();
        IntegrationConfig integrationConfig2 = new IntegrationConfig();
        assertThat(integrationConfig1).isNotEqualTo(integrationConfig2);

        integrationConfig2.setId(integrationConfig1.getId());
        assertThat(integrationConfig1).isEqualTo(integrationConfig2);

        integrationConfig2 = getIntegrationConfigSample2();
        assertThat(integrationConfig1).isNotEqualTo(integrationConfig2);
    }

    @Test
    void externalSystemTest() throws Exception {
        IntegrationConfig integrationConfig = getIntegrationConfigRandomSampleGenerator();
        ExternalSystem externalSystemBack = getExternalSystemRandomSampleGenerator();

        integrationConfig.setExternalSystem(externalSystemBack);
        assertThat(integrationConfig.getExternalSystem()).isEqualTo(externalSystemBack);

        integrationConfig.externalSystem(null);
        assertThat(integrationConfig.getExternalSystem()).isNull();
    }
}
