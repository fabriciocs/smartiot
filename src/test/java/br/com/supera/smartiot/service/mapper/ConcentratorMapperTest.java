package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ConcentratorAsserts.*;
import static br.com.supera.smartiot.domain.ConcentratorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConcentratorMapperTest {

    private ConcentratorMapper concentratorMapper;

    @BeforeEach
    void setUp() {
        concentratorMapper = new ConcentratorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConcentratorSample1();
        var actual = concentratorMapper.toEntity(concentratorMapper.toDto(expected));
        assertConcentratorAllPropertiesEquals(expected, actual);
    }
}
