package br.com.supera.feedback360.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackResponseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackResponseDTO.class);
        FeedbackResponseDTO feedbackResponseDTO1 = new FeedbackResponseDTO();
        feedbackResponseDTO1.setId(1L);
        FeedbackResponseDTO feedbackResponseDTO2 = new FeedbackResponseDTO();
        assertThat(feedbackResponseDTO1).isNotEqualTo(feedbackResponseDTO2);
        feedbackResponseDTO2.setId(feedbackResponseDTO1.getId());
        assertThat(feedbackResponseDTO1).isEqualTo(feedbackResponseDTO2);
        feedbackResponseDTO2.setId(2L);
        assertThat(feedbackResponseDTO1).isNotEqualTo(feedbackResponseDTO2);
        feedbackResponseDTO1.setId(null);
        assertThat(feedbackResponseDTO1).isNotEqualTo(feedbackResponseDTO2);
    }
}
