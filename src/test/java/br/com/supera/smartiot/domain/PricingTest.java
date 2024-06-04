package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.PricingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PricingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pricing.class);
        Pricing pricing1 = getPricingSample1();
        Pricing pricing2 = new Pricing();
        assertThat(pricing1).isNotEqualTo(pricing2);

        pricing2.setId(pricing1.getId());
        assertThat(pricing1).isEqualTo(pricing2);

        pricing2 = getPricingSample2();
        assertThat(pricing1).isNotEqualTo(pricing2);
    }
}
