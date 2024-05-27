package br.com.supera.feedback360.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.feedback360.domain.IntegrationConfig} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegrationConfigDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String serviceName;

    @NotNull(message = "must not be null")
    private String configData;

    @NotNull(message = "must not be null")
    private Instant createdAt;

    private Instant updatedAt;

    private ExternalSystemDTO externalSystem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ExternalSystemDTO getExternalSystem() {
        return externalSystem;
    }

    public void setExternalSystem(ExternalSystemDTO externalSystem) {
        this.externalSystem = externalSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntegrationConfigDTO)) {
            return false;
        }

        IntegrationConfigDTO integrationConfigDTO = (IntegrationConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, integrationConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntegrationConfigDTO{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", configData='" + getConfigData() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", externalSystem=" + getExternalSystem() +
            "}";
    }
}
