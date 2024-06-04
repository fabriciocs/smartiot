package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.DadoSensorAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.DadoSensor;
import br.com.supera.smartiot.repository.DadoSensorRepository;
import br.com.supera.smartiot.service.dto.DadoSensorDTO;
import br.com.supera.smartiot.service.mapper.DadoSensorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DadoSensorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DadoSensorResourceIT {

    private static final String DEFAULT_DADOS = "AAAAAAAAAA";
    private static final String UPDATED_DADOS = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/dado-sensors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DadoSensorRepository dadoSensorRepository;

    @Autowired
    private DadoSensorMapper dadoSensorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDadoSensorMockMvc;

    private DadoSensor dadoSensor;

    private DadoSensor insertedDadoSensor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DadoSensor createEntity(EntityManager em) {
        DadoSensor dadoSensor = new DadoSensor().dados(DEFAULT_DADOS).timestamp(DEFAULT_TIMESTAMP);
        return dadoSensor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DadoSensor createUpdatedEntity(EntityManager em) {
        DadoSensor dadoSensor = new DadoSensor().dados(UPDATED_DADOS).timestamp(UPDATED_TIMESTAMP);
        return dadoSensor;
    }

    @BeforeEach
    public void initTest() {
        dadoSensor = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDadoSensor != null) {
            dadoSensorRepository.delete(insertedDadoSensor);
            insertedDadoSensor = null;
        }
    }

    @Test
    @Transactional
    void createDadoSensor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);
        var returnedDadoSensorDTO = om.readValue(
            restDadoSensorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dadoSensorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DadoSensorDTO.class
        );

        // Validate the DadoSensor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDadoSensor = dadoSensorMapper.toEntity(returnedDadoSensorDTO);
        assertDadoSensorUpdatableFieldsEquals(returnedDadoSensor, getPersistedDadoSensor(returnedDadoSensor));

        insertedDadoSensor = returnedDadoSensor;
    }

    @Test
    @Transactional
    void createDadoSensorWithExistingId() throws Exception {
        // Create the DadoSensor with an existing ID
        dadoSensor.setId(1L);
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDadoSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dadoSensorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDadoSensors() throws Exception {
        // Initialize the database
        insertedDadoSensor = dadoSensorRepository.saveAndFlush(dadoSensor);

        // Get all the dadoSensorList
        restDadoSensorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dadoSensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].dados").value(hasItem(DEFAULT_DADOS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getDadoSensor() throws Exception {
        // Initialize the database
        insertedDadoSensor = dadoSensorRepository.saveAndFlush(dadoSensor);

        // Get the dadoSensor
        restDadoSensorMockMvc
            .perform(get(ENTITY_API_URL_ID, dadoSensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dadoSensor.getId().intValue()))
            .andExpect(jsonPath("$.dados").value(DEFAULT_DADOS))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDadoSensor() throws Exception {
        // Get the dadoSensor
        restDadoSensorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDadoSensor() throws Exception {
        // Initialize the database
        insertedDadoSensor = dadoSensorRepository.saveAndFlush(dadoSensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dadoSensor
        DadoSensor updatedDadoSensor = dadoSensorRepository.findById(dadoSensor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDadoSensor are not directly saved in db
        em.detach(updatedDadoSensor);
        updatedDadoSensor.dados(UPDATED_DADOS).timestamp(UPDATED_TIMESTAMP);
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(updatedDadoSensor);

        restDadoSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dadoSensorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dadoSensorDTO))
            )
            .andExpect(status().isOk());

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDadoSensorToMatchAllProperties(updatedDadoSensor);
    }

    @Test
    @Transactional
    void putNonExistingDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDadoSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dadoSensorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dadoSensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDadoSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dadoSensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDadoSensorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dadoSensorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDadoSensorWithPatch() throws Exception {
        // Initialize the database
        insertedDadoSensor = dadoSensorRepository.saveAndFlush(dadoSensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dadoSensor using partial update
        DadoSensor partialUpdatedDadoSensor = new DadoSensor();
        partialUpdatedDadoSensor.setId(dadoSensor.getId());

        restDadoSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDadoSensor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDadoSensor))
            )
            .andExpect(status().isOk());

        // Validate the DadoSensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDadoSensorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDadoSensor, dadoSensor),
            getPersistedDadoSensor(dadoSensor)
        );
    }

    @Test
    @Transactional
    void fullUpdateDadoSensorWithPatch() throws Exception {
        // Initialize the database
        insertedDadoSensor = dadoSensorRepository.saveAndFlush(dadoSensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dadoSensor using partial update
        DadoSensor partialUpdatedDadoSensor = new DadoSensor();
        partialUpdatedDadoSensor.setId(dadoSensor.getId());

        partialUpdatedDadoSensor.dados(UPDATED_DADOS).timestamp(UPDATED_TIMESTAMP);

        restDadoSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDadoSensor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDadoSensor))
            )
            .andExpect(status().isOk());

        // Validate the DadoSensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDadoSensorUpdatableFieldsEquals(partialUpdatedDadoSensor, getPersistedDadoSensor(partialUpdatedDadoSensor));
    }

    @Test
    @Transactional
    void patchNonExistingDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDadoSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dadoSensorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dadoSensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDadoSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dadoSensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDadoSensorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dadoSensorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDadoSensor() throws Exception {
        // Initialize the database
        insertedDadoSensor = dadoSensorRepository.saveAndFlush(dadoSensor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dadoSensor
        restDadoSensorMockMvc
            .perform(delete(ENTITY_API_URL_ID, dadoSensor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dadoSensorRepository.count();
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

    protected DadoSensor getPersistedDadoSensor(DadoSensor dadoSensor) {
        return dadoSensorRepository.findById(dadoSensor.getId()).orElseThrow();
    }

    protected void assertPersistedDadoSensorToMatchAllProperties(DadoSensor expectedDadoSensor) {
        assertDadoSensorAllPropertiesEquals(expectedDadoSensor, getPersistedDadoSensor(expectedDadoSensor));
    }

    protected void assertPersistedDadoSensorToMatchUpdatableProperties(DadoSensor expectedDadoSensor) {
        assertDadoSensorAllUpdatablePropertiesEquals(expectedDadoSensor, getPersistedDadoSensor(expectedDadoSensor));
    }
}
