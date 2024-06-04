package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ResourceGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceGroup.class);
        ResourceGroup resourceGroup1 = getResourceGroupSample1();
        ResourceGroup resourceGroup2 = new ResourceGroup();
        assertThat(resourceGroup1).isNotEqualTo(resourceGroup2);

        resourceGroup2.setId(resourceGroup1.getId());
        assertThat(resourceGroup1).isEqualTo(resourceGroup2);

        resourceGroup2 = getResourceGroupSample2();
        assertThat(resourceGroup1).isNotEqualTo(resourceGroup2);
    }
}
