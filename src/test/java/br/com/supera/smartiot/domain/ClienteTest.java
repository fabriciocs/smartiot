package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ClienteTestSamples.*;
import static br.com.supera.smartiot.domain.SensorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    void sensoresTest() throws Exception {
        Cliente cliente = getClienteRandomSampleGenerator();
        Sensor sensorBack = getSensorRandomSampleGenerator();

        cliente.setSensores(sensorBack);
        assertThat(cliente.getSensores()).isEqualTo(sensorBack);

        cliente.sensores(null);
        assertThat(cliente.getSensores()).isNull();
    }
}
