package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Concentrator} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConcentratorDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    @NotNull
    private Integer capacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConcentratorDTO)) {
            return false;
        }

        ConcentratorDTO concentratorDTO = (ConcentratorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, concentratorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConcentratorDTO{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", capacity=" + getCapacity() +
            "}";
    }
}
