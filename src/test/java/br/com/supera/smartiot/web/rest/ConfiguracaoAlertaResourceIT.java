package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ConfiguracaoAlertaAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.smartiot.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.ConfiguracaoAlerta;
import br.com.supera.smartiot.repository.ConfiguracaoAlertaRepository;
import br.com.supera.smartiot.service.dto.ConfiguracaoAlertaDTO;
import br.com.supera.smartiot.service.mapper.ConfiguracaoAlertaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConfiguracaoAlertaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfiguracaoAlertaResourceIT {

    private static final BigDecimal DEFAULT_LIMITE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIMITE = new BigDecimal(2);

    private static final String DEFAULT_EMAIL = ":-]0#@Jj:\\=oNze9z";
    private static final String UPDATED_EMAIL = "EO@f4 5f.\\{8Z,";

    private static final String ENTITY_API_URL = "/api/configuracao-alertas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConfiguracaoAlertaRepository configuracaoAlertaRepository;

    @Autowired
    private ConfiguracaoAlertaMapper configuracaoAlertaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfiguracaoAlertaMockMvc;

    private ConfiguracaoAlerta configuracaoAlerta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfiguracaoAlerta createEntity(EntityManager em) {
        ConfiguracaoAlerta configuracaoAlerta = new ConfiguracaoAlerta().limite(DEFAULT_LIMITE).email(DEFAULT_EMAIL);
        return configuracaoAlerta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfiguracaoAlerta createUpdatedEntity(EntityManager em) {
        ConfiguracaoAlerta configuracaoAlerta = new ConfiguracaoAlerta().limite(UPDATED_LIMITE).email(UPDATED_EMAIL);
        return configuracaoAlerta;
    }

    @BeforeEach
    public void initTest() {
        configuracaoAlerta = createEntity(em);
    }

    @Test
    @Transactional
    void createConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);
        var returnedConfiguracaoAlertaDTO = om.readValue(
            restConfiguracaoAlertaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoAlertaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConfiguracaoAlertaDTO.class
        );

        // Validate the ConfiguracaoAlerta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConfiguracaoAlerta = configuracaoAlertaMapper.toEntity(returnedConfiguracaoAlertaDTO);
        assertConfiguracaoAlertaUpdatableFieldsEquals(
            returnedConfiguracaoAlerta,
            getPersistedConfiguracaoAlerta(returnedConfiguracaoAlerta)
        );
    }

    @Test
    @Transactional
    void createConfiguracaoAlertaWithExistingId() throws Exception {
        // Create the ConfiguracaoAlerta with an existing ID
        configuracaoAlerta.setId(1L);
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfiguracaoAlertaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoAlertaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configuracaoAlerta.setEmail(null);

        // Create the ConfiguracaoAlerta, which fails.
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        restConfiguracaoAlertaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoAlertaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConfiguracaoAlertas() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.saveAndFlush(configuracaoAlerta);

        // Get all the configuracaoAlertaList
        restConfiguracaoAlertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configuracaoAlerta.getId().intValue())))
            .andExpect(jsonPath("$.[*].limite").value(hasItem(sameNumber(DEFAULT_LIMITE))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getConfiguracaoAlerta() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.saveAndFlush(configuracaoAlerta);

        // Get the configuracaoAlerta
        restConfiguracaoAlertaMockMvc
            .perform(get(ENTITY_API_URL_ID, configuracaoAlerta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configuracaoAlerta.getId().intValue()))
            .andExpect(jsonPath("$.limite").value(sameNumber(DEFAULT_LIMITE)))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingConfiguracaoAlerta() throws Exception {
        // Get the configuracaoAlerta
        restConfiguracaoAlertaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConfiguracaoAlerta() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.saveAndFlush(configuracaoAlerta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoAlerta
        ConfiguracaoAlerta updatedConfiguracaoAlerta = configuracaoAlertaRepository.findById(configuracaoAlerta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConfiguracaoAlerta are not directly saved in db
        em.detach(updatedConfiguracaoAlerta);
        updatedConfiguracaoAlerta.limite(UPDATED_LIMITE).email(UPDATED_EMAIL);
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(updatedConfiguracaoAlerta);

        restConfiguracaoAlertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configuracaoAlertaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configuracaoAlertaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConfiguracaoAlertaToMatchAllProperties(updatedConfiguracaoAlerta);
    }

    @Test
    @Transactional
    void putNonExistingConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfiguracaoAlertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configuracaoAlertaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configuracaoAlertaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracaoAlertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configuracaoAlertaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracaoAlertaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoAlertaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfiguracaoAlertaWithPatch() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.saveAndFlush(configuracaoAlerta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoAlerta using partial update
        ConfiguracaoAlerta partialUpdatedConfiguracaoAlerta = new ConfiguracaoAlerta();
        partialUpdatedConfiguracaoAlerta.setId(configuracaoAlerta.getId());

        partialUpdatedConfiguracaoAlerta.limite(UPDATED_LIMITE).email(UPDATED_EMAIL);

        restConfiguracaoAlertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfiguracaoAlerta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfiguracaoAlerta))
            )
            .andExpect(status().isOk());

        // Validate the ConfiguracaoAlerta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfiguracaoAlertaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConfiguracaoAlerta, configuracaoAlerta),
            getPersistedConfiguracaoAlerta(configuracaoAlerta)
        );
    }

    @Test
    @Transactional
    void fullUpdateConfiguracaoAlertaWithPatch() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.saveAndFlush(configuracaoAlerta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoAlerta using partial update
        ConfiguracaoAlerta partialUpdatedConfiguracaoAlerta = new ConfiguracaoAlerta();
        partialUpdatedConfiguracaoAlerta.setId(configuracaoAlerta.getId());

        partialUpdatedConfiguracaoAlerta.limite(UPDATED_LIMITE).email(UPDATED_EMAIL);

        restConfiguracaoAlertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfiguracaoAlerta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfiguracaoAlerta))
            )
            .andExpect(status().isOk());

        // Validate the ConfiguracaoAlerta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfiguracaoAlertaUpdatableFieldsEquals(
            partialUpdatedConfiguracaoAlerta,
            getPersistedConfiguracaoAlerta(partialUpdatedConfiguracaoAlerta)
        );
    }

    @Test
    @Transactional
    void patchNonExistingConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfiguracaoAlertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configuracaoAlertaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configuracaoAlertaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracaoAlertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configuracaoAlertaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracaoAlertaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(configuracaoAlertaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfiguracaoAlerta() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.saveAndFlush(configuracaoAlerta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the configuracaoAlerta
        restConfiguracaoAlertaMockMvc
            .perform(delete(ENTITY_API_URL_ID, configuracaoAlerta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return configuracaoAlertaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ConfiguracaoAlerta getPersistedConfiguracaoAlerta(ConfiguracaoAlerta configuracaoAlerta) {
        return configuracaoAlertaRepository.findById(configuracaoAlerta.getId()).orElseThrow();
    }

    protected void assertPersistedConfiguracaoAlertaToMatchAllProperties(ConfiguracaoAlerta expectedConfiguracaoAlerta) {
        assertConfiguracaoAlertaAllPropertiesEquals(expectedConfiguracaoAlerta, getPersistedConfiguracaoAlerta(expectedConfiguracaoAlerta));
    }

    protected void assertPersistedConfiguracaoAlertaToMatchUpdatableProperties(ConfiguracaoAlerta expectedConfiguracaoAlerta) {
        assertConfiguracaoAlertaAllUpdatablePropertiesEquals(
            expectedConfiguracaoAlerta,
            getPersistedConfiguracaoAlerta(expectedConfiguracaoAlerta)
        );
    }
}
