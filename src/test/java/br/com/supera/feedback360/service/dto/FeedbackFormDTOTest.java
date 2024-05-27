package br.com.supera.feedback360.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackFormDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackFormDTO.class);
        FeedbackFormDTO feedbackFormDTO1 = new FeedbackFormDTO();
        feedbackFormDTO1.setId(1L);
        FeedbackFormDTO feedbackFormDTO2 = new FeedbackFormDTO();
        assertThat(feedbackFormDTO1).isNotEqualTo(feedbackFormDTO2);
        feedbackFormDTO2.setId(feedbackFormDTO1.getId());
        assertThat(feedbackFormDTO1).isEqualTo(feedbackFormDTO2);
        feedbackFormDTO2.setId(2L);
        assertThat(feedbackFormDTO1).isNotEqualTo(feedbackFormDTO2);
        feedbackFormDTO1.setId(null);
        assertThat(feedbackFormDTO1).isNotEqualTo(feedbackFormDTO2);
    }
}
