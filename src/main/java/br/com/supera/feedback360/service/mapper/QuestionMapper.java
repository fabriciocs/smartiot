package br.com.supera.feedback360.service.mapper;

import br.com.supera.feedback360.domain.FeedbackForm;
import br.com.supera.feedback360.domain.Question;
import br.com.supera.feedback360.service.dto.FeedbackFormDTO;
import br.com.supera.feedback360.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "feedbackForm", source = "feedbackForm", qualifiedByName = "feedbackFormTitle")
    QuestionDTO toDto(Question s);

    @Named("feedbackFormTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FeedbackFormDTO toDtoFeedbackFormTitle(FeedbackForm feedbackForm);
}
