package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Repeater} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RepeaterDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    @NotNull
    private Integer range;

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

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepeaterDTO)) {
            return false;
        }

        RepeaterDTO repeaterDTO = (RepeaterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, repeaterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RepeaterDTO{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", range=" + getRange() +
            "}";
    }
}
