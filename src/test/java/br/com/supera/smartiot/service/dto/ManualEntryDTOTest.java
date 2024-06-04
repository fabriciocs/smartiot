package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualEntryDTO.class);
        ManualEntryDTO manualEntryDTO1 = new ManualEntryDTO();
        manualEntryDTO1.setId(1L);
        ManualEntryDTO manualEntryDTO2 = new ManualEntryDTO();
        assertThat(manualEntryDTO1).isNotEqualTo(manualEntryDTO2);
        manualEntryDTO2.setId(manualEntryDTO1.getId());
        assertThat(manualEntryDTO1).isEqualTo(manualEntryDTO2);
        manualEntryDTO2.setId(2L);
        assertThat(manualEntryDTO1).isNotEqualTo(manualEntryDTO2);
        manualEntryDTO1.setId(null);
        assertThat(manualEntryDTO1).isNotEqualTo(manualEntryDTO2);
    }
}
