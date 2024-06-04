package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ManualEntryAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.smartiot.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.ManualEntry;
import br.com.supera.smartiot.repository.ManualEntryRepository;
import br.com.supera.smartiot.service.dto.ManualEntryDTO;
import br.com.supera.smartiot.service.mapper.ManualEntryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link ManualEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManualEntryResourceIT {

    private static final String DEFAULT_ENTRY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTRY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ENTRY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENTRY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/manual-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ManualEntryRepository manualEntryRepository;

    @Autowired
    private ManualEntryMapper manualEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManualEntryMockMvc;

    private ManualEntry manualEntry;

    private ManualEntry insertedManualEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualEntry createEntity(EntityManager em) {
        ManualEntry manualEntry = new ManualEntry().entryType(DEFAULT_ENTRY_TYPE).value(DEFAULT_VALUE).entryDate(DEFAULT_ENTRY_DATE);
        return manualEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualEntry createUpdatedEntity(EntityManager em) {
        ManualEntry manualEntry = new ManualEntry().entryType(UPDATED_ENTRY_TYPE).value(UPDATED_VALUE).entryDate(UPDATED_ENTRY_DATE);
        return manualEntry;
    }

    @BeforeEach
    public void initTest() {
        manualEntry = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedManualEntry != null) {
            manualEntryRepository.delete(insertedManualEntry);
            insertedManualEntry = null;
        }
    }

    @Test
    @Transactional
    void createManualEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ManualEntry
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);
        var returnedManualEntryDTO = om.readValue(
            restManualEntryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualEntryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ManualEntryDTO.class
        );

        // Validate the ManualEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedManualEntry = manualEntryMapper.toEntity(returnedManualEntryDTO);
        assertManualEntryUpdatableFieldsEquals(returnedManualEntry, getPersistedManualEntry(returnedManualEntry));

        insertedManualEntry = returnedManualEntry;
    }

    @Test
    @Transactional
    void createManualEntryWithExistingId() throws Exception {
        // Create the ManualEntry with an existing ID
        manualEntry.setId(1L);
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManualEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ManualEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntryTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        manualEntry.setEntryType(null);

        // Create the ManualEntry, which fails.
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        restManualEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        manualEntry.setValue(null);

        // Create the ManualEntry, which fails.
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        restManualEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntryDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        manualEntry.setEntryDate(null);

        // Create the ManualEntry, which fails.
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        restManualEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllManualEntries() throws Exception {
        // Initialize the database
        insertedManualEntry = manualEntryRepository.saveAndFlush(manualEntry);

        // Get all the manualEntryList
        restManualEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manualEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryType").value(hasItem(DEFAULT_ENTRY_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(sameInstant(DEFAULT_ENTRY_DATE))));
    }

    @Test
    @Transactional
    void getManualEntry() throws Exception {
        // Initialize the database
        insertedManualEntry = manualEntryRepository.saveAndFlush(manualEntry);

        // Get the manualEntry
        restManualEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, manualEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manualEntry.getId().intValue()))
            .andExpect(jsonPath("$.entryType").value(DEFAULT_ENTRY_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.entryDate").value(sameInstant(DEFAULT_ENTRY_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingManualEntry() throws Exception {
        // Get the manualEntry
        restManualEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingManualEntry() throws Exception {
        // Initialize the database
        insertedManualEntry = manualEntryRepository.saveAndFlush(manualEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manualEntry
        ManualEntry updatedManualEntry = manualEntryRepository.findById(manualEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedManualEntry are not directly saved in db
        em.detach(updatedManualEntry);
        updatedManualEntry.entryType(UPDATED_ENTRY_TYPE).value(UPDATED_VALUE).entryDate(UPDATED_ENTRY_DATE);
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(updatedManualEntry);

        restManualEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manualEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ManualEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedManualEntryToMatchAllProperties(updatedManualEntry);
    }

    @Test
    @Transactional
    void putNonExistingManualEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualEntry.setId(longCount.incrementAndGet());

        // Create the ManualEntry
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manualEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManualEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualEntry.setId(longCount.incrementAndGet());

        // Create the ManualEntry
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manualEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManualEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualEntry.setId(longCount.incrementAndGet());

        // Create the ManualEntry
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManualEntryWithPatch() throws Exception {
        // Initialize the database
        insertedManualEntry = manualEntryRepository.saveAndFlush(manualEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manualEntry using partial update
        ManualEntry partialUpdatedManualEntry = new ManualEntry();
        partialUpdatedManualEntry.setId(manualEntry.getId());

        partialUpdatedManualEntry.entryType(UPDATED_ENTRY_TYPE).value(UPDATED_VALUE).entryDate(UPDATED_ENTRY_DATE);

        restManualEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManualEntry))
            )
            .andExpect(status().isOk());

        // Validate the ManualEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManualEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedManualEntry, manualEntry),
            getPersistedManualEntry(manualEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateManualEntryWithPatch() throws Exception {
        // Initialize the database
        insertedManualEntry = manualEntryRepository.saveAndFlush(manualEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manualEntry using partial update
        ManualEntry partialUpdatedManualEntry = new ManualEntry();
        partialUpdatedManualEntry.setId(manualEntry.getId());

        partialUpdatedManualEntry.entryType(UPDATED_ENTRY_TYPE).value(UPDATED_VALUE).entryDate(UPDATED_ENTRY_DATE);

        restManualEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManualEntry))
            )
            .andExpect(status().isOk());

        // Validate the ManualEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManualEntryUpdatableFieldsEquals(partialUpdatedManualEntry, getPersistedManualEntry(partialUpdatedManualEntry));
    }

    @Test
    @Transactional
    void patchNonExistingManualEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualEntry.setId(longCount.incrementAndGet());

        // Create the ManualEntry
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manualEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(manualEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManualEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualEntry.setId(longCount.incrementAndGet());

        // Create the ManualEntry
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(manualEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManualEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualEntry.setId(longCount.incrementAndGet());

        // Create the ManualEntry
        ManualEntryDTO manualEntryDTO = manualEntryMapper.toDto(manualEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualEntryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(manualEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManualEntry() throws Exception {
        // Initialize the database
        insertedManualEntry = manualEntryRepository.saveAndFlush(manualEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the manualEntry
        restManualEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, manualEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return manualEntryRepository.count();
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

    protected ManualEntry getPersistedManualEntry(ManualEntry manualEntry) {
        return manualEntryRepository.findById(manualEntry.getId()).orElseThrow();
    }

    protected void assertPersistedManualEntryToMatchAllProperties(ManualEntry expectedManualEntry) {
        assertManualEntryAllPropertiesEquals(expectedManualEntry, getPersistedManualEntry(expectedManualEntry));
    }

    protected void assertPersistedManualEntryToMatchUpdatableProperties(ManualEntry expectedManualEntry) {
        assertManualEntryAllUpdatablePropertiesEquals(expectedManualEntry, getPersistedManualEntry(expectedManualEntry));
    }
}
