package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ClienteTestSamples.*;
import static br.com.supera.smartiot.domain.ConfiguracaoAlertaTestSamples.*;
import static br.com.supera.smartiot.domain.DadoSensorTestSamples.*;
import static br.com.supera.smartiot.domain.SensorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SensorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sensor.class);
        Sensor sensor1 = getSensorSample1();
        Sensor sensor2 = new Sensor();
        assertThat(sensor1).isNotEqualTo(sensor2);

        sensor2.setId(sensor1.getId());
        assertThat(sensor1).isEqualTo(sensor2);

        sensor2 = getSensorSample2();
        assertThat(sensor1).isNotEqualTo(sensor2);
    }

    @Test
    void configuracaoAlertasTest() {
        Sensor sensor = getSensorRandomSampleGenerator();
        ConfiguracaoAlerta configuracaoAlertaBack = getConfiguracaoAlertaRandomSampleGenerator();

        sensor.addConfiguracaoAlertas(configuracaoAlertaBack);
        assertThat(sensor.getConfiguracaoAlertas()).containsOnly(configuracaoAlertaBack);
        assertThat(configuracaoAlertaBack.getSensor()).isEqualTo(sensor);

        sensor.removeConfiguracaoAlertas(configuracaoAlertaBack);
        assertThat(sensor.getConfiguracaoAlertas()).doesNotContain(configuracaoAlertaBack);
        assertThat(configuracaoAlertaBack.getSensor()).isNull();

        sensor.configuracaoAlertas(new HashSet<>(Set.of(configuracaoAlertaBack)));
        assertThat(sensor.getConfiguracaoAlertas()).containsOnly(configuracaoAlertaBack);
        assertThat(configuracaoAlertaBack.getSensor()).isEqualTo(sensor);

        sensor.setConfiguracaoAlertas(new HashSet<>());
        assertThat(sensor.getConfiguracaoAlertas()).doesNotContain(configuracaoAlertaBack);
        assertThat(configuracaoAlertaBack.getSensor()).isNull();
    }

    @Test
    void clienteTest() {
        Sensor sensor = getSensorRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        sensor.setCliente(clienteBack);
        assertThat(sensor.getCliente()).isEqualTo(clienteBack);

        sensor.cliente(null);
        assertThat(sensor.getCliente()).isNull();
    }

    @Test
    void dadoSensoresTest() {
        Sensor sensor = getSensorRandomSampleGenerator();
        DadoSensor dadoSensorBack = getDadoSensorRandomSampleGenerator();

        sensor.setDadoSensores(dadoSensorBack);
        assertThat(sensor.getDadoSensores()).isEqualTo(dadoSensorBack);

        sensor.dadoSensores(null);
        assertThat(sensor.getDadoSensores()).isNull();
    }
}
