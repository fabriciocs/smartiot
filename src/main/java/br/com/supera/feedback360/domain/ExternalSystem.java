package br.com.supera.feedback360.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ExternalSystem.
 */
@Table("external_system")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("api_endpoint")
    private String apiEndpoint;

    @NotNull(message = "must not be null")
    @Column("auth_details")
    private String authDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExternalSystem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ExternalSystem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ExternalSystem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiEndpoint() {
        return this.apiEndpoint;
    }

    public ExternalSystem apiEndpoint(String apiEndpoint) {
        this.setApiEndpoint(apiEndpoint);
        return this;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getAuthDetails() {
        return this.authDetails;
    }

    public ExternalSystem authDetails(String authDetails) {
        this.setAuthDetails(authDetails);
        return this;
    }

    public void setAuthDetails(String authDetails) {
        this.authDetails = authDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalSystem)) {
            return false;
        }
        return getId() != null && getId().equals(((ExternalSystem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalSystem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", apiEndpoint='" + getApiEndpoint() + "'" +
            ", authDetails='" + getAuthDetails() + "'" +
            "}";
    }
}
