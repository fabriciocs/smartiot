package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.RepeaterAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Repeater;
import br.com.supera.smartiot.repository.RepeaterRepository;
import br.com.supera.smartiot.service.dto.RepeaterDTO;
import br.com.supera.smartiot.service.mapper.RepeaterMapper;
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
 * Integration tests for the {@link RepeaterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RepeaterResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_RANGE = 1;
    private static final Integer UPDATED_RANGE = 2;

    private static final String ENTITY_API_URL = "/api/repeaters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RepeaterRepository repeaterRepository;

    @Autowired
    private RepeaterMapper repeaterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepeaterMockMvc;

    private Repeater repeater;

    private Repeater insertedRepeater;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repeater createEntity(EntityManager em) {
        Repeater repeater = new Repeater().serialNumber(DEFAULT_SERIAL_NUMBER).range(DEFAULT_RANGE);
        return repeater;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repeater createUpdatedEntity(EntityManager em) {
        Repeater repeater = new Repeater().serialNumber(UPDATED_SERIAL_NUMBER).range(UPDATED_RANGE);
        return repeater;
    }

    @BeforeEach
    public void initTest() {
        repeater = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRepeater != null) {
            repeaterRepository.delete(insertedRepeater);
            insertedRepeater = null;
        }
    }

    @Test
    @Transactional
    void createRepeater() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Repeater
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);
        var returnedRepeaterDTO = om.readValue(
            restRepeaterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repeaterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RepeaterDTO.class
        );

        // Validate the Repeater in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRepeater = repeaterMapper.toEntity(returnedRepeaterDTO);
        assertRepeaterUpdatableFieldsEquals(returnedRepeater, getPersistedRepeater(returnedRepeater));

        insertedRepeater = returnedRepeater;
    }

    @Test
    @Transactional
    void createRepeaterWithExistingId() throws Exception {
        // Create the Repeater with an existing ID
        repeater.setId(1L);
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepeaterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repeaterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Repeater in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        repeater.setSerialNumber(null);

        // Create the Repeater, which fails.
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        restRepeaterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repeaterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRangeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        repeater.setRange(null);

        // Create the Repeater, which fails.
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        restRepeaterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repeaterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRepeaters() throws Exception {
        // Initialize the database
        insertedRepeater = repeaterRepository.saveAndFlush(repeater);

        // Get all the repeaterList
        restRepeaterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repeater.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].range").value(hasItem(DEFAULT_RANGE)));
    }

    @Test
    @Transactional
    void getRepeater() throws Exception {
        // Initialize the database
        insertedRepeater = repeaterRepository.saveAndFlush(repeater);

        // Get the repeater
        restRepeaterMockMvc
            .perform(get(ENTITY_API_URL_ID, repeater.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repeater.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.range").value(DEFAULT_RANGE));
    }

    @Test
    @Transactional
    void getNonExistingRepeater() throws Exception {
        // Get the repeater
        restRepeaterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRepeater() throws Exception {
        // Initialize the database
        insertedRepeater = repeaterRepository.saveAndFlush(repeater);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repeater
        Repeater updatedRepeater = repeaterRepository.findById(repeater.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRepeater are not directly saved in db
        em.detach(updatedRepeater);
        updatedRepeater.serialNumber(UPDATED_SERIAL_NUMBER).range(UPDATED_RANGE);
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(updatedRepeater);

        restRepeaterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repeaterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(repeaterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Repeater in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRepeaterToMatchAllProperties(updatedRepeater);
    }

    @Test
    @Transactional
    void putNonExistingRepeater() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repeater.setId(longCount.incrementAndGet());

        // Create the Repeater
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepeaterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repeaterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(repeaterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repeater in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepeater() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repeater.setId(longCount.incrementAndGet());

        // Create the Repeater
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepeaterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(repeaterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repeater in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepeater() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repeater.setId(longCount.incrementAndGet());

        // Create the Repeater
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepeaterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repeaterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repeater in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepeaterWithPatch() throws Exception {
        // Initialize the database
        insertedRepeater = repeaterRepository.saveAndFlush(repeater);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repeater using partial update
        Repeater partialUpdatedRepeater = new Repeater();
        partialUpdatedRepeater.setId(repeater.getId());

        restRepeaterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepeater.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRepeater))
            )
            .andExpect(status().isOk());

        // Validate the Repeater in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRepeaterUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRepeater, repeater), getPersistedRepeater(repeater));
    }

    @Test
    @Transactional
    void fullUpdateRepeaterWithPatch() throws Exception {
        // Initialize the database
        insertedRepeater = repeaterRepository.saveAndFlush(repeater);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repeater using partial update
        Repeater partialUpdatedRepeater = new Repeater();
        partialUpdatedRepeater.setId(repeater.getId());

        partialUpdatedRepeater.serialNumber(UPDATED_SERIAL_NUMBER).range(UPDATED_RANGE);

        restRepeaterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepeater.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRepeater))
            )
            .andExpect(status().isOk());

        // Validate the Repeater in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRepeaterUpdatableFieldsEquals(partialUpdatedRepeater, getPersistedRepeater(partialUpdatedRepeater));
    }

    @Test
    @Transactional
    void patchNonExistingRepeater() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repeater.setId(longCount.incrementAndGet());

        // Create the Repeater
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepeaterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repeaterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(repeaterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repeater in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepeater() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repeater.setId(longCount.incrementAndGet());

        // Create the Repeater
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepeaterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(repeaterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repeater in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepeater() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repeater.setId(longCount.incrementAndGet());

        // Create the Repeater
        RepeaterDTO repeaterDTO = repeaterMapper.toDto(repeater);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepeaterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(repeaterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repeater in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepeater() throws Exception {
        // Initialize the database
        insertedRepeater = repeaterRepository.saveAndFlush(repeater);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the repeater
        restRepeaterMockMvc
            .perform(delete(ENTITY_API_URL_ID, repeater.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return repeaterRepository.count();
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

    protected Repeater getPersistedRepeater(Repeater repeater) {
        return repeaterRepository.findById(repeater.getId()).orElseThrow();
    }

    protected void assertPersistedRepeaterToMatchAllProperties(Repeater expectedRepeater) {
        assertRepeaterAllPropertiesEquals(expectedRepeater, getPersistedRepeater(expectedRepeater));
    }

    protected void assertPersistedRepeaterToMatchUpdatableProperties(Repeater expectedRepeater) {
        assertRepeaterAllUpdatablePropertiesEquals(expectedRepeater, getPersistedRepeater(expectedRepeater));
    }
}
