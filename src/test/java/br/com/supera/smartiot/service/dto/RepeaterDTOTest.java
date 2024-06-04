package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepeaterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepeaterDTO.class);
        RepeaterDTO repeaterDTO1 = new RepeaterDTO();
        repeaterDTO1.setId(1L);
        RepeaterDTO repeaterDTO2 = new RepeaterDTO();
        assertThat(repeaterDTO1).isNotEqualTo(repeaterDTO2);
        repeaterDTO2.setId(repeaterDTO1.getId());
        assertThat(repeaterDTO1).isEqualTo(repeaterDTO2);
        repeaterDTO2.setId(2L);
        assertThat(repeaterDTO1).isNotEqualTo(repeaterDTO2);
        repeaterDTO1.setId(null);
        assertThat(repeaterDTO1).isNotEqualTo(repeaterDTO2);
    }
}
