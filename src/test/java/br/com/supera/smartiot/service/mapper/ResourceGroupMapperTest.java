package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.ResourceGroupAsserts.*;
import static br.com.supera.smartiot.domain.ResourceGroupTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceGroupMapperTest {

    private ResourceGroupMapper resourceGroupMapper;

    @BeforeEach
    void setUp() {
        resourceGroupMapper = new ResourceGroupMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResourceGroupSample1();
        var actual = resourceGroupMapper.toEntity(resourceGroupMapper.toDto(expected));
        assertResourceGroupAllPropertiesEquals(expected, actual);
    }
}
