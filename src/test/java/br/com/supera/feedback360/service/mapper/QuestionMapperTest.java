package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.QuestionAsserts.*;
import static br.com.supera.feedback360.domain.QuestionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionMapperTest {

    private QuestionMapper questionMapper;

    @BeforeEach
    void setUp() {
        questionMapper = new QuestionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuestionSample1();
        var actual = questionMapper.toEntity(questionMapper.toDto(expected));
        assertQuestionAllPropertiesEquals(expected, actual);
    }
}
