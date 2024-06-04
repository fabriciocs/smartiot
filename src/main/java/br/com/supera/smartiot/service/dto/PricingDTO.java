package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Pricing} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PricingDTO implements Serializable {

    private Long id;

    @NotNull
    private String serviceType;

    @NotNull
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PricingDTO)) {
            return false;
        }

        PricingDTO pricingDTO = (PricingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pricingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PricingDTO{" +
            "id=" + getId() +
            ", serviceType='" + getServiceType() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
