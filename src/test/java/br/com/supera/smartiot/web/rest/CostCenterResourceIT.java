package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.CostCenterAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.smartiot.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.CostCenter;
import br.com.supera.smartiot.repository.CostCenterRepository;
import br.com.supera.smartiot.service.dto.CostCenterDTO;
import br.com.supera.smartiot.service.mapper.CostCenterMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CostCenterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CostCenterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BUDGET = new BigDecimal(1);
    private static final BigDecimal UPDATED_BUDGET = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/cost-centers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CostCenterRepository costCenterRepository;

    @Autowired
    private CostCenterMapper costCenterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCostCenterMockMvc;

    private CostCenter costCenter;

    private CostCenter insertedCostCenter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostCenter createEntity(EntityManager em) {
        CostCenter costCenter = new CostCenter().name(DEFAULT_NAME).budget(DEFAULT_BUDGET);
        return costCenter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostCenter createUpdatedEntity(EntityManager em) {
        CostCenter costCenter = new CostCenter().name(UPDATED_NAME).budget(UPDATED_BUDGET);
        return costCenter;
    }

    @BeforeEach
    public void initTest() {
        costCenter = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCostCenter != null) {
            costCenterRepository.delete(insertedCostCenter);
            insertedCostCenter = null;
        }
    }

    @Test
    @Transactional
    void createCostCenter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CostCenter
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);
        var returnedCostCenterDTO = om.readValue(
            restCostCenterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costCenterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CostCenterDTO.class
        );

        // Validate the CostCenter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCostCenter = costCenterMapper.toEntity(returnedCostCenterDTO);
        assertCostCenterUpdatableFieldsEquals(returnedCostCenter, getPersistedCostCenter(returnedCostCenter));

        insertedCostCenter = returnedCostCenter;
    }

    @Test
    @Transactional
    void createCostCenterWithExistingId() throws Exception {
        // Create the CostCenter with an existing ID
        costCenter.setId(1L);
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostCenterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costCenterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        costCenter.setName(null);

        // Create the CostCenter, which fails.
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        restCostCenterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costCenterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBudgetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        costCenter.setBudget(null);

        // Create the CostCenter, which fails.
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        restCostCenterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costCenterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCostCenters() throws Exception {
        // Initialize the database
        insertedCostCenter = costCenterRepository.saveAndFlush(costCenter);

        // Get all the costCenterList
        restCostCenterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costCenter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(sameNumber(DEFAULT_BUDGET))));
    }

    @Test
    @Transactional
    void getCostCenter() throws Exception {
        // Initialize the database
        insertedCostCenter = costCenterRepository.saveAndFlush(costCenter);

        // Get the costCenter
        restCostCenterMockMvc
            .perform(get(ENTITY_API_URL_ID, costCenter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(costCenter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.budget").value(sameNumber(DEFAULT_BUDGET)));
    }

    @Test
    @Transactional
    void getNonExistingCostCenter() throws Exception {
        // Get the costCenter
        restCostCenterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCostCenter() throws Exception {
        // Initialize the database
        insertedCostCenter = costCenterRepository.saveAndFlush(costCenter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the costCenter
        CostCenter updatedCostCenter = costCenterRepository.findById(costCenter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCostCenter are not directly saved in db
        em.detach(updatedCostCenter);
        updatedCostCenter.name(UPDATED_NAME).budget(UPDATED_BUDGET);
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(updatedCostCenter);

        restCostCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, costCenterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(costCenterDTO))
            )
            .andExpect(status().isOk());

        // Validate the CostCenter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCostCenterToMatchAllProperties(updatedCostCenter);
    }

    @Test
    @Transactional
    void putNonExistingCostCenter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costCenter.setId(longCount.incrementAndGet());

        // Create the CostCenter
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, costCenterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(costCenterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCostCenter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costCenter.setId(longCount.incrementAndGet());

        // Create the CostCenter
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(costCenterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCostCenter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costCenter.setId(longCount.incrementAndGet());

        // Create the CostCenter
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(costCenterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CostCenter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCostCenterWithPatch() throws Exception {
        // Initialize the database
        insertedCostCenter = costCenterRepository.saveAndFlush(costCenter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the costCenter using partial update
        CostCenter partialUpdatedCostCenter = new CostCenter();
        partialUpdatedCostCenter.setId(costCenter.getId());

        partialUpdatedCostCenter.name(UPDATED_NAME).budget(UPDATED_BUDGET);

        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCostCenter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCostCenter))
            )
            .andExpect(status().isOk());

        // Validate the CostCenter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCostCenterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCostCenter, costCenter),
            getPersistedCostCenter(costCenter)
        );
    }

    @Test
    @Transactional
    void fullUpdateCostCenterWithPatch() throws Exception {
        // Initialize the database
        insertedCostCenter = costCenterRepository.saveAndFlush(costCenter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the costCenter using partial update
        CostCenter partialUpdatedCostCenter = new CostCenter();
        partialUpdatedCostCenter.setId(costCenter.getId());

        partialUpdatedCostCenter.name(UPDATED_NAME).budget(UPDATED_BUDGET);

        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCostCenter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCostCenter))
            )
            .andExpect(status().isOk());

        // Validate the CostCenter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCostCenterUpdatableFieldsEquals(partialUpdatedCostCenter, getPersistedCostCenter(partialUpdatedCostCenter));
    }

    @Test
    @Transactional
    void patchNonExistingCostCenter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costCenter.setId(longCount.incrementAndGet());

        // Create the CostCenter
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, costCenterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(costCenterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCostCenter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costCenter.setId(longCount.incrementAndGet());

        // Create the CostCenter
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(costCenterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCostCenter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        costCenter.setId(longCount.incrementAndGet());

        // Create the CostCenter
        CostCenterDTO costCenterDTO = costCenterMapper.toDto(costCenter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(costCenterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CostCenter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCostCenter() throws Exception {
        // Initialize the database
        insertedCostCenter = costCenterRepository.saveAndFlush(costCenter);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the costCenter
        restCostCenterMockMvc
            .perform(delete(ENTITY_API_URL_ID, costCenter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return costCenterRepository.count();
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

    protected CostCenter getPersistedCostCenter(CostCenter costCenter) {
        return costCenterRepository.findById(costCenter.getId()).orElseThrow();
    }

    protected void assertPersistedCostCenterToMatchAllProperties(CostCenter expectedCostCenter) {
        assertCostCenterAllPropertiesEquals(expectedCostCenter, getPersistedCostCenter(expectedCostCenter));
    }

    protected void assertPersistedCostCenterToMatchUpdatableProperties(CostCenter expectedCostCenter) {
        assertCostCenterAllUpdatablePropertiesEquals(expectedCostCenter, getPersistedCostCenter(expectedCostCenter));
    }
}
