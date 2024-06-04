package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AggregatedDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AggregatedDataDTO.class);
        AggregatedDataDTO aggregatedDataDTO1 = new AggregatedDataDTO();
        aggregatedDataDTO1.setId(1L);
        AggregatedDataDTO aggregatedDataDTO2 = new AggregatedDataDTO();
        assertThat(aggregatedDataDTO1).isNotEqualTo(aggregatedDataDTO2);
        aggregatedDataDTO2.setId(aggregatedDataDTO1.getId());
        assertThat(aggregatedDataDTO1).isEqualTo(aggregatedDataDTO2);
        aggregatedDataDTO2.setId(2L);
        assertThat(aggregatedDataDTO1).isNotEqualTo(aggregatedDataDTO2);
        aggregatedDataDTO1.setId(null);
        assertThat(aggregatedDataDTO1).isNotEqualTo(aggregatedDataDTO2);
    }
}
