package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.Notification;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.service.dto.NotificationDTO;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "sysUserName")
    NotificationDTO toDto(Notification s);

    @Named("sysUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SysUserDTO toDtoSysUserName(SysUser sysUser);
}
