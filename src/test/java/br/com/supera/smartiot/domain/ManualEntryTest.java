package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ManualEntryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualEntry.class);
        ManualEntry manualEntry1 = getManualEntrySample1();
        ManualEntry manualEntry2 = new ManualEntry();
        assertThat(manualEntry1).isNotEqualTo(manualEntry2);

        manualEntry2.setId(manualEntry1.getId());
        assertThat(manualEntry1).isEqualTo(manualEntry2);

        manualEntry2 = getManualEntrySample2();
        assertThat(manualEntry1).isNotEqualTo(manualEntry2);
    }
}
