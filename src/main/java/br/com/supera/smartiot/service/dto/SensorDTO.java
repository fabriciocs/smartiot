package br.com.supera.smartiot.service.dto;

import br.com.supera.smartiot.domain.enumeration.TipoSensor;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Sensor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SensorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nome;

    @NotNull
    private TipoSensor tipo;

    private String configuracao;

    private ConfiguracaoAlertaDTO configuracaoAlertas;

    private DadoSensorDTO dadoSensores;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoSensor getTipo() {
        return tipo;
    }

    public void setTipo(TipoSensor tipo) {
        this.tipo = tipo;
    }

    public String getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(String configuracao) {
        this.configuracao = configuracao;
    }

    public ConfiguracaoAlertaDTO getConfiguracaoAlertas() {
        return configuracaoAlertas;
    }

    public void setConfiguracaoAlertas(ConfiguracaoAlertaDTO configuracaoAlertas) {
        this.configuracaoAlertas = configuracaoAlertas;
    }

    public DadoSensorDTO getDadoSensores() {
        return dadoSensores;
    }

    public void setDadoSensores(DadoSensorDTO dadoSensores) {
        this.dadoSensores = dadoSensores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SensorDTO)) {
            return false;
        }

        SensorDTO sensorDTO = (SensorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sensorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SensorDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", configuracao='" + getConfiguracao() + "'" +
            ", configuracaoAlertas=" + getConfiguracaoAlertas() +
            ", dadoSensores=" + getDadoSensores() +
            "}";
    }
}
