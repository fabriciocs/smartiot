package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.QuestionAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.Question;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.QuestionRepository;
import br.com.supera.feedback360.service.QuestionService;
import br.com.supera.feedback360.service.dto.QuestionDTO;
import br.com.supera.feedback360.service.mapper.QuestionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class QuestionResourceIT {

    private static final String DEFAULT_QUESTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionRepository questionRepository;

    @Mock
    private QuestionRepository questionRepositoryMock;

    @Autowired
    private QuestionMapper questionMapper;

    @Mock
    private QuestionService questionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Question question;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question().questionText(DEFAULT_QUESTION_TEXT).questionType(DEFAULT_QUESTION_TYPE);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question().questionText(UPDATED_QUESTION_TEXT).questionType(UPDATED_QUESTION_TYPE);
        return question;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Question.class).block();
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
        question = createEntity(em);
    }

    @Test
    void createQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        var returnedQuestionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(QuestionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Question in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestion = questionMapper.toEntity(returnedQuestionDTO);
        assertQuestionUpdatableFieldsEquals(returnedQuestion, getPersistedQuestion(returnedQuestion));
    }

    @Test
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuestionTextIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setQuestionText(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkQuestionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setQuestionType(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllQuestionsAsStream() {
        // Initialize the database
        questionRepository.save(question).block();

        List<Question> questionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(QuestionDTO.class)
            .getResponseBody()
            .map(questionMapper::toEntity)
            .filter(question::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(questionList).isNotNull();
        assertThat(questionList).hasSize(1);
        Question testQuestion = questionList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionAllPropertiesEquals(question, testQuestion);
        assertQuestionUpdatableFieldsEquals(question, testQuestion);
    }

    @Test
    void getAllQuestions() {
        // Initialize the database
        questionRepository.save(question).block();

        // Get all the questionList
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
            .value(hasItem(question.getId().intValue()))
            .jsonPath("$.[*].questionText")
            .value(hasItem(DEFAULT_QUESTION_TEXT))
            .jsonPath("$.[*].questionType")
            .value(hasItem(DEFAULT_QUESTION_TYPE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsEnabled() {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(questionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsNotEnabled() {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(questionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getQuestion() {
        // Initialize the database
        questionRepository.save(question).block();

        // Get the question
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, question.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(question.getId().intValue()))
            .jsonPath("$.questionText")
            .value(is(DEFAULT_QUESTION_TEXT))
            .jsonPath("$.questionType")
            .value(is(DEFAULT_QUESTION_TYPE));
    }

    @Test
    void getNonExistingQuestion() {
        // Get the question
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingQuestion() throws Exception {
        // Initialize the database
        questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).block();
        updatedQuestion.questionText(UPDATED_QUESTION_TEXT).questionType(UPDATED_QUESTION_TYPE);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionToMatchAllProperties(updatedQuestion);
    }

    @Test
    void putNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuestion, question), getPersistedQuestion(question));
    }

    @Test
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.save(question).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion.questionText(UPDATED_QUESTION_TEXT).questionType(UPDATED_QUESTION_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(partialUpdatedQuestion, getPersistedQuestion(partialUpdatedQuestion));
    }

    @Test
    void patchNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, questionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(questionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQuestion() {
        // Initialize the database
        questionRepository.save(question).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the question
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, question.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionRepository.count().block();
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

    protected Question getPersistedQuestion(Question question) {
        return questionRepository.findById(question.getId()).block();
    }

    protected void assertPersistedQuestionToMatchAllProperties(Question expectedQuestion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionAllPropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
        assertQuestionUpdatableFieldsEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }

    protected void assertPersistedQuestionToMatchUpdatableProperties(Question expectedQuestion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertQuestionAllUpdatablePropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
        assertQuestionUpdatableFieldsEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }
}
