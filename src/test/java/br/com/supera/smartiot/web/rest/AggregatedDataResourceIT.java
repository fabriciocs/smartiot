package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.AggregatedDataAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.smartiot.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.AggregatedData;
import br.com.supera.smartiot.repository.AggregatedDataRepository;
import br.com.supera.smartiot.service.dto.AggregatedDataDTO;
import br.com.supera.smartiot.service.mapper.AggregatedDataMapper;
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
 * Integration tests for the {@link AggregatedDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AggregatedDataResourceIT {

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_AGGREGATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_AGGREGATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/aggregated-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AggregatedDataRepository aggregatedDataRepository;

    @Autowired
    private AggregatedDataMapper aggregatedDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAggregatedDataMockMvc;

    private AggregatedData aggregatedData;

    private AggregatedData insertedAggregatedData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AggregatedData createEntity(EntityManager em) {
        AggregatedData aggregatedData = new AggregatedData()
            .dataType(DEFAULT_DATA_TYPE)
            .value(DEFAULT_VALUE)
            .aggregationTime(DEFAULT_AGGREGATION_TIME);
        return aggregatedData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AggregatedData createUpdatedEntity(EntityManager em) {
        AggregatedData aggregatedData = new AggregatedData()
            .dataType(UPDATED_DATA_TYPE)
            .value(UPDATED_VALUE)
            .aggregationTime(UPDATED_AGGREGATION_TIME);
        return aggregatedData;
    }

    @BeforeEach
    public void initTest() {
        aggregatedData = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAggregatedData != null) {
            aggregatedDataRepository.delete(insertedAggregatedData);
            insertedAggregatedData = null;
        }
    }

    @Test
    @Transactional
    void createAggregatedData() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AggregatedData
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);
        var returnedAggregatedDataDTO = om.readValue(
            restAggregatedDataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregatedDataDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AggregatedDataDTO.class
        );

        // Validate the AggregatedData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAggregatedData = aggregatedDataMapper.toEntity(returnedAggregatedDataDTO);
        assertAggregatedDataUpdatableFieldsEquals(returnedAggregatedData, getPersistedAggregatedData(returnedAggregatedData));

        insertedAggregatedData = returnedAggregatedData;
    }

    @Test
    @Transactional
    void createAggregatedDataWithExistingId() throws Exception {
        // Create the AggregatedData with an existing ID
        aggregatedData.setId(1L);
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAggregatedDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregatedDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AggregatedData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aggregatedData.setDataType(null);

        // Create the AggregatedData, which fails.
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        restAggregatedDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregatedDataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aggregatedData.setValue(null);

        // Create the AggregatedData, which fails.
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        restAggregatedDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregatedDataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAggregationTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aggregatedData.setAggregationTime(null);

        // Create the AggregatedData, which fails.
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        restAggregatedDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregatedDataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAggregatedData() throws Exception {
        // Initialize the database
        insertedAggregatedData = aggregatedDataRepository.saveAndFlush(aggregatedData);

        // Get all the aggregatedDataList
        restAggregatedDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aggregatedData.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].aggregationTime").value(hasItem(sameInstant(DEFAULT_AGGREGATION_TIME))));
    }

    @Test
    @Transactional
    void getAggregatedData() throws Exception {
        // Initialize the database
        insertedAggregatedData = aggregatedDataRepository.saveAndFlush(aggregatedData);

        // Get the aggregatedData
        restAggregatedDataMockMvc
            .perform(get(ENTITY_API_URL_ID, aggregatedData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aggregatedData.getId().intValue()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.aggregationTime").value(sameInstant(DEFAULT_AGGREGATION_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingAggregatedData() throws Exception {
        // Get the aggregatedData
        restAggregatedDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAggregatedData() throws Exception {
        // Initialize the database
        insertedAggregatedData = aggregatedDataRepository.saveAndFlush(aggregatedData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aggregatedData
        AggregatedData updatedAggregatedData = aggregatedDataRepository.findById(aggregatedData.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAggregatedData are not directly saved in db
        em.detach(updatedAggregatedData);
        updatedAggregatedData.dataType(UPDATED_DATA_TYPE).value(UPDATED_VALUE).aggregationTime(UPDATED_AGGREGATION_TIME);
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(updatedAggregatedData);

        restAggregatedDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aggregatedDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aggregatedDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the AggregatedData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAggregatedDataToMatchAllProperties(updatedAggregatedData);
    }

    @Test
    @Transactional
    void putNonExistingAggregatedData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregatedData.setId(longCount.incrementAndGet());

        // Create the AggregatedData
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAggregatedDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aggregatedDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aggregatedDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AggregatedData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAggregatedData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregatedData.setId(longCount.incrementAndGet());

        // Create the AggregatedData
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAggregatedDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aggregatedDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AggregatedData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAggregatedData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregatedData.setId(longCount.incrementAndGet());

        // Create the AggregatedData
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAggregatedDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aggregatedDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AggregatedData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAggregatedDataWithPatch() throws Exception {
        // Initialize the database
        insertedAggregatedData = aggregatedDataRepository.saveAndFlush(aggregatedData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aggregatedData using partial update
        AggregatedData partialUpdatedAggregatedData = new AggregatedData();
        partialUpdatedAggregatedData.setId(aggregatedData.getId());

        partialUpdatedAggregatedData.value(UPDATED_VALUE).aggregationTime(UPDATED_AGGREGATION_TIME);

        restAggregatedDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAggregatedData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAggregatedData))
            )
            .andExpect(status().isOk());

        // Validate the AggregatedData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAggregatedDataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAggregatedData, aggregatedData),
            getPersistedAggregatedData(aggregatedData)
        );
    }

    @Test
    @Transactional
    void fullUpdateAggregatedDataWithPatch() throws Exception {
        // Initialize the database
        insertedAggregatedData = aggregatedDataRepository.saveAndFlush(aggregatedData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aggregatedData using partial update
        AggregatedData partialUpdatedAggregatedData = new AggregatedData();
        partialUpdatedAggregatedData.setId(aggregatedData.getId());

        partialUpdatedAggregatedData.dataType(UPDATED_DATA_TYPE).value(UPDATED_VALUE).aggregationTime(UPDATED_AGGREGATION_TIME);

        restAggregatedDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAggregatedData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAggregatedData))
            )
            .andExpect(status().isOk());

        // Validate the AggregatedData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAggregatedDataUpdatableFieldsEquals(partialUpdatedAggregatedData, getPersistedAggregatedData(partialUpdatedAggregatedData));
    }

    @Test
    @Transactional
    void patchNonExistingAggregatedData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregatedData.setId(longCount.incrementAndGet());

        // Create the AggregatedData
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAggregatedDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aggregatedDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aggregatedDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AggregatedData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAggregatedData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregatedData.setId(longCount.incrementAndGet());

        // Create the AggregatedData
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAggregatedDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aggregatedDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AggregatedData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAggregatedData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aggregatedData.setId(longCount.incrementAndGet());

        // Create the AggregatedData
        AggregatedDataDTO aggregatedDataDTO = aggregatedDataMapper.toDto(aggregatedData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAggregatedDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aggregatedDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AggregatedData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAggregatedData() throws Exception {
        // Initialize the database
        insertedAggregatedData = aggregatedDataRepository.saveAndFlush(aggregatedData);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aggregatedData
        restAggregatedDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, aggregatedData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aggregatedDataRepository.count();
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

    protected AggregatedData getPersistedAggregatedData(AggregatedData aggregatedData) {
        return aggregatedDataRepository.findById(aggregatedData.getId()).orElseThrow();
    }

    protected void assertPersistedAggregatedDataToMatchAllProperties(AggregatedData expectedAggregatedData) {
        assertAggregatedDataAllPropertiesEquals(expectedAggregatedData, getPersistedAggregatedData(expectedAggregatedData));
    }

    protected void assertPersistedAggregatedDataToMatchUpdatableProperties(AggregatedData expectedAggregatedData) {
        assertAggregatedDataAllUpdatablePropertiesEquals(expectedAggregatedData, getPersistedAggregatedData(expectedAggregatedData));
    }
}
