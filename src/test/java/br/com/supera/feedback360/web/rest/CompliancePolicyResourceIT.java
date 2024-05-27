package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.CompliancePolicyAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.CompliancePolicy;
import br.com.supera.feedback360.repository.CompliancePolicyRepository;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.service.dto.CompliancePolicyDTO;
import br.com.supera.feedback360.service.mapper.CompliancePolicyMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CompliancePolicyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CompliancePolicyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RULES = "AAAAAAAAAA";
    private static final String UPDATED_RULES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/compliance-policies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompliancePolicyRepository compliancePolicyRepository;

    @Autowired
    private CompliancePolicyMapper compliancePolicyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CompliancePolicy compliancePolicy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompliancePolicy createEntity(EntityManager em) {
        CompliancePolicy compliancePolicy = new CompliancePolicy().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).rules(DEFAULT_RULES);
        return compliancePolicy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompliancePolicy createUpdatedEntity(EntityManager em) {
        CompliancePolicy compliancePolicy = new CompliancePolicy().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).rules(UPDATED_RULES);
        return compliancePolicy;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CompliancePolicy.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        compliancePolicy = createEntity(em);
    }

    @Test
    void createCompliancePolicy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompliancePolicy
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);
        var returnedCompliancePolicyDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CompliancePolicyDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CompliancePolicy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompliancePolicy = compliancePolicyMapper.toEntity(returnedCompliancePolicyDTO);
        assertCompliancePolicyUpdatableFieldsEquals(returnedCompliancePolicy, getPersistedCompliancePolicy(returnedCompliancePolicy));
    }

    @Test
    void createCompliancePolicyWithExistingId() throws Exception {
        // Create the CompliancePolicy with an existing ID
        compliancePolicy.setId(1L);
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompliancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compliancePolicy.setName(null);

        // Create the CompliancePolicy, which fails.
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkRulesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compliancePolicy.setRules(null);

        // Create the CompliancePolicy, which fails.
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCompliancePoliciesAsStream() {
        // Initialize the database
        compliancePolicyRepository.save(compliancePolicy).block();

        List<CompliancePolicy> compliancePolicyList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CompliancePolicyDTO.class)
            .getResponseBody()
            .map(compliancePolicyMapper::toEntity)
            .filter(compliancePolicy::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(compliancePolicyList).isNotNull();
        assertThat(compliancePolicyList).hasSize(1);
        CompliancePolicy testCompliancePolicy = compliancePolicyList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertCompliancePolicyAllPropertiesEquals(compliancePolicy, testCompliancePolicy);
        assertCompliancePolicyUpdatableFieldsEquals(compliancePolicy, testCompliancePolicy);
    }

    @Test
    void getAllCompliancePolicies() {
        // Initialize the database
        compliancePolicyRepository.save(compliancePolicy).block();

        // Get all the compliancePolicyList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(compliancePolicy.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].rules")
            .value(hasItem(DEFAULT_RULES));
    }

    @Test
    void getCompliancePolicy() {
        // Initialize the database
        compliancePolicyRepository.save(compliancePolicy).block();

        // Get the compliancePolicy
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, compliancePolicy.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(compliancePolicy.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.rules")
            .value(is(DEFAULT_RULES));
    }

    @Test
    void getNonExistingCompliancePolicy() {
        // Get the compliancePolicy
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCompliancePolicy() throws Exception {
        // Initialize the database
        compliancePolicyRepository.save(compliancePolicy).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compliancePolicy
        CompliancePolicy updatedCompliancePolicy = compliancePolicyRepository.findById(compliancePolicy.getId()).block();
        updatedCompliancePolicy.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).rules(UPDATED_RULES);
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(updatedCompliancePolicy);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, compliancePolicyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompliancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompliancePolicyToMatchAllProperties(updatedCompliancePolicy);
    }

    @Test
    void putNonExistingCompliancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compliancePolicy.setId(longCount.incrementAndGet());

        // Create the CompliancePolicy
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, compliancePolicyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompliancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCompliancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compliancePolicy.setId(longCount.incrementAndGet());

        // Create the CompliancePolicy
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompliancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCompliancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compliancePolicy.setId(longCount.incrementAndGet());

        // Create the CompliancePolicy
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CompliancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCompliancePolicyWithPatch() throws Exception {
        // Initialize the database
        compliancePolicyRepository.save(compliancePolicy).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compliancePolicy using partial update
        CompliancePolicy partialUpdatedCompliancePolicy = new CompliancePolicy();
        partialUpdatedCompliancePolicy.setId(compliancePolicy.getId());

        partialUpdatedCompliancePolicy.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompliancePolicy.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompliancePolicy))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompliancePolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompliancePolicyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompliancePolicy, compliancePolicy),
            getPersistedCompliancePolicy(compliancePolicy)
        );
    }

    @Test
    void fullUpdateCompliancePolicyWithPatch() throws Exception {
        // Initialize the database
        compliancePolicyRepository.save(compliancePolicy).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compliancePolicy using partial update
        CompliancePolicy partialUpdatedCompliancePolicy = new CompliancePolicy();
        partialUpdatedCompliancePolicy.setId(compliancePolicy.getId());

        partialUpdatedCompliancePolicy.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).rules(UPDATED_RULES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompliancePolicy.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompliancePolicy))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompliancePolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompliancePolicyUpdatableFieldsEquals(
            partialUpdatedCompliancePolicy,
            getPersistedCompliancePolicy(partialUpdatedCompliancePolicy)
        );
    }

    @Test
    void patchNonExistingCompliancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compliancePolicy.setId(longCount.incrementAndGet());

        // Create the CompliancePolicy
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, compliancePolicyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompliancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCompliancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compliancePolicy.setId(longCount.incrementAndGet());

        // Create the CompliancePolicy
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompliancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCompliancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compliancePolicy.setId(longCount.incrementAndGet());

        // Create the CompliancePolicy
        CompliancePolicyDTO compliancePolicyDTO = compliancePolicyMapper.toDto(compliancePolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(compliancePolicyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CompliancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCompliancePolicy() {
        // Initialize the database
        compliancePolicyRepository.save(compliancePolicy).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compliancePolicy
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, compliancePolicy.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compliancePolicyRepository.count().block();
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

    protected CompliancePolicy getPersistedCompliancePolicy(CompliancePolicy compliancePolicy) {
        return compliancePolicyRepository.findById(compliancePolicy.getId()).block();
    }

    protected void assertPersistedCompliancePolicyToMatchAllProperties(CompliancePolicy expectedCompliancePolicy) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCompliancePolicyAllPropertiesEquals(expectedCompliancePolicy, getPersistedCompliancePolicy(expectedCompliancePolicy));
        assertCompliancePolicyUpdatableFieldsEquals(expectedCompliancePolicy, getPersistedCompliancePolicy(expectedCompliancePolicy));
    }

    protected void assertPersistedCompliancePolicyToMatchUpdatableProperties(CompliancePolicy expectedCompliancePolicy) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCompliancePolicyAllUpdatablePropertiesEquals(expectedCompliancePolicy, getPersistedCompliancePolicy(expectedCompliancePolicy));
        assertCompliancePolicyUpdatableFieldsEquals(expectedCompliancePolicy, getPersistedCompliancePolicy(expectedCompliancePolicy));
    }
}
