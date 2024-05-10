package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.SensorAsserts.*;
import static br.com.supera.smartiot.domain.SensorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SensorMapperTest {

    private SensorMapper sensorMapper;

    @BeforeEach
    void setUp() {
        sensorMapper = new SensorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSensorSample1();
        var actual = sensorMapper.toEntity(sensorMapper.toDto(expected));
        assertSensorAllPropertiesEquals(expected, actual);
    }
}
