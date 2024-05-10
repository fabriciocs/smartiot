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
    void configuracaoAlertasTest() throws Exception {
        Sensor sensor = getSensorRandomSampleGenerator();
        ConfiguracaoAlerta configuracaoAlertaBack = getConfiguracaoAlertaRandomSampleGenerator();

        sensor.setConfiguracaoAlertas(configuracaoAlertaBack);
        assertThat(sensor.getConfiguracaoAlertas()).isEqualTo(configuracaoAlertaBack);

        sensor.configuracaoAlertas(null);
        assertThat(sensor.getConfiguracaoAlertas()).isNull();
    }

    @Test
    void dadoSensoresTest() throws Exception {
        Sensor sensor = getSensorRandomSampleGenerator();
        DadoSensor dadoSensorBack = getDadoSensorRandomSampleGenerator();

        sensor.setDadoSensores(dadoSensorBack);
        assertThat(sensor.getDadoSensores()).isEqualTo(dadoSensorBack);

        sensor.dadoSensores(null);
        assertThat(sensor.getDadoSensores()).isNull();
    }

    @Test
    void clienteTest() throws Exception {
        Sensor sensor = getSensorRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        sensor.addCliente(clienteBack);
        assertThat(sensor.getClientes()).containsOnly(clienteBack);
        assertThat(clienteBack.getSensores()).isEqualTo(sensor);

        sensor.removeCliente(clienteBack);
        assertThat(sensor.getClientes()).doesNotContain(clienteBack);
        assertThat(clienteBack.getSensores()).isNull();

        sensor.clientes(new HashSet<>(Set.of(clienteBack)));
        assertThat(sensor.getClientes()).containsOnly(clienteBack);
        assertThat(clienteBack.getSensores()).isEqualTo(sensor);

        sensor.setClientes(new HashSet<>());
        assertThat(sensor.getClientes()).doesNotContain(clienteBack);
        assertThat(clienteBack.getSensores()).isNull();
    }
}
