package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.FeedbackFormAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.FeedbackForm;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.FeedbackFormRepository;
import br.com.supera.feedback360.service.FeedbackFormService;
import br.com.supera.feedback360.service.dto.FeedbackFormDTO;
import br.com.supera.feedback360.service.mapper.FeedbackFormMapper;
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
 * Integration tests for the {@link FeedbackFormResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FeedbackFormResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/feedback-forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackFormRepository feedbackFormRepository;

    @Mock
    private FeedbackFormRepository feedbackFormRepositoryMock;

    @Autowired
    private FeedbackFormMapper feedbackFormMapper;

    @Mock
    private FeedbackFormService feedbackFormServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FeedbackForm feedbackForm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackForm createEntity(EntityManager em) {
        FeedbackForm feedbackForm = new FeedbackForm()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return feedbackForm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackForm createUpdatedEntity(EntityManager em) {
        FeedbackForm feedbackForm = new FeedbackForm()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return feedbackForm;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FeedbackForm.class).block();
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
        feedbackForm = createEntity(em);
    }

    @Test
    void createFeedbackForm() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeedbackForm
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);
        var returnedFeedbackFormDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FeedbackFormDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the FeedbackForm in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeedbackForm = feedbackFormMapper.toEntity(returnedFeedbackFormDTO);
        assertFeedbackFormUpdatableFieldsEquals(returnedFeedbackForm, getPersistedFeedbackForm(returnedFeedbackForm));
    }

    @Test
    void createFeedbackFormWithExistingId() throws Exception {
        // Create the FeedbackForm with an existing ID
        feedbackForm.setId(1L);
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackForm in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackForm.setTitle(null);

        // Create the FeedbackForm, which fails.
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackForm.setStatus(null);

        // Create the FeedbackForm, which fails.
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackForm.setCreatedAt(null);

        // Create the FeedbackForm, which fails.
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFeedbackForms() {
        // Initialize the database
        feedbackFormRepository.save(feedbackForm).block();

        // Get all the feedbackFormList
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
            .value(hasItem(feedbackForm.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackFormsWithEagerRelationshipsIsEnabled() {
        when(feedbackFormServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(feedbackFormServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackFormsWithEagerRelationshipsIsNotEnabled() {
        when(feedbackFormServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(feedbackFormRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getFeedbackForm() {
        // Initialize the database
        feedbackFormRepository.save(feedbackForm).block();

        // Get the feedbackForm
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, feedbackForm.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(feedbackForm.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getNonExistingFeedbackForm() {
        // Get the feedbackForm
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFeedbackForm() throws Exception {
        // Initialize the database
        feedbackFormRepository.save(feedbackForm).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackForm
        FeedbackForm updatedFeedbackForm = feedbackFormRepository.findById(feedbackForm.getId()).block();
        updatedFeedbackForm
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(updatedFeedbackForm);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feedbackFormDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackFormToMatchAllProperties(updatedFeedbackForm);
    }

    @Test
    void putNonExistingFeedbackForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackForm.setId(longCount.incrementAndGet());

        // Create the FeedbackForm
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feedbackFormDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFeedbackForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackForm.setId(longCount.incrementAndGet());

        // Create the FeedbackForm
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFeedbackForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackForm.setId(longCount.incrementAndGet());

        // Create the FeedbackForm
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FeedbackForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFeedbackFormWithPatch() throws Exception {
        // Initialize the database
        feedbackFormRepository.save(feedbackForm).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackForm using partial update
        FeedbackForm partialUpdatedFeedbackForm = new FeedbackForm();
        partialUpdatedFeedbackForm.setId(feedbackForm.getId());

        partialUpdatedFeedbackForm
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedbackForm.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFeedbackForm))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackForm in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackFormUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeedbackForm, feedbackForm),
            getPersistedFeedbackForm(feedbackForm)
        );
    }

    @Test
    void fullUpdateFeedbackFormWithPatch() throws Exception {
        // Initialize the database
        feedbackFormRepository.save(feedbackForm).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackForm using partial update
        FeedbackForm partialUpdatedFeedbackForm = new FeedbackForm();
        partialUpdatedFeedbackForm.setId(feedbackForm.getId());

        partialUpdatedFeedbackForm
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedbackForm.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFeedbackForm))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackForm in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackFormUpdatableFieldsEquals(partialUpdatedFeedbackForm, getPersistedFeedbackForm(partialUpdatedFeedbackForm));
    }

    @Test
    void patchNonExistingFeedbackForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackForm.setId(longCount.incrementAndGet());

        // Create the FeedbackForm
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, feedbackFormDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFeedbackForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackForm.setId(longCount.incrementAndGet());

        // Create the FeedbackForm
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFeedbackForm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackForm.setId(longCount.incrementAndGet());

        // Create the FeedbackForm
        FeedbackFormDTO feedbackFormDTO = feedbackFormMapper.toDto(feedbackForm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(feedbackFormDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FeedbackForm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFeedbackForm() {
        // Initialize the database
        feedbackFormRepository.save(feedbackForm).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedbackForm
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, feedbackForm.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackFormRepository.count().block();
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

    protected FeedbackForm getPersistedFeedbackForm(FeedbackForm feedbackForm) {
        return feedbackFormRepository.findById(feedbackForm.getId()).block();
    }

    protected void assertPersistedFeedbackFormToMatchAllProperties(FeedbackForm expectedFeedbackForm) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFeedbackFormAllPropertiesEquals(expectedFeedbackForm, getPersistedFeedbackForm(expectedFeedbackForm));
        assertFeedbackFormUpdatableFieldsEquals(expectedFeedbackForm, getPersistedFeedbackForm(expectedFeedbackForm));
    }

    protected void assertPersistedFeedbackFormToMatchUpdatableProperties(FeedbackForm expectedFeedbackForm) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFeedbackFormAllUpdatablePropertiesEquals(expectedFeedbackForm, getPersistedFeedbackForm(expectedFeedbackForm));
        assertFeedbackFormUpdatableFieldsEquals(expectedFeedbackForm, getPersistedFeedbackForm(expectedFeedbackForm));
    }
}
