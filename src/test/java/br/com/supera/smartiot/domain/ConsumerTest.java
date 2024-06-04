package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.AlertTestSamples.*;
import static br.com.supera.smartiot.domain.ConsumerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConsumerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consumer.class);
        Consumer consumer1 = getConsumerSample1();
        Consumer consumer2 = new Consumer();
        assertThat(consumer1).isNotEqualTo(consumer2);

        consumer2.setId(consumer1.getId());
        assertThat(consumer1).isEqualTo(consumer2);

        consumer2 = getConsumerSample2();
        assertThat(consumer1).isNotEqualTo(consumer2);
    }

    @Test
    void alertTest() {
        Consumer consumer = getConsumerRandomSampleGenerator();
        Alert alertBack = getAlertRandomSampleGenerator();

        consumer.addAlert(alertBack);
        assertThat(consumer.getAlerts()).containsOnly(alertBack);
        assertThat(alertBack.getConsumer()).isEqualTo(consumer);

        consumer.removeAlert(alertBack);
        assertThat(consumer.getAlerts()).doesNotContain(alertBack);
        assertThat(alertBack.getConsumer()).isNull();

        consumer.alerts(new HashSet<>(Set.of(alertBack)));
        assertThat(consumer.getAlerts()).containsOnly(alertBack);
        assertThat(alertBack.getConsumer()).isEqualTo(consumer);

        consumer.setAlerts(new HashSet<>());
        assertThat(consumer.getAlerts()).doesNotContain(alertBack);
        assertThat(alertBack.getConsumer()).isNull();
    }
}
