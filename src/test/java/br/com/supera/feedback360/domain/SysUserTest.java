package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.SysRoleTestSamples.*;
import static br.com.supera.feedback360.domain.SysUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysUser.class);
        SysUser sysUser1 = getSysUserSample1();
        SysUser sysUser2 = new SysUser();
        assertThat(sysUser1).isNotEqualTo(sysUser2);

        sysUser2.setId(sysUser1.getId());
        assertThat(sysUser1).isEqualTo(sysUser2);

        sysUser2 = getSysUserSample2();
        assertThat(sysUser1).isNotEqualTo(sysUser2);
    }

    @Test
    void roleTest() throws Exception {
        SysUser sysUser = getSysUserRandomSampleGenerator();
        SysRole sysRoleBack = getSysRoleRandomSampleGenerator();

        sysUser.setRole(sysRoleBack);
        assertThat(sysUser.getRole()).isEqualTo(sysRoleBack);

        sysUser.role(null);
        assertThat(sysUser.getRole()).isNull();
    }
}
