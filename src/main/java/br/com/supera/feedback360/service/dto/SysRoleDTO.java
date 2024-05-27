package br.com.supera.feedback360.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.feedback360.domain.SysRole} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SysRoleDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String roleName;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysRoleDTO)) {
            return false;
        }

        SysRoleDTO sysRoleDTO = (SysRoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sysRoleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysRoleDTO{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
