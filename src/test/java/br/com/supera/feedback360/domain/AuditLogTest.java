package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.AuditLogTestSamples.*;
import static br.com.supera.feedback360.domain.SysUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuditLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditLog.class);
        AuditLog auditLog1 = getAuditLogSample1();
        AuditLog auditLog2 = new AuditLog();
        assertThat(auditLog1).isNotEqualTo(auditLog2);

        auditLog2.setId(auditLog1.getId());
        assertThat(auditLog1).isEqualTo(auditLog2);

        auditLog2 = getAuditLogSample2();
        assertThat(auditLog1).isNotEqualTo(auditLog2);
    }

    @Test
    void userTest() throws Exception {
        AuditLog auditLog = getAuditLogRandomSampleGenerator();
        SysUser sysUserBack = getSysUserRandomSampleGenerator();

        auditLog.setUser(sysUserBack);
        assertThat(auditLog.getUser()).isEqualTo(sysUserBack);

        auditLog.user(null);
        assertThat(auditLog.getUser()).isNull();
    }
}
