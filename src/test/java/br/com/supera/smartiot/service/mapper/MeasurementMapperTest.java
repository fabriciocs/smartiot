package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.MeasurementAsserts.*;
import static br.com.supera.smartiot.domain.MeasurementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeasurementMapperTest {

    private MeasurementMapper measurementMapper;

    @BeforeEach
    void setUp() {
        measurementMapper = new MeasurementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMeasurementSample1();
        var actual = measurementMapper.toEntity(measurementMapper.toDto(expected));
        assertMeasurementAllPropertiesEquals(expected, actual);
    }
}
