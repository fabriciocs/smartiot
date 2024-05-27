package br.com.supera.feedback360.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A IntegrationConfig.
 */
@Table("integration_config")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegrationConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("service_name")
    private String serviceName;

    @NotNull(message = "must not be null")
    @Column("config_data")
    private String configData;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Transient
    private ExternalSystem externalSystem;

    @Column("external_system_id")
    private Long externalSystemId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IntegrationConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public IntegrationConfig serviceName(String serviceName) {
        this.setServiceName(serviceName);
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getConfigData() {
        return this.configData;
    }

    public IntegrationConfig configData(String configData) {
        this.setConfigData(configData);
        return this;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public IntegrationConfig createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public IntegrationConfig updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ExternalSystem getExternalSystem() {
        return this.externalSystem;
    }

    public void setExternalSystem(ExternalSystem externalSystem) {
        this.externalSystem = externalSystem;
        this.externalSystemId = externalSystem != null ? externalSystem.getId() : null;
    }

    public IntegrationConfig externalSystem(ExternalSystem externalSystem) {
        this.setExternalSystem(externalSystem);
        return this;
    }

    public Long getExternalSystemId() {
        return this.externalSystemId;
    }

    public void setExternalSystemId(Long externalSystem) {
        this.externalSystemId = externalSystem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntegrationConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((IntegrationConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntegrationConfig{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", configData='" + getConfigData() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
