package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.PricingAsserts.*;
import static br.com.supera.smartiot.domain.PricingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PricingMapperTest {

    private PricingMapper pricingMapper;

    @BeforeEach
    void setUp() {
        pricingMapper = new PricingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPricingSample1();
        var actual = pricingMapper.toEntity(pricingMapper.toDto(expected));
        assertPricingAllPropertiesEquals(expected, actual);
    }
}
