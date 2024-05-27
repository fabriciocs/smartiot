package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.SysRole;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.service.dto.SysRoleDTO;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysUser} and its DTO {@link SysUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface SysUserMapper extends EntityMapper<SysUserDTO, SysUser> {
    @Mapping(target = "role", source = "role", qualifiedByName = "sysRoleRoleName")
    SysUserDTO toDto(SysUser s);

    @Named("sysRoleRoleName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "roleName", source = "roleName")
    SysRoleDTO toDtoSysRoleRoleName(SysRole sysRole);
}
