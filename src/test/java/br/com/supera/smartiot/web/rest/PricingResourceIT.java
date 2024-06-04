package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.PricingAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.smartiot.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Pricing;
import br.com.supera.smartiot.repository.PricingRepository;
import br.com.supera.smartiot.service.dto.PricingDTO;
import br.com.supera.smartiot.service.mapper.PricingMapper;
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
 * Integration tests for the {@link PricingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PricingResourceIT {

    private static final String DEFAULT_SERVICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/pricings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private PricingMapper pricingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPricingMockMvc;

    private Pricing pricing;

    private Pricing insertedPricing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pricing createEntity(EntityManager em) {
        Pricing pricing = new Pricing().serviceType(DEFAULT_SERVICE_TYPE).price(DEFAULT_PRICE);
        return pricing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pricing createUpdatedEntity(EntityManager em) {
        Pricing pricing = new Pricing().serviceType(UPDATED_SERVICE_TYPE).price(UPDATED_PRICE);
        return pricing;
    }

    @BeforeEach
    public void initTest() {
        pricing = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPricing != null) {
            pricingRepository.delete(insertedPricing);
            insertedPricing = null;
        }
    }

    @Test
    @Transactional
    void createPricing() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);
        var returnedPricingDTO = om.readValue(
            restPricingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PricingDTO.class
        );

        // Validate the Pricing in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPricing = pricingMapper.toEntity(returnedPricingDTO);
        assertPricingUpdatableFieldsEquals(returnedPricing, getPersistedPricing(returnedPricing));

        insertedPricing = returnedPricing;
    }

    @Test
    @Transactional
    void createPricingWithExistingId() throws Exception {
        // Create the Pricing with an existing ID
        pricing.setId(1L);
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkServiceTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pricing.setServiceType(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        restPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pricing.setPrice(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        restPricingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPricings() throws Exception {
        // Initialize the database
        insertedPricing = pricingRepository.saveAndFlush(pricing);

        // Get all the pricingList
        restPricingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pricing.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getPricing() throws Exception {
        // Initialize the database
        insertedPricing = pricingRepository.saveAndFlush(pricing);

        // Get the pricing
        restPricingMockMvc
            .perform(get(ENTITY_API_URL_ID, pricing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pricing.getId().intValue()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingPricing() throws Exception {
        // Get the pricing
        restPricingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPricing() throws Exception {
        // Initialize the database
        insertedPricing = pricingRepository.saveAndFlush(pricing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pricing
        Pricing updatedPricing = pricingRepository.findById(pricing.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPricing are not directly saved in db
        em.detach(updatedPricing);
        updatedPricing.serviceType(UPDATED_SERVICE_TYPE).price(UPDATED_PRICE);
        PricingDTO pricingDTO = pricingMapper.toDto(updatedPricing);

        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pricingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPricingToMatchAllProperties(updatedPricing);
    }

    @Test
    @Transactional
    void putNonExistingPricing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricing.setId(longCount.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pricingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPricing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricing.setId(longCount.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPricing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricing.setId(longCount.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pricing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePricingWithPatch() throws Exception {
        // Initialize the database
        insertedPricing = pricingRepository.saveAndFlush(pricing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pricing using partial update
        Pricing partialUpdatedPricing = new Pricing();
        partialUpdatedPricing.setId(pricing.getId());

        partialUpdatedPricing.serviceType(UPDATED_SERVICE_TYPE);

        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPricing))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPricingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPricing, pricing), getPersistedPricing(pricing));
    }

    @Test
    @Transactional
    void fullUpdatePricingWithPatch() throws Exception {
        // Initialize the database
        insertedPricing = pricingRepository.saveAndFlush(pricing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pricing using partial update
        Pricing partialUpdatedPricing = new Pricing();
        partialUpdatedPricing.setId(pricing.getId());

        partialUpdatedPricing.serviceType(UPDATED_SERVICE_TYPE).price(UPDATED_PRICE);

        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPricing))
            )
            .andExpect(status().isOk());

        // Validate the Pricing in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPricingUpdatableFieldsEquals(partialUpdatedPricing, getPersistedPricing(partialUpdatedPricing));
    }

    @Test
    @Transactional
    void patchNonExistingPricing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricing.setId(longCount.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pricingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPricing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricing.setId(longCount.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pricing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPricing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricing.setId(longCount.incrementAndGet());

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.toDto(pricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pricingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pricing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePricing() throws Exception {
        // Initialize the database
        insertedPricing = pricingRepository.saveAndFlush(pricing);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pricing
        restPricingMockMvc
            .perform(delete(ENTITY_API_URL_ID, pricing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pricingRepository.count();
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

    protected Pricing getPersistedPricing(Pricing pricing) {
        return pricingRepository.findById(pricing.getId()).orElseThrow();
    }

    protected void assertPersistedPricingToMatchAllProperties(Pricing expectedPricing) {
        assertPricingAllPropertiesEquals(expectedPricing, getPersistedPricing(expectedPricing));
    }

    protected void assertPersistedPricingToMatchUpdatableProperties(Pricing expectedPricing) {
        assertPricingAllUpdatablePropertiesEquals(expectedPricing, getPersistedPricing(expectedPricing));
    }
}
