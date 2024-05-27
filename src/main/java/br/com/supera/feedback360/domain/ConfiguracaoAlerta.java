package br.com.supera.feedback360.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ConfiguracaoAlerta.
 */
@Table("configuracao_alerta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfiguracaoAlerta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("limite")
    private BigDecimal limite;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column("email")
    private String email;

    @Transient
    @JsonIgnoreProperties(value = { "configuracaoAlertas", "cliente", "dadoSensores" }, allowSetters = true)
    private Sensor sensor;

    @Column("sensor_id")
    private Long sensorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConfiguracaoAlerta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLimite() {
        return this.limite;
    }

    public ConfiguracaoAlerta limite(BigDecimal limite) {
        this.setLimite(limite);
        return this;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite != null ? limite.stripTrailingZeros() : null;
    }

    public String getEmail() {
        return this.email;
    }

    public ConfiguracaoAlerta email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
        this.sensorId = sensor != null ? sensor.getId() : null;
    }

    public ConfiguracaoAlerta sensor(Sensor sensor) {
        this.setSensor(sensor);
        return this;
    }

    public Long getSensorId() {
        return this.sensorId;
    }

    public void setSensorId(Long sensor) {
        this.sensorId = sensor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfiguracaoAlerta)) {
            return false;
        }
        return getId() != null && getId().equals(((ConfiguracaoAlerta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfiguracaoAlerta{" +
            "id=" + getId() +
            ", limite=" + getLimite() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
