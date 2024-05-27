package br.com.supera.feedback360.service.mapper;

import static br.com.supera.feedback360.domain.FeedbackResponseAsserts.*;
import static br.com.supera.feedback360.domain.FeedbackResponseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedbackResponseMapperTest {

    private FeedbackResponseMapper feedbackResponseMapper;

    @BeforeEach
    void setUp() {
        feedbackResponseMapper = new FeedbackResponseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFeedbackResponseSample1();
        var actual = feedbackResponseMapper.toEntity(feedbackResponseMapper.toDto(expected));
        assertFeedbackResponseAllPropertiesEquals(expected, actual);
    }
}
