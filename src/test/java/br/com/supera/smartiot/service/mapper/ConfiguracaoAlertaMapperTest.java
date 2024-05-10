package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ConfiguracaoAlertaAsserts.*;
import static br.com.supera.smartiot.domain.ConfiguracaoAlertaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfiguracaoAlertaMapperTest {

    private ConfiguracaoAlertaMapper configuracaoAlertaMapper;

    @BeforeEach
    void setUp() {
        configuracaoAlertaMapper = new ConfiguracaoAlertaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConfiguracaoAlertaSample1();
        var actual = configuracaoAlertaMapper.toEntity(configuracaoAlertaMapper.toDto(expected));
        assertConfiguracaoAlertaAllPropertiesEquals(expected, actual);
    }
}
