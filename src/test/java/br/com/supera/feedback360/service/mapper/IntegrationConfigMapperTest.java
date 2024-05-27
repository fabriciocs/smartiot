package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.IntegrationConfigAsserts.*;
import static br.com.supera.feedback360.domain.IntegrationConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegrationConfigMapperTest {

    private IntegrationConfigMapper integrationConfigMapper;

    @BeforeEach
    void setUp() {
        integrationConfigMapper = new IntegrationConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIntegrationConfigSample1();
        var actual = integrationConfigMapper.toEntity(integrationConfigMapper.toDto(expected));
        assertIntegrationConfigAllPropertiesEquals(expected, actual);
    }
}
