package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.EnrollmentTestSamples.*;
import static br.com.supera.smartiot.domain.MeasurementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EnrollmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enrollment.class);
        Enrollment enrollment1 = getEnrollmentSample1();
        Enrollment enrollment2 = new Enrollment();
        assertThat(enrollment1).isNotEqualTo(enrollment2);

        enrollment2.setId(enrollment1.getId());
        assertThat(enrollment1).isEqualTo(enrollment2);

        enrollment2 = getEnrollmentSample2();
        assertThat(enrollment1).isNotEqualTo(enrollment2);
    }

    @Test
    void measurementTest() {
        Enrollment enrollment = getEnrollmentRandomSampleGenerator();
        Measurement measurementBack = getMeasurementRandomSampleGenerator();

        enrollment.addMeasurement(measurementBack);
        assertThat(enrollment.getMeasurements()).containsOnly(measurementBack);
        assertThat(measurementBack.getEnrollment()).isEqualTo(enrollment);

        enrollment.removeMeasurement(measurementBack);
        assertThat(enrollment.getMeasurements()).doesNotContain(measurementBack);
        assertThat(measurementBack.getEnrollment()).isNull();

        enrollment.measurements(new HashSet<>(Set.of(measurementBack)));
        assertThat(enrollment.getMeasurements()).containsOnly(measurementBack);
        assertThat(measurementBack.getEnrollment()).isEqualTo(enrollment);

        enrollment.setMeasurements(new HashSet<>());
        assertThat(enrollment.getMeasurements()).doesNotContain(measurementBack);
        assertThat(measurementBack.getEnrollment()).isNull();
    }
}
