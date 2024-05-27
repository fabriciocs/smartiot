package br.com.supera.feedback360.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DadoSensorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DadoSensorDTO.class);
        DadoSensorDTO dadoSensorDTO1 = new DadoSensorDTO();
        dadoSensorDTO1.setId(1L);
        DadoSensorDTO dadoSensorDTO2 = new DadoSensorDTO();
        assertThat(dadoSensorDTO1).isNotEqualTo(dadoSensorDTO2);
        dadoSensorDTO2.setId(dadoSensorDTO1.getId());
        assertThat(dadoSensorDTO1).isEqualTo(dadoSensorDTO2);
        dadoSensorDTO2.setId(2L);
        assertThat(dadoSensorDTO1).isNotEqualTo(dadoSensorDTO2);
        dadoSensorDTO1.setId(null);
        assertThat(dadoSensorDTO1).isNotEqualTo(dadoSensorDTO2);
    }
}
