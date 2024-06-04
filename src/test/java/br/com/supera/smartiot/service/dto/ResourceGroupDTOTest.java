package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceGroupDTO.class);
        ResourceGroupDTO resourceGroupDTO1 = new ResourceGroupDTO();
        resourceGroupDTO1.setId(1L);
        ResourceGroupDTO resourceGroupDTO2 = new ResourceGroupDTO();
        assertThat(resourceGroupDTO1).isNotEqualTo(resourceGroupDTO2);
        resourceGroupDTO2.setId(resourceGroupDTO1.getId());
        assertThat(resourceGroupDTO1).isEqualTo(resourceGroupDTO2);
        resourceGroupDTO2.setId(2L);
        assertThat(resourceGroupDTO1).isNotEqualTo(resourceGroupDTO2);
        resourceGroupDTO1.setId(null);
        assertThat(resourceGroupDTO1).isNotEqualTo(resourceGroupDTO2);
    }
}
