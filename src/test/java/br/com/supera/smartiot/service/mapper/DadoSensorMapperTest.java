package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.DadoSensorAsserts.*;
import static br.com.supera.smartiot.domain.DadoSensorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DadoSensorMapperTest {

    private DadoSensorMapper dadoSensorMapper;

    @BeforeEach
    void setUp() {
        dadoSensorMapper = new DadoSensorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDadoSensorSample1();
        var actual = dadoSensorMapper.toEntity(dadoSensorMapper.toDto(expected));
        assertDadoSensorAllPropertiesEquals(expected, actual);
    }
}
