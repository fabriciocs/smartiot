package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.TransmitterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransmitterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transmitter.class);
        Transmitter transmitter1 = getTransmitterSample1();
        Transmitter transmitter2 = new Transmitter();
        assertThat(transmitter1).isNotEqualTo(transmitter2);

        transmitter2.setId(transmitter1.getId());
        assertThat(transmitter1).isEqualTo(transmitter2);

        transmitter2 = getTransmitterSample2();
        assertThat(transmitter1).isNotEqualTo(transmitter2);
    }
}
