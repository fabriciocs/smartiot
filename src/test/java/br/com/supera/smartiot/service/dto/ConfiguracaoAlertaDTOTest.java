package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfiguracaoAlertaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfiguracaoAlertaDTO.class);
        ConfiguracaoAlertaDTO configuracaoAlertaDTO1 = new ConfiguracaoAlertaDTO();
        configuracaoAlertaDTO1.setId(1L);
        ConfiguracaoAlertaDTO configuracaoAlertaDTO2 = new ConfiguracaoAlertaDTO();
        assertThat(configuracaoAlertaDTO1).isNotEqualTo(configuracaoAlertaDTO2);
        configuracaoAlertaDTO2.setId(configuracaoAlertaDTO1.getId());
        assertThat(configuracaoAlertaDTO1).isEqualTo(configuracaoAlertaDTO2);
        configuracaoAlertaDTO2.setId(2L);
        assertThat(configuracaoAlertaDTO1).isNotEqualTo(configuracaoAlertaDTO2);
        configuracaoAlertaDTO1.setId(null);
        assertThat(configuracaoAlertaDTO1).isNotEqualTo(configuracaoAlertaDTO2);
    }
}
