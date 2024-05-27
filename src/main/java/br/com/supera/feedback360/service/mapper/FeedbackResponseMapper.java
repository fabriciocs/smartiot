package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.FeedbackForm;
import br.com.supera.feedback360.domain.FeedbackResponse;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.service.dto.FeedbackFormDTO;
import br.com.supera.feedback360.service.dto.FeedbackResponseDTO;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FeedbackResponse} and its DTO {@link FeedbackResponseDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackResponseMapper extends EntityMapper<FeedbackResponseDTO, FeedbackResponse> {
    @Mapping(target = "form", source = "form", qualifiedByName = "feedbackFormTitle")
    @Mapping(target = "user", source = "user", qualifiedByName = "sysUserName")
    FeedbackResponseDTO toDto(FeedbackResponse s);

    @Named("feedbackFormTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FeedbackFormDTO toDtoFeedbackFormTitle(FeedbackForm feedbackForm);

    @Named("sysUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SysUserDTO toDtoSysUserName(SysUser sysUser);
}
