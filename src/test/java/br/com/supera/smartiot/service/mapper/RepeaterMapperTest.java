package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.RepeaterAsserts.*;
import static br.com.supera.smartiot.domain.RepeaterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepeaterMapperTest {

    private RepeaterMapper repeaterMapper;

    @BeforeEach
    void setUp() {
        repeaterMapper = new RepeaterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRepeaterSample1();
        var actual = repeaterMapper.toEntity(repeaterMapper.toDto(expected));
        assertRepeaterAllPropertiesEquals(expected, actual);
    }
}
