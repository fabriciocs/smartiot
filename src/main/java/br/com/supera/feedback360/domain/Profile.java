package br.com.supera.feedback360.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Profile.
 */
@Table("profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("phone_number")
    private String phoneNumber;

    @Column("address")
    private String address;

    @Column("profile_picture")
    private String profilePicture;

    @Column("preferences")
    private String preferences;

    @Transient
    @JsonIgnoreProperties(value = { "role" }, allowSetters = true)
    private SysUser user;

    @Column("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Profile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Profile phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public Profile address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public Profile profilePicture(String profilePicture) {
        this.setProfilePicture(profilePicture);
        return this;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPreferences() {
        return this.preferences;
    }

    public Profile preferences(String preferences) {
        this.setPreferences(preferences);
        return this;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public SysUser getUser() {
        return this.user;
    }

    public void setUser(SysUser sysUser) {
        this.user = sysUser;
        this.userId = sysUser != null ? sysUser.getId() : null;
    }

    public Profile user(SysUser sysUser) {
        this.setUser(sysUser);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long sysUser) {
        this.userId = sysUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return getId() != null && getId().equals(((Profile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", profilePicture='" + getProfilePicture() + "'" +
            ", preferences='" + getPreferences() + "'" +
            "}";
    }
}
