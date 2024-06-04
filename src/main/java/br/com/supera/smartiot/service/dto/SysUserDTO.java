package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.SysUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SysUserDTO implements Serializable {

    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysUserDTO)) {
            return false;
        }

        SysUserDTO sysUserDTO = (SysUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sysUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysUserDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
