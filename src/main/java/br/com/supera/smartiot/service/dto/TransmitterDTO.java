package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Transmitter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransmitterDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    @NotNull
    private Integer frequency;

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

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransmitterDTO)) {
            return false;
        }

        TransmitterDTO transmitterDTO = (TransmitterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transmitterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransmitterDTO{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", frequency=" + getFrequency() +
            "}";
    }
}
