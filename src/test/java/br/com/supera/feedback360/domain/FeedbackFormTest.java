package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.FeedbackFormTestSamples.*;
import static br.com.supera.feedback360.domain.SysUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackFormTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackForm.class);
        FeedbackForm feedbackForm1 = getFeedbackFormSample1();
        FeedbackForm feedbackForm2 = new FeedbackForm();
        assertThat(feedbackForm1).isNotEqualTo(feedbackForm2);

        feedbackForm2.setId(feedbackForm1.getId());
        assertThat(feedbackForm1).isEqualTo(feedbackForm2);

        feedbackForm2 = getFeedbackFormSample2();
        assertThat(feedbackForm1).isNotEqualTo(feedbackForm2);
    }

    @Test
    void creatorTest() throws Exception {
        FeedbackForm feedbackForm = getFeedbackFormRandomSampleGenerator();
        SysUser sysUserBack = getSysUserRandomSampleGenerator();

        feedbackForm.setCreator(sysUserBack);
        assertThat(feedbackForm.getCreator()).isEqualTo(sysUserBack);

        feedbackForm.creator(null);
        assertThat(feedbackForm.getCreator()).isNull();
    }
}
