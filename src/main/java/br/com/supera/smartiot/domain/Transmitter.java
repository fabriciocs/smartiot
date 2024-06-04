package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transmitter.
 */
@Entity
@Table(name = "transmitter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transmitter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @NotNull
    @Column(name = "frequency", nullable = false)
    private Integer frequency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transmitter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Transmitter serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getFrequency() {
        return this.frequency;
    }

    public Transmitter frequency(Integer frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transmitter)) {
            return false;
        }
        return getId() != null && getId().equals(((Transmitter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transmitter{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", frequency=" + getFrequency() +
            "}";
    }
}
