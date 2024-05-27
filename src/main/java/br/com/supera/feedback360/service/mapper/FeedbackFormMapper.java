package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.FeedbackForm;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.service.dto.FeedbackFormDTO;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FeedbackForm} and its DTO {@link FeedbackFormDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackFormMapper extends EntityMapper<FeedbackFormDTO, FeedbackForm> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "sysUserName")
    FeedbackFormDTO toDto(FeedbackForm s);

    @Named("sysUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SysUserDTO toDtoSysUserName(SysUser sysUser);
}
