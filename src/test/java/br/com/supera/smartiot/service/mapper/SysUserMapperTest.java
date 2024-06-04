package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.SysUserAsserts.*;
import static br.com.supera.smartiot.domain.SysUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SysUserMapperTest {

    private SysUserMapper sysUserMapper;

    @BeforeEach
    void setUp() {
        sysUserMapper = new SysUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSysUserSample1();
        var actual = sysUserMapper.toEntity(sysUserMapper.toDto(expected));
        assertSysUserAllPropertiesEquals(expected, actual);
    }
}
