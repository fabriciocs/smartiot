package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ConcentratorAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Concentrator;
import br.com.supera.smartiot.repository.ConcentratorRepository;
import br.com.supera.smartiot.service.dto.ConcentratorDTO;
import br.com.supera.smartiot.service.mapper.ConcentratorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ConcentratorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConcentratorResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final String ENTITY_API_URL = "/api/concentrators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConcentratorRepository concentratorRepository;

    @Autowired
    private ConcentratorMapper concentratorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConcentratorMockMvc;

    private Concentrator concentrator;

    private Concentrator insertedConcentrator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Concentrator createEntity(EntityManager em) {
        Concentrator concentrator = new Concentrator().serialNumber(DEFAULT_SERIAL_NUMBER).capacity(DEFAULT_CAPACITY);
        return concentrator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Concentrator createUpdatedEntity(EntityManager em) {
        Concentrator concentrator = new Concentrator().serialNumber(UPDATED_SERIAL_NUMBER).capacity(UPDATED_CAPACITY);
        return concentrator;
    }

    @BeforeEach
    public void initTest() {
        concentrator = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedConcentrator != null) {
            concentratorRepository.delete(insertedConcentrator);
            insertedConcentrator = null;
        }
    }

    @Test
    @Transactional
    void createConcentrator() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Concentrator
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);
        var returnedConcentratorDTO = om.readValue(
            restConcentratorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concentratorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConcentratorDTO.class
        );

        // Validate the Concentrator in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConcentrator = concentratorMapper.toEntity(returnedConcentratorDTO);
        assertConcentratorUpdatableFieldsEquals(returnedConcentrator, getPersistedConcentrator(returnedConcentrator));

        insertedConcentrator = returnedConcentrator;
    }

    @Test
    @Transactional
    void createConcentratorWithExistingId() throws Exception {
        // Create the Concentrator with an existing ID
        concentrator.setId(1L);
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConcentratorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concentratorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Concentrator in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        concentrator.setSerialNumber(null);

        // Create the Concentrator, which fails.
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        restConcentratorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concentratorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        concentrator.setCapacity(null);

        // Create the Concentrator, which fails.
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        restConcentratorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concentratorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConcentrators() throws Exception {
        // Initialize the database
        insertedConcentrator = concentratorRepository.saveAndFlush(concentrator);

        // Get all the concentratorList
        restConcentratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(concentrator.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }

    @Test
    @Transactional
    void getConcentrator() throws Exception {
        // Initialize the database
        insertedConcentrator = concentratorRepository.saveAndFlush(concentrator);

        // Get the concentrator
        restConcentratorMockMvc
            .perform(get(ENTITY_API_URL_ID, concentrator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(concentrator.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY));
    }

    @Test
    @Transactional
    void getNonExistingConcentrator() throws Exception {
        // Get the concentrator
        restConcentratorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConcentrator() throws Exception {
        // Initialize the database
        insertedConcentrator = concentratorRepository.saveAndFlush(concentrator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the concentrator
        Concentrator updatedConcentrator = concentratorRepository.findById(concentrator.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConcentrator are not directly saved in db
        em.detach(updatedConcentrator);
        updatedConcentrator.serialNumber(UPDATED_SERIAL_NUMBER).capacity(UPDATED_CAPACITY);
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(updatedConcentrator);

        restConcentratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, concentratorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(concentratorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Concentrator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConcentratorToMatchAllProperties(updatedConcentrator);
    }

    @Test
    @Transactional
    void putNonExistingConcentrator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concentrator.setId(longCount.incrementAndGet());

        // Create the Concentrator
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConcentratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, concentratorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(concentratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concentrator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConcentrator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concentrator.setId(longCount.incrementAndGet());

        // Create the Concentrator
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcentratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(concentratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concentrator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConcentrator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concentrator.setId(longCount.incrementAndGet());

        // Create the Concentrator
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcentratorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concentratorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Concentrator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConcentratorWithPatch() throws Exception {
        // Initialize the database
        insertedConcentrator = concentratorRepository.saveAndFlush(concentrator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the concentrator using partial update
        Concentrator partialUpdatedConcentrator = new Concentrator();
        partialUpdatedConcentrator.setId(concentrator.getId());

        partialUpdatedConcentrator.serialNumber(UPDATED_SERIAL_NUMBER).capacity(UPDATED_CAPACITY);

        restConcentratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcentrator.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConcentrator))
            )
            .andExpect(status().isOk());

        // Validate the Concentrator in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConcentratorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConcentrator, concentrator),
            getPersistedConcentrator(concentrator)
        );
    }

    @Test
    @Transactional
    void fullUpdateConcentratorWithPatch() throws Exception {
        // Initialize the database
        insertedConcentrator = concentratorRepository.saveAndFlush(concentrator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the concentrator using partial update
        Concentrator partialUpdatedConcentrator = new Concentrator();
        partialUpdatedConcentrator.setId(concentrator.getId());

        partialUpdatedConcentrator.serialNumber(UPDATED_SERIAL_NUMBER).capacity(UPDATED_CAPACITY);

        restConcentratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcentrator.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConcentrator))
            )
            .andExpect(status().isOk());

        // Validate the Concentrator in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConcentratorUpdatableFieldsEquals(partialUpdatedConcentrator, getPersistedConcentrator(partialUpdatedConcentrator));
    }

    @Test
    @Transactional
    void patchNonExistingConcentrator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concentrator.setId(longCount.incrementAndGet());

        // Create the Concentrator
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConcentratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, concentratorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(concentratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concentrator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConcentrator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concentrator.setId(longCount.incrementAndGet());

        // Create the Concentrator
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcentratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(concentratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concentrator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConcentrator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concentrator.setId(longCount.incrementAndGet());

        // Create the Concentrator
        ConcentratorDTO concentratorDTO = concentratorMapper.toDto(concentrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcentratorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(concentratorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Concentrator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConcentrator() throws Exception {
        // Initialize the database
        insertedConcentrator = concentratorRepository.saveAndFlush(concentrator);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the concentrator
        restConcentratorMockMvc
            .perform(delete(ENTITY_API_URL_ID, concentrator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return concentratorRepository.count();
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

    protected Concentrator getPersistedConcentrator(Concentrator concentrator) {
        return concentratorRepository.findById(concentrator.getId()).orElseThrow();
    }

    protected void assertPersistedConcentratorToMatchAllProperties(Concentrator expectedConcentrator) {
        assertConcentratorAllPropertiesEquals(expectedConcentrator, getPersistedConcentrator(expectedConcentrator));
    }

    protected void assertPersistedConcentratorToMatchUpdatableProperties(Concentrator expectedConcentrator) {
        assertConcentratorAllUpdatablePropertiesEquals(expectedConcentrator, getPersistedConcentrator(expectedConcentrator));
    }
}
