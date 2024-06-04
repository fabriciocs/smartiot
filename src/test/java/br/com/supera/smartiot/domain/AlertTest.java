package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.AlertTestSamples.*;
import static br.com.supera.smartiot.domain.ConsumerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alert.class);
        Alert alert1 = getAlertSample1();
        Alert alert2 = new Alert();
        assertThat(alert1).isNotEqualTo(alert2);

        alert2.setId(alert1.getId());
        assertThat(alert1).isEqualTo(alert2);

        alert2 = getAlertSample2();
        assertThat(alert1).isNotEqualTo(alert2);
    }

    @Test
    void consumerTest() {
        Alert alert = getAlertRandomSampleGenerator();
        Consumer consumerBack = getConsumerRandomSampleGenerator();

        alert.setConsumer(consumerBack);
        assertThat(alert.getConsumer()).isEqualTo(consumerBack);

        alert.consumer(null);
        assertThat(alert.getConsumer()).isNull();
    }
}
