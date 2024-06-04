package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.MeterAsserts.*;
import static br.com.supera.smartiot.domain.MeterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeterMapperTest {

    private MeterMapper meterMapper;

    @BeforeEach
    void setUp() {
        meterMapper = new MeterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMeterSample1();
        var actual = meterMapper.toEntity(meterMapper.toDto(expected));
        assertMeterAllPropertiesEquals(expected, actual);
    }
}
