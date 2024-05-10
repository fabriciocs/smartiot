package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.DadoSensor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DadoSensorDTO implements Serializable {

    private Long id;

    private String dados;

    private Instant timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DadoSensorDTO)) {
            return false;
        }

        DadoSensorDTO dadoSensorDTO = (DadoSensorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dadoSensorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DadoSensorDTO{" +
            "id=" + getId() +
            ", dados='" + getDados() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
