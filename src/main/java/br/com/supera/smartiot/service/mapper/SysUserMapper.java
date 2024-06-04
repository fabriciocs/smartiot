package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.SysUser;
import br.com.supera.smartiot.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysUser} and its DTO {@link SysUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface SysUserMapper extends EntityMapper<SysUserDTO, SysUser> {}
