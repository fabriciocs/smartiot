package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.ExternalSystemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExternalSystemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalSystem.class);
        ExternalSystem externalSystem1 = getExternalSystemSample1();
        ExternalSystem externalSystem2 = new ExternalSystem();
        assertThat(externalSystem1).isNotEqualTo(externalSystem2);

        externalSystem2.setId(externalSystem1.getId());
        assertThat(externalSystem1).isEqualTo(externalSystem2);

        externalSystem2 = getExternalSystemSample2();
        assertThat(externalSystem1).isNotEqualTo(externalSystem2);
    }
}
