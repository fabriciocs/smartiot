package br.com.supera.feedback360.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.feedback360.domain.ExternalSystem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalSystemDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String description;

    @NotNull(message = "must not be null")
    private String apiEndpoint;

    @NotNull(message = "must not be null")
    private String authDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getAuthDetails() {
        return authDetails;
    }

    public void setAuthDetails(String authDetails) {
        this.authDetails = authDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalSystemDTO)) {
            return false;
        }

        ExternalSystemDTO externalSystemDTO = (ExternalSystemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, externalSystemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalSystemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", apiEndpoint='" + getApiEndpoint() + "'" +
            ", authDetails='" + getAuthDetails() + "'" +
            "}";
    }
}
