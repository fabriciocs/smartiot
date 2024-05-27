package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.FeedbackFormTestSamples.*;
import static br.com.supera.feedback360.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void feedbackFormTest() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        FeedbackForm feedbackFormBack = getFeedbackFormRandomSampleGenerator();

        question.setFeedbackForm(feedbackFormBack);
        assertThat(question.getFeedbackForm()).isEqualTo(feedbackFormBack);

        question.feedbackForm(null);
        assertThat(question.getFeedbackForm()).isNull();
    }
}
