package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.NotificationSettings;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.service.dto.NotificationSettingsDTO;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationSettings} and its DTO {@link NotificationSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationSettingsMapper extends EntityMapper<NotificationSettingsDTO, NotificationSettings> {
    @Mapping(target = "user", source = "user", qualifiedByName = "sysUserName")
    NotificationSettingsDTO toDto(NotificationSettings s);

    @Named("sysUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SysUserDTO toDtoSysUserName(SysUser sysUser);
}
