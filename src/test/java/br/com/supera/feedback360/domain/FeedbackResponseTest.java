package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.FeedbackFormTestSamples.*;
import static br.com.supera.feedback360.domain.FeedbackResponseTestSamples.*;
import static br.com.supera.feedback360.domain.SysUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackResponse.class);
        FeedbackResponse feedbackResponse1 = getFeedbackResponseSample1();
        FeedbackResponse feedbackResponse2 = new FeedbackResponse();
        assertThat(feedbackResponse1).isNotEqualTo(feedbackResponse2);

        feedbackResponse2.setId(feedbackResponse1.getId());
        assertThat(feedbackResponse1).isEqualTo(feedbackResponse2);

        feedbackResponse2 = getFeedbackResponseSample2();
        assertThat(feedbackResponse1).isNotEqualTo(feedbackResponse2);
    }

    @Test
    void formTest() throws Exception {
        FeedbackResponse feedbackResponse = getFeedbackResponseRandomSampleGenerator();
        FeedbackForm feedbackFormBack = getFeedbackFormRandomSampleGenerator();

        feedbackResponse.setForm(feedbackFormBack);
        assertThat(feedbackResponse.getForm()).isEqualTo(feedbackFormBack);

        feedbackResponse.form(null);
        assertThat(feedbackResponse.getForm()).isNull();
    }

    @Test
    void userTest() throws Exception {
        FeedbackResponse feedbackResponse = getFeedbackResponseRandomSampleGenerator();
        SysUser sysUserBack = getSysUserRandomSampleGenerator();

        feedbackResponse.setUser(sysUserBack);
        assertThat(feedbackResponse.getUser()).isEqualTo(sysUserBack);

        feedbackResponse.user(null);
        assertThat(feedbackResponse.getUser()).isNull();
    }
}
