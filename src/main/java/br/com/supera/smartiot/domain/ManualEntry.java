package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ManualEntry.
 */
@Entity
@Table(name = "manual_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "entry_type", nullable = false)
    private String entryType;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "entry_date", nullable = false)
    private ZonedDateTime entryDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ManualEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntryType() {
        return this.entryType;
    }

    public ManualEntry entryType(String entryType) {
        this.setEntryType(entryType);
        return this;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getValue() {
        return this.value;
    }

    public ManualEntry value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getEntryDate() {
        return this.entryDate;
    }

    public ManualEntry entryDate(ZonedDateTime entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(ZonedDateTime entryDate) {
        this.entryDate = entryDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((ManualEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualEntry{" +
            "id=" + getId() +
            ", entryType='" + getEntryType() + "'" +
            ", value='" + getValue() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            "}";
    }
}
