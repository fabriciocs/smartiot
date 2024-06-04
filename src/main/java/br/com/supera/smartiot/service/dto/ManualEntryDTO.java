package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.ManualEntry} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private String entryType;

    @NotNull
    private String value;

    @NotNull
    private ZonedDateTime entryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(ZonedDateTime entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualEntryDTO)) {
            return false;
        }

        ManualEntryDTO manualEntryDTO = (ManualEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manualEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualEntryDTO{" +
            "id=" + getId() +
            ", entryType='" + getEntryType() + "'" +
            ", value='" + getValue() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            "}";
    }
}
