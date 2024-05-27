package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.ExternalSystemAsserts.*;
import static br.com.supera.feedback360.domain.ExternalSystemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExternalSystemMapperTest {

    private ExternalSystemMapper externalSystemMapper;

    @BeforeEach
    void setUp() {
        externalSystemMapper = new ExternalSystemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExternalSystemSample1();
        var actual = externalSystemMapper.toEntity(externalSystemMapper.toDto(expected));
        assertExternalSystemAllPropertiesEquals(expected, actual);
    }
}
