package br.com.supera.feedback360.domain;

import br.com.supera.feedback360.domain.enumeration.TipoSensor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Sensor.
 */
@Table("sensor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("nome")
    private String nome;

    @NotNull(message = "must not be null")
    @Column("tipo")
    private TipoSensor tipo;

    @Column("configuracao")
    private String configuracao;

    @Transient
    @JsonIgnoreProperties(value = { "sensor" }, allowSetters = true)
    private Set<ConfiguracaoAlerta> configuracaoAlertas = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "sensores" }, allowSetters = true)
    private Cliente cliente;

    @Transient
    @JsonIgnoreProperties(value = { "sensors" }, allowSetters = true)
    private DadoSensor dadoSensores;

    @Column("cliente_id")
    private Long clienteId;

    @Column("dado_sensores_id")
    private Long dadoSensoresId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sensor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Sensor nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoSensor getTipo() {
        return this.tipo;
    }

    public Sensor tipo(TipoSensor tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoSensor tipo) {
        this.tipo = tipo;
    }

    public String getConfiguracao() {
        return this.configuracao;
    }

    public Sensor configuracao(String configuracao) {
        this.setConfiguracao(configuracao);
        return this;
    }

    public void setConfiguracao(String configuracao) {
        this.configuracao = configuracao;
    }

    public Set<ConfiguracaoAlerta> getConfiguracaoAlertas() {
        return this.configuracaoAlertas;
    }

    public void setConfiguracaoAlertas(Set<ConfiguracaoAlerta> configuracaoAlertas) {
        if (this.configuracaoAlertas != null) {
            this.configuracaoAlertas.forEach(i -> i.setSensor(null));
        }
        if (configuracaoAlertas != null) {
            configuracaoAlertas.forEach(i -> i.setSensor(this));
        }
        this.configuracaoAlertas = configuracaoAlertas;
    }

    public Sensor configuracaoAlertas(Set<ConfiguracaoAlerta> configuracaoAlertas) {
        this.setConfiguracaoAlertas(configuracaoAlertas);
        return this;
    }

    public Sensor addConfiguracaoAlertas(ConfiguracaoAlerta configuracaoAlerta) {
        this.configuracaoAlertas.add(configuracaoAlerta);
        configuracaoAlerta.setSensor(this);
        return this;
    }

    public Sensor removeConfiguracaoAlertas(ConfiguracaoAlerta configuracaoAlerta) {
        this.configuracaoAlertas.remove(configuracaoAlerta);
        configuracaoAlerta.setSensor(null);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.clienteId = cliente != null ? cliente.getId() : null;
    }

    public Sensor cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public DadoSensor getDadoSensores() {
        return this.dadoSensores;
    }

    public void setDadoSensores(DadoSensor dadoSensor) {
        this.dadoSensores = dadoSensor;
        this.dadoSensoresId = dadoSensor != null ? dadoSensor.getId() : null;
    }

    public Sensor dadoSensores(DadoSensor dadoSensor) {
        this.setDadoSensores(dadoSensor);
        return this;
    }

    public Long getClienteId() {
        return this.clienteId;
    }

    public void setClienteId(Long cliente) {
        this.clienteId = cliente;
    }

    public Long getDadoSensoresId() {
        return this.dadoSensoresId;
    }

    public void setDadoSensoresId(Long dadoSensor) {
        this.dadoSensoresId = dadoSensor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sensor)) {
            return false;
        }
        return getId() != null && getId().equals(((Sensor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", configuracao='" + getConfiguracao() + "'" +
            "}";
    }
}
