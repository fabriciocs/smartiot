package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.AggregatedDataAsserts.*;
import static br.com.supera.smartiot.domain.AggregatedDataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AggregatedDataMapperTest {

    private AggregatedDataMapper aggregatedDataMapper;

    @BeforeEach
    void setUp() {
        aggregatedDataMapper = new AggregatedDataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAggregatedDataSample1();
        var actual = aggregatedDataMapper.toEntity(aggregatedDataMapper.toDto(expected));
        assertAggregatedDataAllPropertiesEquals(expected, actual);
    }
}
