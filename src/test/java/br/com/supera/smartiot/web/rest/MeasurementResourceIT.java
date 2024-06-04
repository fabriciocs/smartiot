package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.MeasurementAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.smartiot.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Measurement;
import br.com.supera.smartiot.repository.MeasurementRepository;
import br.com.supera.smartiot.service.dto.MeasurementDTO;
import br.com.supera.smartiot.service.mapper.MeasurementMapper;
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
 * Integration tests for the {@link MeasurementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeasurementResourceIT {

    private static final String DEFAULT_MEASUREMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MEASUREMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_MEASUREMENT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MEASUREMENT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/measurements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MeasurementMapper measurementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeasurementMockMvc;

    private Measurement measurement;

    private Measurement insertedMeasurement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measurement createEntity(EntityManager em) {
        Measurement measurement = new Measurement()
            .measurementType(DEFAULT_MEASUREMENT_TYPE)
            .value(DEFAULT_VALUE)
            .measurementTime(DEFAULT_MEASUREMENT_TIME);
        return measurement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measurement createUpdatedEntity(EntityManager em) {
        Measurement measurement = new Measurement()
            .measurementType(UPDATED_MEASUREMENT_TYPE)
            .value(UPDATED_VALUE)
            .measurementTime(UPDATED_MEASUREMENT_TIME);
        return measurement;
    }

    @BeforeEach
    public void initTest() {
        measurement = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMeasurement != null) {
            measurementRepository.delete(insertedMeasurement);
            insertedMeasurement = null;
        }
    }

    @Test
    @Transactional
    void createMeasurement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);
        var returnedMeasurementDTO = om.readValue(
            restMeasurementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(measurementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MeasurementDTO.class
        );

        // Validate the Measurement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMeasurement = measurementMapper.toEntity(returnedMeasurementDTO);
        assertMeasurementUpdatableFieldsEquals(returnedMeasurement, getPersistedMeasurement(returnedMeasurement));

        insertedMeasurement = returnedMeasurement;
    }

    @Test
    @Transactional
    void createMeasurementWithExistingId() throws Exception {
        // Create the Measurement with an existing ID
        measurement.setId(1L);
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasurementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(measurementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMeasurementTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        measurement.setMeasurementType(null);

        // Create the Measurement, which fails.
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        restMeasurementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(measurementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        measurement.setValue(null);

        // Create the Measurement, which fails.
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        restMeasurementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(measurementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMeasurementTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        measurement.setMeasurementTime(null);

        // Create the Measurement, which fails.
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        restMeasurementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(measurementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeasurements() throws Exception {
        // Initialize the database
        insertedMeasurement = measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList
        restMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].measurementType").value(hasItem(DEFAULT_MEASUREMENT_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].measurementTime").value(hasItem(sameInstant(DEFAULT_MEASUREMENT_TIME))));
    }

    @Test
    @Transactional
    void getMeasurement() throws Exception {
        // Initialize the database
        insertedMeasurement = measurementRepository.saveAndFlush(measurement);

        // Get the measurement
        restMeasurementMockMvc
            .perform(get(ENTITY_API_URL_ID, measurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(measurement.getId().intValue()))
            .andExpect(jsonPath("$.measurementType").value(DEFAULT_MEASUREMENT_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.measurementTime").value(sameInstant(DEFAULT_MEASUREMENT_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingMeasurement() throws Exception {
        // Get the measurement
        restMeasurementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeasurement() throws Exception {
        // Initialize the database
        insertedMeasurement = measurementRepository.saveAndFlush(measurement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the measurement
        Measurement updatedMeasurement = measurementRepository.findById(measurement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMeasurement are not directly saved in db
        em.detach(updatedMeasurement);
        updatedMeasurement.measurementType(UPDATED_MEASUREMENT_TYPE).value(UPDATED_VALUE).measurementTime(UPDATED_MEASUREMENT_TIME);
        MeasurementDTO measurementDTO = measurementMapper.toDto(updatedMeasurement);

        restMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, measurementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(measurementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Measurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMeasurementToMatchAllProperties(updatedMeasurement);
    }

    @Test
    @Transactional
    void putNonExistingMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        measurement.setId(longCount.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, measurementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        measurement.setId(longCount.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        measurement.setId(longCount.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(measurementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Measurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeasurementWithPatch() throws Exception {
        // Initialize the database
        insertedMeasurement = measurementRepository.saveAndFlush(measurement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the measurement using partial update
        Measurement partialUpdatedMeasurement = new Measurement();
        partialUpdatedMeasurement.setId(measurement.getId());

        partialUpdatedMeasurement.value(UPDATED_VALUE);

        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasurement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the Measurement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMeasurementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMeasurement, measurement),
            getPersistedMeasurement(measurement)
        );
    }

    @Test
    @Transactional
    void fullUpdateMeasurementWithPatch() throws Exception {
        // Initialize the database
        insertedMeasurement = measurementRepository.saveAndFlush(measurement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the measurement using partial update
        Measurement partialUpdatedMeasurement = new Measurement();
        partialUpdatedMeasurement.setId(measurement.getId());

        partialUpdatedMeasurement.measurementType(UPDATED_MEASUREMENT_TYPE).value(UPDATED_VALUE).measurementTime(UPDATED_MEASUREMENT_TIME);

        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasurement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the Measurement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMeasurementUpdatableFieldsEquals(partialUpdatedMeasurement, getPersistedMeasurement(partialUpdatedMeasurement));
    }

    @Test
    @Transactional
    void patchNonExistingMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        measurement.setId(longCount.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, measurementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        measurement.setId(longCount.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeasurement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        measurement.setId(longCount.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(measurementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Measurement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeasurement() throws Exception {
        // Initialize the database
        insertedMeasurement = measurementRepository.saveAndFlush(measurement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the measurement
        restMeasurementMockMvc
            .perform(delete(ENTITY_API_URL_ID, measurement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return measurementRepository.count();
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

    protected Measurement getPersistedMeasurement(Measurement measurement) {
        return measurementRepository.findById(measurement.getId()).orElseThrow();
    }

    protected void assertPersistedMeasurementToMatchAllProperties(Measurement expectedMeasurement) {
        assertMeasurementAllPropertiesEquals(expectedMeasurement, getPersistedMeasurement(expectedMeasurement));
    }

    protected void assertPersistedMeasurementToMatchUpdatableProperties(Measurement expectedMeasurement) {
        assertMeasurementAllUpdatablePropertiesEquals(expectedMeasurement, getPersistedMeasurement(expectedMeasurement));
    }
}
