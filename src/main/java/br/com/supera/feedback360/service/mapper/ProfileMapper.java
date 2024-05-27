package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.Profile;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.service.dto.ProfileDTO;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profile} and its DTO {@link ProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "sysUserName")
    ProfileDTO toDto(Profile s);

    @Named("sysUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SysUserDTO toDtoSysUserName(SysUser sysUser);
}
