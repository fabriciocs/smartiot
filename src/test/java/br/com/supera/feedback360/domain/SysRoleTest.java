package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.SysRoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysRole.class);
        SysRole sysRole1 = getSysRoleSample1();
        SysRole sysRole2 = new SysRole();
        assertThat(sysRole1).isNotEqualTo(sysRole2);

        sysRole2.setId(sysRole1.getId());
        assertThat(sysRole1).isEqualTo(sysRole2);

        sysRole2 = getSysRoleSample2();
        assertThat(sysRole1).isNotEqualTo(sysRole2);
    }
}
