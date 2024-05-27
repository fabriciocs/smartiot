package br.com.supera.feedback360.domain;

import static br.com.supera.feedback360.domain.AnalyticsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.feedback360.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnalyticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Analytics.class);
        Analytics analytics1 = getAnalyticsSample1();
        Analytics analytics2 = new Analytics();
        assertThat(analytics1).isNotEqualTo(analytics2);

        analytics2.setId(analytics1.getId());
        assertThat(analytics1).isEqualTo(analytics2);

        analytics2 = getAnalyticsSample2();
        assertThat(analytics1).isNotEqualTo(analytics2);
    }
}
