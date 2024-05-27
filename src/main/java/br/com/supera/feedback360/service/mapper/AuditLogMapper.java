package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.AuditLog;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.service.dto.AuditLogDTO;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuditLog} and its DTO {@link AuditLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuditLogMapper extends EntityMapper<AuditLogDTO, AuditLog> {
    @Mapping(target = "user", source = "user", qualifiedByName = "sysUserName")
    AuditLogDTO toDto(AuditLog s);

    @Named("sysUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SysUserDTO toDtoSysUserName(SysUser sysUser);
}
