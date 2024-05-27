package br.com.supera.feedback360.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A NotificationSettings.
 */
@Table("notification_settings")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
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

    public NotificationSettings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferences() {
        return this.preferences;
    }

    public NotificationSettings preferences(String preferences) {
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

    public NotificationSettings user(SysUser sysUser) {
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
        if (!(o instanceof NotificationSettings)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationSettings) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationSettings{" +
            "id=" + getId() +
            ", preferences='" + getPreferences() + "'" +
            "}";
    }
}
