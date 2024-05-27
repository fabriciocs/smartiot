package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.FeedbackFormAsserts.*;
import static br.com.supera.feedback360.domain.FeedbackFormTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedbackFormMapperTest {

    private FeedbackFormMapper feedbackFormMapper;

    @BeforeEach
    void setUp() {
        feedbackFormMapper = new FeedbackFormMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFeedbackFormSample1();
        var actual = feedbackFormMapper.toEntity(feedbackFormMapper.toDto(expected));
        assertFeedbackFormAllPropertiesEquals(expected, actual);
    }
}
