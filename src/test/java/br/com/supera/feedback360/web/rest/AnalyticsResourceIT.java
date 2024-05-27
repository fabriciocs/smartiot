package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.AnalyticsAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.Analytics;
import br.com.supera.feedback360.repository.AnalyticsRepository;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.service.dto.AnalyticsDTO;
import br.com.supera.feedback360.service.mapper.AnalyticsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AnalyticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AnalyticsResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/analytics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private AnalyticsMapper analyticsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Analytics analytics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analytics createEntity(EntityManager em) {
        Analytics analytics = new Analytics().type(DEFAULT_TYPE).data(DEFAULT_DATA).createdAt(DEFAULT_CREATED_AT);
        return analytics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analytics createUpdatedEntity(EntityManager em) {
        Analytics analytics = new Analytics().type(UPDATED_TYPE).data(UPDATED_DATA).createdAt(UPDATED_CREATED_AT);
        return analytics;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Analytics.class).block();
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
        analytics = createEntity(em);
    }

    @Test
    void createAnalytics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);
        var returnedAnalyticsDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AnalyticsDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Analytics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAnalytics = analyticsMapper.toEntity(returnedAnalyticsDTO);
        assertAnalyticsUpdatableFieldsEquals(returnedAnalytics, getPersistedAnalytics(returnedAnalytics));
    }

    @Test
    void createAnalyticsWithExistingId() throws Exception {
        // Create the Analytics with an existing ID
        analytics.setId(1L);
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Analytics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analytics.setType(null);

        // Create the Analytics, which fails.
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDataIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analytics.setData(null);

        // Create the Analytics, which fails.
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analytics.setCreatedAt(null);

        // Create the Analytics, which fails.
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAnalyticsAsStream() {
        // Initialize the database
        analyticsRepository.save(analytics).block();

        List<Analytics> analyticsList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(AnalyticsDTO.class)
            .getResponseBody()
            .map(analyticsMapper::toEntity)
            .filter(analytics::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(analyticsList).isNotNull();
        assertThat(analyticsList).hasSize(1);
        Analytics testAnalytics = analyticsList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertAnalyticsAllPropertiesEquals(analytics, testAnalytics);
        assertAnalyticsUpdatableFieldsEquals(analytics, testAnalytics);
    }

    @Test
    void getAllAnalytics() {
        // Initialize the database
        analyticsRepository.save(analytics).block();

        // Get all the analyticsList
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
            .value(hasItem(analytics.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].data")
            .value(hasItem(DEFAULT_DATA))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    void getAnalytics() {
        // Initialize the database
        analyticsRepository.save(analytics).block();

        // Get the analytics
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, analytics.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(analytics.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.data")
            .value(is(DEFAULT_DATA))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    void getNonExistingAnalytics() {
        // Get the analytics
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAnalytics() throws Exception {
        // Initialize the database
        analyticsRepository.save(analytics).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analytics
        Analytics updatedAnalytics = analyticsRepository.findById(analytics.getId()).block();
        updatedAnalytics.type(UPDATED_TYPE).data(UPDATED_DATA).createdAt(UPDATED_CREATED_AT);
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(updatedAnalytics);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, analyticsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Analytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnalyticsToMatchAllProperties(updatedAnalytics);
    }

    @Test
    void putNonExistingAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analytics.setId(longCount.incrementAndGet());

        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, analyticsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Analytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analytics.setId(longCount.incrementAndGet());

        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Analytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analytics.setId(longCount.incrementAndGet());

        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Analytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAnalyticsWithPatch() throws Exception {
        // Initialize the database
        analyticsRepository.save(analytics).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analytics using partial update
        Analytics partialUpdatedAnalytics = new Analytics();
        partialUpdatedAnalytics.setId(analytics.getId());

        partialUpdatedAnalytics.type(UPDATED_TYPE).data(UPDATED_DATA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAnalytics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAnalytics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Analytics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnalyticsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAnalytics, analytics),
            getPersistedAnalytics(analytics)
        );
    }

    @Test
    void fullUpdateAnalyticsWithPatch() throws Exception {
        // Initialize the database
        analyticsRepository.save(analytics).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analytics using partial update
        Analytics partialUpdatedAnalytics = new Analytics();
        partialUpdatedAnalytics.setId(analytics.getId());

        partialUpdatedAnalytics.type(UPDATED_TYPE).data(UPDATED_DATA).createdAt(UPDATED_CREATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAnalytics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAnalytics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Analytics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnalyticsUpdatableFieldsEquals(partialUpdatedAnalytics, getPersistedAnalytics(partialUpdatedAnalytics));
    }

    @Test
    void patchNonExistingAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analytics.setId(longCount.incrementAndGet());

        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, analyticsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Analytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analytics.setId(longCount.incrementAndGet());

        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Analytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAnalytics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analytics.setId(longCount.incrementAndGet());

        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(analyticsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Analytics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAnalytics() {
        // Initialize the database
        analyticsRepository.save(analytics).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the analytics
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, analytics.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return analyticsRepository.count().block();
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

    protected Analytics getPersistedAnalytics(Analytics analytics) {
        return analyticsRepository.findById(analytics.getId()).block();
    }

    protected void assertPersistedAnalyticsToMatchAllProperties(Analytics expectedAnalytics) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAnalyticsAllPropertiesEquals(expectedAnalytics, getPersistedAnalytics(expectedAnalytics));
        assertAnalyticsUpdatableFieldsEquals(expectedAnalytics, getPersistedAnalytics(expectedAnalytics));
    }

    protected void assertPersistedAnalyticsToMatchUpdatableProperties(Analytics expectedAnalytics) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAnalyticsAllUpdatablePropertiesEquals(expectedAnalytics, getPersistedAnalytics(expectedAnalytics));
        assertAnalyticsUpdatableFieldsEquals(expectedAnalytics, getPersistedAnalytics(expectedAnalytics));
    }
}
