package br.com.supera.smartiot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DadoSensor.
 */
@Entity
@Table(name = "dado_sensor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DadoSensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dados")
    private String dados;

    @Column(name = "timestamp")
    private Instant timestamp;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dadoSensores")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "configuracaoAlertas", "dadoSensores", "clientes" }, allowSetters = true)
    private Set<Sensor> sensors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DadoSensor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDados() {
        return this.dados;
    }

    public DadoSensor dados(String dados) {
        this.setDados(dados);
        return this;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public DadoSensor timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Set<Sensor> getSensors() {
        return this.sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        if (this.sensors != null) {
            this.sensors.forEach(i -> i.setDadoSensores(null));
        }
        if (sensors != null) {
            sensors.forEach(i -> i.setDadoSensores(this));
        }
        this.sensors = sensors;
    }

    public DadoSensor sensors(Set<Sensor> sensors) {
        this.setSensors(sensors);
        return this;
    }

    public DadoSensor addSensor(Sensor sensor) {
        this.sensors.add(sensor);
        sensor.setDadoSensores(this);
        return this;
    }

    public DadoSensor removeSensor(Sensor sensor) {
        this.sensors.remove(sensor);
        sensor.setDadoSensores(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DadoSensor)) {
            return false;
        }
        return getId() != null && getId().equals(((DadoSensor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DadoSensor{" +
            "id=" + getId() +
            ", dados='" + getDados() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
