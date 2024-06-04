package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConcentratorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConcentratorDTO.class);
        ConcentratorDTO concentratorDTO1 = new ConcentratorDTO();
        concentratorDTO1.setId(1L);
        ConcentratorDTO concentratorDTO2 = new ConcentratorDTO();
        assertThat(concentratorDTO1).isNotEqualTo(concentratorDTO2);
        concentratorDTO2.setId(concentratorDTO1.getId());
        assertThat(concentratorDTO1).isEqualTo(concentratorDTO2);
        concentratorDTO2.setId(2L);
        assertThat(concentratorDTO1).isNotEqualTo(concentratorDTO2);
        concentratorDTO1.setId(null);
        assertThat(concentratorDTO1).isNotEqualTo(concentratorDTO2);
    }
}
