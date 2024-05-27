package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.ConfiguracaoAlertaAsserts.*;
import static br.com.supera.feedback360.domain.ConfiguracaoAlertaTestSamples.*;

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
