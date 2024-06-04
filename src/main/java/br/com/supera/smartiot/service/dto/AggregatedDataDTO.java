package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.AggregatedData} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AggregatedDataDTO implements Serializable {

    private Long id;

    @NotNull
    private String dataType;

    @NotNull
    private String value;

    @NotNull
    private ZonedDateTime aggregationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getAggregationTime() {
        return aggregationTime;
    }

    public void setAggregationTime(ZonedDateTime aggregationTime) {
        this.aggregationTime = aggregationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AggregatedDataDTO)) {
            return false;
        }

        AggregatedDataDTO aggregatedDataDTO = (AggregatedDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aggregatedDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AggregatedDataDTO{" +
            "id=" + getId() +
            ", dataType='" + getDataType() + "'" +
            ", value='" + getValue() + "'" +
            ", aggregationTime='" + getAggregationTime() + "'" +
            "}";
    }
}
