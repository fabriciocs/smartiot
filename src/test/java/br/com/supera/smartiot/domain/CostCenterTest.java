package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.CostCenterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CostCenterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostCenter.class);
        CostCenter costCenter1 = getCostCenterSample1();
        CostCenter costCenter2 = new CostCenter();
        assertThat(costCenter1).isNotEqualTo(costCenter2);

        costCenter2.setId(costCenter1.getId());
        assertThat(costCenter1).isEqualTo(costCenter2);

        costCenter2 = getCostCenterSample2();
        assertThat(costCenter1).isNotEqualTo(costCenter2);
    }
}
