package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Meter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MeterDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    @NotNull
    private String location;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeterDTO)) {
            return false;
        }

        MeterDTO meterDTO = (MeterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, meterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeterDTO{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
