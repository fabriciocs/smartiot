package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.MeterAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Meter;
import br.com.supera.smartiot.repository.MeterRepository;
import br.com.supera.smartiot.service.dto.MeterDTO;
import br.com.supera.smartiot.service.mapper.MeterMapper;
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
 * Integration tests for the {@link MeterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeterResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private MeterMapper meterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeterMockMvc;

    private Meter meter;

    private Meter insertedMeter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meter createEntity(EntityManager em) {
        Meter meter = new Meter().serialNumber(DEFAULT_SERIAL_NUMBER).location(DEFAULT_LOCATION);
        return meter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meter createUpdatedEntity(EntityManager em) {
        Meter meter = new Meter().serialNumber(UPDATED_SERIAL_NUMBER).location(UPDATED_LOCATION);
        return meter;
    }

    @BeforeEach
    public void initTest() {
        meter = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMeter != null) {
            meterRepository.delete(insertedMeter);
            insertedMeter = null;
        }
    }

    @Test
    @Transactional
    void createMeter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Meter
        MeterDTO meterDTO = meterMapper.toDto(meter);
        var returnedMeterDTO = om.readValue(
            restMeterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MeterDTO.class
        );

        // Validate the Meter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMeter = meterMapper.toEntity(returnedMeterDTO);
        assertMeterUpdatableFieldsEquals(returnedMeter, getPersistedMeter(returnedMeter));

        insertedMeter = returnedMeter;
    }

    @Test
    @Transactional
    void createMeterWithExistingId() throws Exception {
        // Create the Meter with an existing ID
        meter.setId(1L);
        MeterDTO meterDTO = meterMapper.toDto(meter);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        meter.setSerialNumber(null);

        // Create the Meter, which fails.
        MeterDTO meterDTO = meterMapper.toDto(meter);

        restMeterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        meter.setLocation(null);

        // Create the Meter, which fails.
        MeterDTO meterDTO = meterMapper.toDto(meter);

        restMeterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeters() throws Exception {
        // Initialize the database
        insertedMeter = meterRepository.saveAndFlush(meter);

        // Get all the meterList
        restMeterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meter.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    void getMeter() throws Exception {
        // Initialize the database
        insertedMeter = meterRepository.saveAndFlush(meter);

        // Get the meter
        restMeterMockMvc
            .perform(get(ENTITY_API_URL_ID, meter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meter.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getNonExistingMeter() throws Exception {
        // Get the meter
        restMeterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeter() throws Exception {
        // Initialize the database
        insertedMeter = meterRepository.saveAndFlush(meter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the meter
        Meter updatedMeter = meterRepository.findById(meter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMeter are not directly saved in db
        em.detach(updatedMeter);
        updatedMeter.serialNumber(UPDATED_SERIAL_NUMBER).location(UPDATED_LOCATION);
        MeterDTO meterDTO = meterMapper.toDto(updatedMeter);

        restMeterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meterDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Meter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMeterToMatchAllProperties(updatedMeter);
    }

    @Test
    @Transactional
    void putNonExistingMeter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meter.setId(longCount.incrementAndGet());

        // Create the Meter
        MeterDTO meterDTO = meterMapper.toDto(meter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meterDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meter.setId(longCount.incrementAndGet());

        // Create the Meter
        MeterDTO meterDTO = meterMapper.toDto(meter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(meterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meter.setId(longCount.incrementAndGet());

        // Create the Meter
        MeterDTO meterDTO = meterMapper.toDto(meter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeterWithPatch() throws Exception {
        // Initialize the database
        insertedMeter = meterRepository.saveAndFlush(meter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the meter using partial update
        Meter partialUpdatedMeter = new Meter();
        partialUpdatedMeter.setId(meter.getId());

        restMeterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMeter))
            )
            .andExpect(status().isOk());

        // Validate the Meter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMeterUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMeter, meter), getPersistedMeter(meter));
    }

    @Test
    @Transactional
    void fullUpdateMeterWithPatch() throws Exception {
        // Initialize the database
        insertedMeter = meterRepository.saveAndFlush(meter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the meter using partial update
        Meter partialUpdatedMeter = new Meter();
        partialUpdatedMeter.setId(meter.getId());

        partialUpdatedMeter.serialNumber(UPDATED_SERIAL_NUMBER).location(UPDATED_LOCATION);

        restMeterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMeter))
            )
            .andExpect(status().isOk());

        // Validate the Meter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMeterUpdatableFieldsEquals(partialUpdatedMeter, getPersistedMeter(partialUpdatedMeter));
    }

    @Test
    @Transactional
    void patchNonExistingMeter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meter.setId(longCount.incrementAndGet());

        // Create the Meter
        MeterDTO meterDTO = meterMapper.toDto(meter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(meterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meter.setId(longCount.incrementAndGet());

        // Create the Meter
        MeterDTO meterDTO = meterMapper.toDto(meter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(meterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meter.setId(longCount.incrementAndGet());

        // Create the Meter
        MeterDTO meterDTO = meterMapper.toDto(meter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(meterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeter() throws Exception {
        // Initialize the database
        insertedMeter = meterRepository.saveAndFlush(meter);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the meter
        restMeterMockMvc
            .perform(delete(ENTITY_API_URL_ID, meter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return meterRepository.count();
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

    protected Meter getPersistedMeter(Meter meter) {
        return meterRepository.findById(meter.getId()).orElseThrow();
    }

    protected void assertPersistedMeterToMatchAllProperties(Meter expectedMeter) {
        assertMeterAllPropertiesEquals(expectedMeter, getPersistedMeter(expectedMeter));
    }

    protected void assertPersistedMeterToMatchUpdatableProperties(Meter expectedMeter) {
        assertMeterAllUpdatablePropertiesEquals(expectedMeter, getPersistedMeter(expectedMeter));
    }
}
