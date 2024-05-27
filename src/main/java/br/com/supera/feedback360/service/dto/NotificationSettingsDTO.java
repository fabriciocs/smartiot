package br.com.supera.feedback360.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.feedback360.domain.NotificationSettings} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationSettingsDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String preferences;

    private SysUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public SysUserDTO getUser() {
        return user;
    }

    public void setUser(SysUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationSettingsDTO)) {
            return false;
        }

        NotificationSettingsDTO notificationSettingsDTO = (NotificationSettingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationSettingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationSettingsDTO{" +
            "id=" + getId() +
            ", preferences='" + getPreferences() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
