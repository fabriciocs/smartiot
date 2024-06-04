package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Enrollment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnrollmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String enrollmentType;

    @NotNull
    private ZonedDateTime enrollmentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnrollmentType() {
        return enrollmentType;
    }

    public void setEnrollmentType(String enrollmentType) {
        this.enrollmentType = enrollmentType;
    }

    public ZonedDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(ZonedDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnrollmentDTO)) {
            return false;
        }

        EnrollmentDTO enrollmentDTO = (EnrollmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enrollmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnrollmentDTO{" +
            "id=" + getId() +
            ", enrollmentType='" + getEnrollmentType() + "'" +
            ", enrollmentDate='" + getEnrollmentDate() + "'" +
            "}";
    }
}
