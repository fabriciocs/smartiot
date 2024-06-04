package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeterDTO.class);
        MeterDTO meterDTO1 = new MeterDTO();
        meterDTO1.setId(1L);
        MeterDTO meterDTO2 = new MeterDTO();
        assertThat(meterDTO1).isNotEqualTo(meterDTO2);
        meterDTO2.setId(meterDTO1.getId());
        assertThat(meterDTO1).isEqualTo(meterDTO2);
        meterDTO2.setId(2L);
        assertThat(meterDTO1).isNotEqualTo(meterDTO2);
        meterDTO1.setId(null);
        assertThat(meterDTO1).isNotEqualTo(meterDTO2);
    }
}
