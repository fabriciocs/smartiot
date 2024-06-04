package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Measurement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MeasurementDTO implements Serializable {

    private Long id;

    @NotNull
    private String measurementType;

    @NotNull
    private String value;

    @NotNull
    private ZonedDateTime measurementTime;

    private EnrollmentDTO enrollment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(ZonedDateTime measurementTime) {
        this.measurementTime = measurementTime;
    }

    public EnrollmentDTO getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(EnrollmentDTO enrollment) {
        this.enrollment = enrollment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeasurementDTO)) {
            return false;
        }

        MeasurementDTO measurementDTO = (MeasurementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, measurementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeasurementDTO{" +
            "id=" + getId() +
            ", measurementType='" + getMeasurementType() + "'" +
            ", value='" + getValue() + "'" +
            ", measurementTime='" + getMeasurementTime() + "'" +
            ", enrollment=" + getEnrollment() +
            "}";
    }
}
