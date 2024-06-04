package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Repeater.
 */
@Entity
@Table(name = "repeater")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Repeater implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @NotNull
    @Column(name = "jhi_range", nullable = false)
    private Integer range;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Repeater id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Repeater serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getRange() {
        return this.range;
    }

    public Repeater range(Integer range) {
        this.setRange(range);
        return this;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Repeater)) {
            return false;
        }
        return getId() != null && getId().equals(((Repeater) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Repeater{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", range=" + getRange() +
            "}";
    }
}
