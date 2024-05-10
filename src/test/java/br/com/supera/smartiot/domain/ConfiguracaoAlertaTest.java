package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ConfiguracaoAlertaTestSamples.*;
import static br.com.supera.smartiot.domain.SensorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConfiguracaoAlertaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfiguracaoAlerta.class);
        ConfiguracaoAlerta configuracaoAlerta1 = getConfiguracaoAlertaSample1();
        ConfiguracaoAlerta configuracaoAlerta2 = new ConfiguracaoAlerta();
        assertThat(configuracaoAlerta1).isNotEqualTo(configuracaoAlerta2);

        configuracaoAlerta2.setId(configuracaoAlerta1.getId());
        assertThat(configuracaoAlerta1).isEqualTo(configuracaoAlerta2);

        configuracaoAlerta2 = getConfiguracaoAlertaSample2();
        assertThat(configuracaoAlerta1).isNotEqualTo(configuracaoAlerta2);
    }

    @Test
    void sensorTest() throws Exception {
        ConfiguracaoAlerta configuracaoAlerta = getConfiguracaoAlertaRandomSampleGenerator();
        Sensor sensorBack = getSensorRandomSampleGenerator();

        configuracaoAlerta.addSensor(sensorBack);
        assertThat(configuracaoAlerta.getSensors()).containsOnly(sensorBack);
        assertThat(sensorBack.getConfiguracaoAlertas()).isEqualTo(configuracaoAlerta);

        configuracaoAlerta.removeSensor(sensorBack);
        assertThat(configuracaoAlerta.getSensors()).doesNotContain(sensorBack);
        assertThat(sensorBack.getConfiguracaoAlertas()).isNull();

        configuracaoAlerta.sensors(new HashSet<>(Set.of(sensorBack)));
        assertThat(configuracaoAlerta.getSensors()).containsOnly(sensorBack);
        assertThat(sensorBack.getConfiguracaoAlertas()).isEqualTo(configuracaoAlerta);

        configuracaoAlerta.setSensors(new HashSet<>());
        assertThat(configuracaoAlerta.getSensors()).doesNotContain(sensorBack);
        assertThat(sensorBack.getConfiguracaoAlertas()).isNull();
    }
}
