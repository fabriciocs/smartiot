package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.FeedbackResponseAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.FeedbackResponse;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.FeedbackResponseRepository;
import br.com.supera.feedback360.service.FeedbackResponseService;
import br.com.supera.feedback360.service.dto.FeedbackResponseDTO;
import br.com.supera.feedback360.service.mapper.FeedbackResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link FeedbackResponseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FeedbackResponseResourceIT {

    private static final String DEFAULT_RESPONSE_DATA = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_SUBMITTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/feedback-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackResponseRepository feedbackResponseRepository;

    @Mock
    private FeedbackResponseRepository feedbackResponseRepositoryMock;

    @Autowired
    private FeedbackResponseMapper feedbackResponseMapper;

    @Mock
    private FeedbackResponseService feedbackResponseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FeedbackResponse feedbackResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackResponse createEntity(EntityManager em) {
        FeedbackResponse feedbackResponse = new FeedbackResponse().responseData(DEFAULT_RESPONSE_DATA).submittedAt(DEFAULT_SUBMITTED_AT);
        return feedbackResponse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackResponse createUpdatedEntity(EntityManager em) {
        FeedbackResponse feedbackResponse = new FeedbackResponse().responseData(UPDATED_RESPONSE_DATA).submittedAt(UPDATED_SUBMITTED_AT);
        return feedbackResponse;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FeedbackResponse.class).block();
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
        feedbackResponse = createEntity(em);
    }

    @Test
    void createFeedbackResponse() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeedbackResponse
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);
        var returnedFeedbackResponseDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FeedbackResponseDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the FeedbackResponse in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeedbackResponse = feedbackResponseMapper.toEntity(returnedFeedbackResponseDTO);
        assertFeedbackResponseUpdatableFieldsEquals(returnedFeedbackResponse, getPersistedFeedbackResponse(returnedFeedbackResponse));
    }

    @Test
    void createFeedbackResponseWithExistingId() throws Exception {
        // Create the FeedbackResponse with an existing ID
        feedbackResponse.setId(1L);
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkResponseDataIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackResponse.setResponseData(null);

        // Create the FeedbackResponse, which fails.
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSubmittedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackResponse.setSubmittedAt(null);

        // Create the FeedbackResponse, which fails.
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFeedbackResponses() {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        // Get all the feedbackResponseList
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
            .value(hasItem(feedbackResponse.getId().intValue()))
            .jsonPath("$.[*].responseData")
            .value(hasItem(DEFAULT_RESPONSE_DATA))
            .jsonPath("$.[*].submittedAt")
            .value(hasItem(DEFAULT_SUBMITTED_AT.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackResponsesWithEagerRelationshipsIsEnabled() {
        when(feedbackResponseServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(feedbackResponseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackResponsesWithEagerRelationshipsIsNotEnabled() {
        when(feedbackResponseServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(feedbackResponseRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getFeedbackResponse() {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        // Get the feedbackResponse
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, feedbackResponse.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(feedbackResponse.getId().intValue()))
            .jsonPath("$.responseData")
            .value(is(DEFAULT_RESPONSE_DATA))
            .jsonPath("$.submittedAt")
            .value(is(DEFAULT_SUBMITTED_AT.toString()));
    }

    @Test
    void getNonExistingFeedbackResponse() {
        // Get the feedbackResponse
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFeedbackResponse() throws Exception {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackResponse
        FeedbackResponse updatedFeedbackResponse = feedbackResponseRepository.findById(feedbackResponse.getId()).block();
        updatedFeedbackResponse.responseData(UPDATED_RESPONSE_DATA).submittedAt(UPDATED_SUBMITTED_AT);
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(updatedFeedbackResponse);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feedbackResponseDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackResponseToMatchAllProperties(updatedFeedbackResponse);
    }

    @Test
    void putNonExistingFeedbackResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponse.setId(longCount.incrementAndGet());

        // Create the FeedbackResponse
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feedbackResponseDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFeedbackResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponse.setId(longCount.incrementAndGet());

        // Create the FeedbackResponse
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFeedbackResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponse.setId(longCount.incrementAndGet());

        // Create the FeedbackResponse
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FeedbackResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFeedbackResponseWithPatch() throws Exception {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackResponse using partial update
        FeedbackResponse partialUpdatedFeedbackResponse = new FeedbackResponse();
        partialUpdatedFeedbackResponse.setId(feedbackResponse.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedbackResponse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFeedbackResponse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackResponse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackResponseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeedbackResponse, feedbackResponse),
            getPersistedFeedbackResponse(feedbackResponse)
        );
    }

    @Test
    void fullUpdateFeedbackResponseWithPatch() throws Exception {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackResponse using partial update
        FeedbackResponse partialUpdatedFeedbackResponse = new FeedbackResponse();
        partialUpdatedFeedbackResponse.setId(feedbackResponse.getId());

        partialUpdatedFeedbackResponse.responseData(UPDATED_RESPONSE_DATA).submittedAt(UPDATED_SUBMITTED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedbackResponse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFeedbackResponse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackResponse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackResponseUpdatableFieldsEquals(
            partialUpdatedFeedbackResponse,
            getPersistedFeedbackResponse(partialUpdatedFeedbackResponse)
        );
    }

    @Test
    void patchNonExistingFeedbackResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponse.setId(longCount.incrementAndGet());

        // Create the FeedbackResponse
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, feedbackResponseDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFeedbackResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponse.setId(longCount.incrementAndGet());

        // Create the FeedbackResponse
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFeedbackResponse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponse.setId(longCount.incrementAndGet());

        // Create the FeedbackResponse
        FeedbackResponseDTO feedbackResponseDTO = feedbackResponseMapper.toDto(feedbackResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(feedbackResponseDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FeedbackResponse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFeedbackResponse() {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedbackResponse
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, feedbackResponse.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackResponseRepository.count().block();
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

    protected FeedbackResponse getPersistedFeedbackResponse(FeedbackResponse feedbackResponse) {
        return feedbackResponseRepository.findById(feedbackResponse.getId()).block();
    }

    protected void assertPersistedFeedbackResponseToMatchAllProperties(FeedbackResponse expectedFeedbackResponse) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFeedbackResponseAllPropertiesEquals(expectedFeedbackResponse, getPersistedFeedbackResponse(expectedFeedbackResponse));
        assertFeedbackResponseUpdatableFieldsEquals(expectedFeedbackResponse, getPersistedFeedbackResponse(expectedFeedbackResponse));
    }

    protected void assertPersistedFeedbackResponseToMatchUpdatableProperties(FeedbackResponse expectedFeedbackResponse) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFeedbackResponseAllUpdatablePropertiesEquals(expectedFeedbackResponse, getPersistedFeedbackResponse(expectedFeedbackResponse));
        assertFeedbackResponseUpdatableFieldsEquals(expectedFeedbackResponse, getPersistedFeedbackResponse(expectedFeedbackResponse));
    }
}
