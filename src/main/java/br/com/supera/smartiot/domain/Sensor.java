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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sensor" }, allowSetters = true)
    private Set<ConfiguracaoAlerta> configuracaoAlertas = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "sensores" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sensors" }, allowSetters = true)
    private DadoSensor dadoSensores;

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
    }

    public Sensor dadoSensores(DadoSensor dadoSensor) {
        this.setDadoSensores(dadoSensor);
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
