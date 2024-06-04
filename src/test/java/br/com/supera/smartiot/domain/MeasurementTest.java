package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.EnrollmentTestSamples.*;
import static br.com.supera.smartiot.domain.MeasurementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeasurementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Measurement.class);
        Measurement measurement1 = getMeasurementSample1();
        Measurement measurement2 = new Measurement();
        assertThat(measurement1).isNotEqualTo(measurement2);

        measurement2.setId(measurement1.getId());
        assertThat(measurement1).isEqualTo(measurement2);

        measurement2 = getMeasurementSample2();
        assertThat(measurement1).isNotEqualTo(measurement2);
    }

    @Test
    void enrollmentTest() {
        Measurement measurement = getMeasurementRandomSampleGenerator();
        Enrollment enrollmentBack = getEnrollmentRandomSampleGenerator();

        measurement.setEnrollment(enrollmentBack);
        assertThat(measurement.getEnrollment()).isEqualTo(enrollmentBack);

        measurement.enrollment(null);
        assertThat(measurement.getEnrollment()).isNull();
    }
}
