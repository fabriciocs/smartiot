package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.CostCenterAsserts.*;
import static br.com.supera.smartiot.domain.CostCenterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CostCenterMapperTest {

    private CostCenterMapper costCenterMapper;

    @BeforeEach
    void setUp() {
        costCenterMapper = new CostCenterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCostCenterSample1();
        var actual = costCenterMapper.toEntity(costCenterMapper.toDto(expected));
        assertCostCenterAllPropertiesEquals(expected, actual);
    }
}
