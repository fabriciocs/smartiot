package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.SysRoleAsserts.*;
import static br.com.supera.feedback360.domain.SysRoleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SysRoleMapperTest {

    private SysRoleMapper sysRoleMapper;

    @BeforeEach
    void setUp() {
        sysRoleMapper = new SysRoleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSysRoleSample1();
        var actual = sysRoleMapper.toEntity(sysRoleMapper.toDto(expected));
        assertSysRoleAllPropertiesEquals(expected, actual);
    }
}
