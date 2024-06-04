package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ClienteTestSamples.*;
import static br.com.supera.smartiot.domain.SensorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void sensoresTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Sensor sensorBack = getSensorRandomSampleGenerator();

        cliente.addSensores(sensorBack);
        assertThat(cliente.getSensores()).containsOnly(sensorBack);
        assertThat(sensorBack.getCliente()).isEqualTo(cliente);

        cliente.removeSensores(sensorBack);
        assertThat(cliente.getSensores()).doesNotContain(sensorBack);
        assertThat(sensorBack.getCliente()).isNull();

        cliente.sensores(new HashSet<>(Set.of(sensorBack)));
        assertThat(cliente.getSensores()).containsOnly(sensorBack);
        assertThat(sensorBack.getCliente()).isEqualTo(cliente);

        cliente.setSensores(new HashSet<>());
        assertThat(cliente.getSensores()).doesNotContain(sensorBack);
        assertThat(sensorBack.getCliente()).isNull();
    }
}
