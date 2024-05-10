package br.com.supera.smartiot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConfiguracaoAlerta.
 */
@Entity
@Table(name = "configuracao_alerta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfiguracaoAlerta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "limite", precision = 21, scale = 2)
    private BigDecimal limite;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "configuracaoAlertas", "cliente", "dadoSensores" }, allowSetters = true)
    private Sensor sensor;

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
        this.limite = limite;
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
    }

    public ConfiguracaoAlerta sensor(Sensor sensor) {
        this.setSensor(sensor);
        return this;
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
