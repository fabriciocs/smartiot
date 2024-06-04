package br.com.supera.smartiot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Measurement.
 */
@Entity
@Table(name = "measurement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Measurement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "measurement_type", nullable = false)
    private String measurementType;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "measurement_time", nullable = false)
    private ZonedDateTime measurementTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "measurements" }, allowSetters = true)
    private Enrollment enrollment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Measurement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasurementType() {
        return this.measurementType;
    }

    public Measurement measurementType(String measurementType) {
        this.setMeasurementType(measurementType);
        return this;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public String getValue() {
        return this.value;
    }

    public Measurement value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getMeasurementTime() {
        return this.measurementTime;
    }

    public Measurement measurementTime(ZonedDateTime measurementTime) {
        this.setMeasurementTime(measurementTime);
        return this;
    }

    public void setMeasurementTime(ZonedDateTime measurementTime) {
        this.measurementTime = measurementTime;
    }

    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Measurement enrollment(Enrollment enrollment) {
        this.setEnrollment(enrollment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Measurement)) {
            return false;
        }
        return getId() != null && getId().equals(((Measurement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Measurement{" +
            "id=" + getId() +
            ", measurementType='" + getMeasurementType() + "'" +
            ", value='" + getValue() + "'" +
            ", measurementTime='" + getMeasurementTime() + "'" +
            "}";
    }
}
