package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.AnalyticsAsserts.*;
import static br.com.supera.feedback360.domain.AnalyticsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnalyticsMapperTest {

    private AnalyticsMapper analyticsMapper;

    @BeforeEach
    void setUp() {
        analyticsMapper = new AnalyticsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAnalyticsSample1();
        var actual = analyticsMapper.toEntity(analyticsMapper.toDto(expected));
        assertAnalyticsAllPropertiesEquals(expected, actual);
    }
}
