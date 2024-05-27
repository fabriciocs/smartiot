package br.com.supera.feedback360.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Analytics.
 */
@Table("analytics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Analytics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type")
    private String type;

    @NotNull(message = "must not be null")
    @Column("data")
    private String data;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Analytics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Analytics type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return this.data;
    }

    public Analytics data(String data) {
        this.setData(data);
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Analytics createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Analytics)) {
            return false;
        }
        return getId() != null && getId().equals(((Analytics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Analytics{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", data='" + getData() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
