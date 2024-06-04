package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.MeterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Meter.class);
        Meter meter1 = getMeterSample1();
        Meter meter2 = new Meter();
        assertThat(meter1).isNotEqualTo(meter2);

        meter2.setId(meter1.getId());
        assertThat(meter1).isEqualTo(meter2);

        meter2 = getMeterSample2();
        assertThat(meter1).isNotEqualTo(meter2);
    }
}
