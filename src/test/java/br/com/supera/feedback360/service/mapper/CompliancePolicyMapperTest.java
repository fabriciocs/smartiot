package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.CompliancePolicyAsserts.*;
import static br.com.supera.feedback360.domain.CompliancePolicyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompliancePolicyMapperTest {

    private CompliancePolicyMapper compliancePolicyMapper;

    @BeforeEach
    void setUp() {
        compliancePolicyMapper = new CompliancePolicyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompliancePolicySample1();
        var actual = compliancePolicyMapper.toEntity(compliancePolicyMapper.toDto(expected));
        assertCompliancePolicyAllPropertiesEquals(expected, actual);
    }
}
