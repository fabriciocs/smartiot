package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.SysRole;
import br.com.supera.feedback360.service.dto.SysRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysRole} and its DTO {@link SysRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SysRoleMapper extends EntityMapper<SysRoleDTO, SysRole> {}
