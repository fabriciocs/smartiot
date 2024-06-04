package br.com.supera.smartiot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Consumer.
 */
@Entity
@Table(name = "consumer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Consumer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "neighborhood", nullable = false)
    private String neighborhood;

    @NotNull
    @Column(name = "property_number", nullable = false)
    private Integer propertyNumber;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "consumer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "consumer" }, allowSetters = true)
    private Set<Alert> alerts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Consumer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Consumer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return this.street;
    }

    public Consumer street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return this.neighborhood;
    }

    public Consumer neighborhood(String neighborhood) {
        this.setNeighborhood(neighborhood);
        return this;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Integer getPropertyNumber() {
        return this.propertyNumber;
    }

    public Consumer propertyNumber(Integer propertyNumber) {
        this.setPropertyNumber(propertyNumber);
        return this;
    }

    public void setPropertyNumber(Integer propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getPhone() {
        return this.phone;
    }

    public Consumer phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Consumer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Alert> getAlerts() {
        return this.alerts;
    }

    public void setAlerts(Set<Alert> alerts) {
        if (this.alerts != null) {
            this.alerts.forEach(i -> i.setConsumer(null));
        }
        if (alerts != null) {
            alerts.forEach(i -> i.setConsumer(this));
        }
        this.alerts = alerts;
    }

    public Consumer alerts(Set<Alert> alerts) {
        this.setAlerts(alerts);
        return this;
    }

    public Consumer addAlert(Alert alert) {
        this.alerts.add(alert);
        alert.setConsumer(this);
        return this;
    }

    public Consumer removeAlert(Alert alert) {
        this.alerts.remove(alert);
        alert.setConsumer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consumer)) {
            return false;
        }
        return getId() != null && getId().equals(((Consumer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consumer{" +
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
