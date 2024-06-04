package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.RepeaterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepeaterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repeater.class);
        Repeater repeater1 = getRepeaterSample1();
        Repeater repeater2 = new Repeater();
        assertThat(repeater1).isNotEqualTo(repeater2);

        repeater2.setId(repeater1.getId());
        assertThat(repeater1).isEqualTo(repeater2);

        repeater2 = getRepeaterSample2();
        assertThat(repeater1).isNotEqualTo(repeater2);
    }
}
