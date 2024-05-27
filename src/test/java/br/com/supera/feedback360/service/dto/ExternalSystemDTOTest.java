package br.com.supera.feedback360.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExternalSystemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalSystemDTO.class);
        ExternalSystemDTO externalSystemDTO1 = new ExternalSystemDTO();
        externalSystemDTO1.setId(1L);
        ExternalSystemDTO externalSystemDTO2 = new ExternalSystemDTO();
        assertThat(externalSystemDTO1).isNotEqualTo(externalSystemDTO2);
        externalSystemDTO2.setId(externalSystemDTO1.getId());
        assertThat(externalSystemDTO1).isEqualTo(externalSystemDTO2);
        externalSystemDTO2.setId(2L);
        assertThat(externalSystemDTO1).isNotEqualTo(externalSystemDTO2);
        externalSystemDTO1.setId(null);
        assertThat(externalSystemDTO1).isNotEqualTo(externalSystemDTO2);
    }
}
