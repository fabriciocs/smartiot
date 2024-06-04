package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeasurementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeasurementDTO.class);
        MeasurementDTO measurementDTO1 = new MeasurementDTO();
        measurementDTO1.setId(1L);
        MeasurementDTO measurementDTO2 = new MeasurementDTO();
        assertThat(measurementDTO1).isNotEqualTo(measurementDTO2);
        measurementDTO2.setId(measurementDTO1.getId());
        assertThat(measurementDTO1).isEqualTo(measurementDTO2);
        measurementDTO2.setId(2L);
        assertThat(measurementDTO1).isNotEqualTo(measurementDTO2);
        measurementDTO1.setId(null);
        assertThat(measurementDTO1).isNotEqualTo(measurementDTO2);
    }
}
