package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CostCenterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostCenterDTO.class);
        CostCenterDTO costCenterDTO1 = new CostCenterDTO();
        costCenterDTO1.setId(1L);
        CostCenterDTO costCenterDTO2 = new CostCenterDTO();
        assertThat(costCenterDTO1).isNotEqualTo(costCenterDTO2);
        costCenterDTO2.setId(costCenterDTO1.getId());
        assertThat(costCenterDTO1).isEqualTo(costCenterDTO2);
        costCenterDTO2.setId(2L);
        assertThat(costCenterDTO1).isNotEqualTo(costCenterDTO2);
        costCenterDTO1.setId(null);
        assertThat(costCenterDTO1).isNotEqualTo(costCenterDTO2);
    }
}
