package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.ConfiguracaoAlerta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfiguracaoAlertaDTO implements Serializable {

    private Long id;

    private BigDecimal limite;

    @NotNull
    @Pattern(regexp = "^[^@\\\\s]+@[^@\\\\s]+\\\\.[^@\\\\s]+$")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfiguracaoAlertaDTO)) {
            return false;
        }

        ConfiguracaoAlertaDTO configuracaoAlertaDTO = (ConfiguracaoAlertaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, configuracaoAlertaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfiguracaoAlertaDTO{" +
            "id=" + getId() +
            ", limite=" + getLimite() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
