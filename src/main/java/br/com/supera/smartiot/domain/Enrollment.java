package br.com.supera.smartiot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Enrollment.
 */
@Entity
@Table(name = "enrollment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "enrollment_type", nullable = false)
    private String enrollmentType;

    @NotNull
    @Column(name = "enrollment_date", nullable = false)
    private ZonedDateTime enrollmentDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "enrollment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "enrollment" }, allowSetters = true)
    private Set<Measurement> measurements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enrollment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnrollmentType() {
        return this.enrollmentType;
    }

    public Enrollment enrollmentType(String enrollmentType) {
        this.setEnrollmentType(enrollmentType);
        return this;
    }

    public void setEnrollmentType(String enrollmentType) {
        this.enrollmentType = enrollmentType;
    }

    public ZonedDateTime getEnrollmentDate() {
        return this.enrollmentDate;
    }

    public Enrollment enrollmentDate(ZonedDateTime enrollmentDate) {
        this.setEnrollmentDate(enrollmentDate);
        return this;
    }

    public void setEnrollmentDate(ZonedDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Set<Measurement> getMeasurements() {
        return this.measurements;
    }

    public void setMeasurements(Set<Measurement> measurements) {
        if (this.measurements != null) {
            this.measurements.forEach(i -> i.setEnrollment(null));
        }
        if (measurements != null) {
            measurements.forEach(i -> i.setEnrollment(this));
        }
        this.measurements = measurements;
    }

    public Enrollment measurements(Set<Measurement> measurements) {
        this.setMeasurements(measurements);
        return this;
    }

    public Enrollment addMeasurement(Measurement measurement) {
        this.measurements.add(measurement);
        measurement.setEnrollment(this);
        return this;
    }

    public Enrollment removeMeasurement(Measurement measurement) {
        this.measurements.remove(measurement);
        measurement.setEnrollment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enrollment)) {
            return false;
        }
        return getId() != null && getId().equals(((Enrollment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enrollment{" +
            "id=" + getId() +
            ", enrollmentType='" + getEnrollmentType() + "'" +
            ", enrollmentDate='" + getEnrollmentDate() + "'" +
            "}";
    }
}
