package br.com.supera.smartiot.domain;

import br.com.supera.smartiot.domain.enumeration.TipoSensor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sensor.
 */
@Entity
@Table(name = "sensor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoSensor tipo;

    @Column(name = "configuracao")
    private String configuracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sensors" }, allowSetters = true)
    private ConfiguracaoAlerta configuracaoAlertas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sensors" }, allowSetters = true)
    private DadoSensor dadoSensores;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sensores")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sensores" }, allowSetters = true)
    private Set<Cliente> clientes = new HashSet<>();

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

    public ConfiguracaoAlerta getConfiguracaoAlertas() {
        return this.configuracaoAlertas;
    }

    public void setConfiguracaoAlertas(ConfiguracaoAlerta configuracaoAlerta) {
        this.configuracaoAlertas = configuracaoAlerta;
    }

    public Sensor configuracaoAlertas(ConfiguracaoAlerta configuracaoAlerta) {
        this.setConfiguracaoAlertas(configuracaoAlerta);
        return this;
    }

    public DadoSensor getDadoSensores() {
        return this.dadoSensores;
    }

    public void setDadoSensores(DadoSensor dadoSensor) {
        this.dadoSensores = dadoSensor;
    }

    public Sensor dadoSensores(DadoSensor dadoSensor) {
        this.setDadoSensores(dadoSensor);
        return this;
    }

    public Set<Cliente> getClientes() {
        return this.clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        if (this.clientes != null) {
            this.clientes.forEach(i -> i.setSensores(null));
        }
        if (clientes != null) {
            clientes.forEach(i -> i.setSensores(this));
        }
        this.clientes = clientes;
    }

    public Sensor clientes(Set<Cliente> clientes) {
        this.setClientes(clientes);
        return this;
    }

    public Sensor addCliente(Cliente cliente) {
        this.clientes.add(cliente);
        cliente.setSensores(this);
        return this;
    }

    public Sensor removeCliente(Cliente cliente) {
        this.clientes.remove(cliente);
        cliente.setSensores(null);
        return this;
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
