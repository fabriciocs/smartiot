package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.DadoSensorTestSamples.*;
import static br.com.supera.smartiot.domain.SensorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DadoSensorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DadoSensor.class);
        DadoSensor dadoSensor1 = getDadoSensorSample1();
        DadoSensor dadoSensor2 = new DadoSensor();
        assertThat(dadoSensor1).isNotEqualTo(dadoSensor2);

        dadoSensor2.setId(dadoSensor1.getId());
        assertThat(dadoSensor1).isEqualTo(dadoSensor2);

        dadoSensor2 = getDadoSensorSample2();
        assertThat(dadoSensor1).isNotEqualTo(dadoSensor2);
    }

    @Test
    void sensorTest() throws Exception {
        DadoSensor dadoSensor = getDadoSensorRandomSampleGenerator();
        Sensor sensorBack = getSensorRandomSampleGenerator();

        dadoSensor.addSensor(sensorBack);
        assertThat(dadoSensor.getSensors()).containsOnly(sensorBack);
        assertThat(sensorBack.getDadoSensores()).isEqualTo(dadoSensor);

        dadoSensor.removeSensor(sensorBack);
        assertThat(dadoSensor.getSensors()).doesNotContain(sensorBack);
        assertThat(sensorBack.getDadoSensores()).isNull();

        dadoSensor.sensors(new HashSet<>(Set.of(sensorBack)));
        assertThat(dadoSensor.getSensors()).containsOnly(sensorBack);
        assertThat(sensorBack.getDadoSensores()).isEqualTo(dadoSensor);

        dadoSensor.setSensors(new HashSet<>());
        assertThat(dadoSensor.getSensors()).doesNotContain(sensorBack);
        assertThat(sensorBack.getDadoSensores()).isNull();
    }
}
