package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Alert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlertDTO implements Serializable {

    private Long id;

    @NotNull
    private String alertType;

    @NotNull
    private String description;

    @NotNull
    private ZonedDateTime createdDate;

    private ConsumerDTO consumer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ConsumerDTO getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerDTO consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertDTO)) {
            return false;
        }

        AlertDTO alertDTO = (AlertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertDTO{" +
            "id=" + getId() +
            ", alertType='" + getAlertType() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", consumer=" + getConsumer() +
            "}";
    }
}
