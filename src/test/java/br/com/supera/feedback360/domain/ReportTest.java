package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.ReportTestSamples.*;
import static br.com.supera.feedback360.domain.SysUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Report.class);
        Report report1 = getReportSample1();
        Report report2 = new Report();
        assertThat(report1).isNotEqualTo(report2);

        report2.setId(report1.getId());
        assertThat(report1).isEqualTo(report2);

        report2 = getReportSample2();
        assertThat(report1).isNotEqualTo(report2);
    }

    @Test
    void generatedByTest() throws Exception {
        Report report = getReportRandomSampleGenerator();
        SysUser sysUserBack = getSysUserRandomSampleGenerator();

        report.setGeneratedBy(sysUserBack);
        assertThat(report.getGeneratedBy()).isEqualTo(sysUserBack);

        report.generatedBy(null);
        assertThat(report.getGeneratedBy()).isNull();
    }
}
