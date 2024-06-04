package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.CostCenter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CostCenterDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CostCenterDTO)) {
            return false;
        }

        CostCenterDTO costCenterDTO = (CostCenterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, costCenterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CostCenterDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", budget=" + getBudget() +
            "}";
    }
}
