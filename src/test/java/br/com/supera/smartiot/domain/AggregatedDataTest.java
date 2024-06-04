package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.AggregatedDataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AggregatedDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AggregatedData.class);
        AggregatedData aggregatedData1 = getAggregatedDataSample1();
        AggregatedData aggregatedData2 = new AggregatedData();
        assertThat(aggregatedData1).isNotEqualTo(aggregatedData2);

        aggregatedData2.setId(aggregatedData1.getId());
        assertThat(aggregatedData1).isEqualTo(aggregatedData2);

        aggregatedData2 = getAggregatedDataSample2();
        assertThat(aggregatedData1).isNotEqualTo(aggregatedData2);
    }
}
