package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ConcentratorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConcentratorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Concentrator.class);
        Concentrator concentrator1 = getConcentratorSample1();
        Concentrator concentrator2 = new Concentrator();
        assertThat(concentrator1).isNotEqualTo(concentrator2);

        concentrator2.setId(concentrator1.getId());
        assertThat(concentrator1).isEqualTo(concentrator2);

        concentrator2 = getConcentratorSample2();
        assertThat(concentrator1).isNotEqualTo(concentrator2);
    }
}
