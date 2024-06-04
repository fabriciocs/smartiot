package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransmitterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransmitterDTO.class);
        TransmitterDTO transmitterDTO1 = new TransmitterDTO();
        transmitterDTO1.setId(1L);
        TransmitterDTO transmitterDTO2 = new TransmitterDTO();
        assertThat(transmitterDTO1).isNotEqualTo(transmitterDTO2);
        transmitterDTO2.setId(transmitterDTO1.getId());
        assertThat(transmitterDTO1).isEqualTo(transmitterDTO2);
        transmitterDTO2.setId(2L);
        assertThat(transmitterDTO1).isNotEqualTo(transmitterDTO2);
        transmitterDTO1.setId(null);
        assertThat(transmitterDTO1).isNotEqualTo(transmitterDTO2);
    }
}
