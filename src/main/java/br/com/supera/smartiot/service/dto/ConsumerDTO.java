package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.Consumer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumerDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String street;

    @NotNull
    private String neighborhood;

    @NotNull
    private Integer propertyNumber;

    @NotNull
    private String phone;

    @NotNull
    private String email;

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Integer getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(Integer propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumerDTO)) {
            return false;
        }

        ConsumerDTO consumerDTO = (ConsumerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consumerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", street='" + getStreet() + "'" +
            ", neighborhood='" + getNeighborhood() + "'" +
            ", propertyNumber=" + getPropertyNumber() +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
