package br.com.supera.smartiot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AggregatedData.
 */
@Entity
@Table(name = "aggregated_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AggregatedData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_type", nullable = false)
    private String dataType;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "aggregation_time", nullable = false)
    private ZonedDateTime aggregationTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AggregatedData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataType() {
        return this.dataType;
    }

    public AggregatedData dataType(String dataType) {
        this.setDataType(dataType);
        return this;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return this.value;
    }

    public AggregatedData value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getAggregationTime() {
        return this.aggregationTime;
    }

    public AggregatedData aggregationTime(ZonedDateTime aggregationTime) {
        this.setAggregationTime(aggregationTime);
        return this;
    }

    public void setAggregationTime(ZonedDateTime aggregationTime) {
        this.aggregationTime = aggregationTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AggregatedData)) {
            return false;
        }
        return getId() != null && getId().equals(((AggregatedData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AggregatedData{" +
            "id=" + getId() +
            ", dataType='" + getDataType() + "'" +
            ", value='" + getValue() + "'" +
            ", aggregationTime='" + getAggregationTime() + "'" +
            "}";
    }
}
