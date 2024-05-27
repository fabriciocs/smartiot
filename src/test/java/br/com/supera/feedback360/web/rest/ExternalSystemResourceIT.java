package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.ExternalSystemAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.ExternalSystem;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.ExternalSystemRepository;
import br.com.supera.feedback360.service.dto.ExternalSystemDTO;
import br.com.supera.feedback360.service.mapper.ExternalSystemMapper;
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
 * Integration tests for the {@link ExternalSystemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ExternalSystemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_API_ENDPOINT = "AAAAAAAAAA";
    private static final String UPDATED_API_ENDPOINT = "BBBBBBBBBB";

    private static final String DEFAULT_AUTH_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_AUTH_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/external-systems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExternalSystemRepository externalSystemRepository;

    @Autowired
    private ExternalSystemMapper externalSystemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ExternalSystem externalSystem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalSystem createEntity(EntityManager em) {
        ExternalSystem externalSystem = new ExternalSystem()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .apiEndpoint(DEFAULT_API_ENDPOINT)
            .authDetails(DEFAULT_AUTH_DETAILS);
        return externalSystem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalSystem createUpdatedEntity(EntityManager em) {
        ExternalSystem externalSystem = new ExternalSystem()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiEndpoint(UPDATED_API_ENDPOINT)
            .authDetails(UPDATED_AUTH_DETAILS);
        return externalSystem;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ExternalSystem.class).block();
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
        externalSystem = createEntity(em);
    }

    @Test
    void createExternalSystem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExternalSystem
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);
        var returnedExternalSystemDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ExternalSystemDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ExternalSystem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExternalSystem = externalSystemMapper.toEntity(returnedExternalSystemDTO);
        assertExternalSystemUpdatableFieldsEquals(returnedExternalSystem, getPersistedExternalSystem(returnedExternalSystem));
    }

    @Test
    void createExternalSystemWithExistingId() throws Exception {
        // Create the ExternalSystem with an existing ID
        externalSystem.setId(1L);
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExternalSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        externalSystem.setName(null);

        // Create the ExternalSystem, which fails.
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkApiEndpointIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        externalSystem.setApiEndpoint(null);

        // Create the ExternalSystem, which fails.
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkAuthDetailsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        externalSystem.setAuthDetails(null);

        // Create the ExternalSystem, which fails.
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllExternalSystemsAsStream() {
        // Initialize the database
        externalSystemRepository.save(externalSystem).block();

        List<ExternalSystem> externalSystemList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ExternalSystemDTO.class)
            .getResponseBody()
            .map(externalSystemMapper::toEntity)
            .filter(externalSystem::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(externalSystemList).isNotNull();
        assertThat(externalSystemList).hasSize(1);
        ExternalSystem testExternalSystem = externalSystemList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertExternalSystemAllPropertiesEquals(externalSystem, testExternalSystem);
        assertExternalSystemUpdatableFieldsEquals(externalSystem, testExternalSystem);
    }

    @Test
    void getAllExternalSystems() {
        // Initialize the database
        externalSystemRepository.save(externalSystem).block();

        // Get all the externalSystemList
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
            .value(hasItem(externalSystem.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].apiEndpoint")
            .value(hasItem(DEFAULT_API_ENDPOINT))
            .jsonPath("$.[*].authDetails")
            .value(hasItem(DEFAULT_AUTH_DETAILS));
    }

    @Test
    void getExternalSystem() {
        // Initialize the database
        externalSystemRepository.save(externalSystem).block();

        // Get the externalSystem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, externalSystem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(externalSystem.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.apiEndpoint")
            .value(is(DEFAULT_API_ENDPOINT))
            .jsonPath("$.authDetails")
            .value(is(DEFAULT_AUTH_DETAILS));
    }

    @Test
    void getNonExistingExternalSystem() {
        // Get the externalSystem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingExternalSystem() throws Exception {
        // Initialize the database
        externalSystemRepository.save(externalSystem).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalSystem
        ExternalSystem updatedExternalSystem = externalSystemRepository.findById(externalSystem.getId()).block();
        updatedExternalSystem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiEndpoint(UPDATED_API_ENDPOINT)
            .authDetails(UPDATED_AUTH_DETAILS);
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(updatedExternalSystem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, externalSystemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ExternalSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExternalSystemToMatchAllProperties(updatedExternalSystem);
    }

    @Test
    void putNonExistingExternalSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalSystem.setId(longCount.incrementAndGet());

        // Create the ExternalSystem
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, externalSystemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExternalSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExternalSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalSystem.setId(longCount.incrementAndGet());

        // Create the ExternalSystem
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExternalSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExternalSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalSystem.setId(longCount.incrementAndGet());

        // Create the ExternalSystem
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ExternalSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExternalSystemWithPatch() throws Exception {
        // Initialize the database
        externalSystemRepository.save(externalSystem).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalSystem using partial update
        ExternalSystem partialUpdatedExternalSystem = new ExternalSystem();
        partialUpdatedExternalSystem.setId(externalSystem.getId());

        partialUpdatedExternalSystem.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExternalSystem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedExternalSystem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ExternalSystem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalSystemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExternalSystem, externalSystem),
            getPersistedExternalSystem(externalSystem)
        );
    }

    @Test
    void fullUpdateExternalSystemWithPatch() throws Exception {
        // Initialize the database
        externalSystemRepository.save(externalSystem).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalSystem using partial update
        ExternalSystem partialUpdatedExternalSystem = new ExternalSystem();
        partialUpdatedExternalSystem.setId(externalSystem.getId());

        partialUpdatedExternalSystem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiEndpoint(UPDATED_API_ENDPOINT)
            .authDetails(UPDATED_AUTH_DETAILS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExternalSystem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedExternalSystem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ExternalSystem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalSystemUpdatableFieldsEquals(partialUpdatedExternalSystem, getPersistedExternalSystem(partialUpdatedExternalSystem));
    }

    @Test
    void patchNonExistingExternalSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalSystem.setId(longCount.incrementAndGet());

        // Create the ExternalSystem
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, externalSystemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExternalSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExternalSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalSystem.setId(longCount.incrementAndGet());

        // Create the ExternalSystem
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ExternalSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExternalSystem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalSystem.setId(longCount.incrementAndGet());

        // Create the ExternalSystem
        ExternalSystemDTO externalSystemDTO = externalSystemMapper.toDto(externalSystem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(externalSystemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ExternalSystem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExternalSystem() {
        // Initialize the database
        externalSystemRepository.save(externalSystem).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the externalSystem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, externalSystem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return externalSystemRepository.count().block();
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

    protected ExternalSystem getPersistedExternalSystem(ExternalSystem externalSystem) {
        return externalSystemRepository.findById(externalSystem.getId()).block();
    }

    protected void assertPersistedExternalSystemToMatchAllProperties(ExternalSystem expectedExternalSystem) {
        // Test fails because reactive api returns an empty object instead of null
        // assertExternalSystemAllPropertiesEquals(expectedExternalSystem, getPersistedExternalSystem(expectedExternalSystem));
        assertExternalSystemUpdatableFieldsEquals(expectedExternalSystem, getPersistedExternalSystem(expectedExternalSystem));
    }

    protected void assertPersistedExternalSystemToMatchUpdatableProperties(ExternalSystem expectedExternalSystem) {
        // Test fails because reactive api returns an empty object instead of null
        // assertExternalSystemAllUpdatablePropertiesEquals(expectedExternalSystem, getPersistedExternalSystem(expectedExternalSystem));
        assertExternalSystemUpdatableFieldsEquals(expectedExternalSystem, getPersistedExternalSystem(expectedExternalSystem));
    }
}
