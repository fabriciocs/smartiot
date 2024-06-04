package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ManualEntryAsserts.*;
import static br.com.supera.smartiot.domain.ManualEntryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualEntryMapperTest {

    private ManualEntryMapper manualEntryMapper;

    @BeforeEach
    void setUp() {
        manualEntryMapper = new ManualEntryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getManualEntrySample1();
        var actual = manualEntryMapper.toEntity(manualEntryMapper.toDto(expected));
        assertManualEntryAllPropertiesEquals(expected, actual);
    }
}
