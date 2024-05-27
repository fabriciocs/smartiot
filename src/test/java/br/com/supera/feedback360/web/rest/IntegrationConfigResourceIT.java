package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.IntegrationConfigAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.IntegrationConfig;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.IntegrationConfigRepository;
import br.com.supera.feedback360.service.IntegrationConfigService;
import br.com.supera.feedback360.service.dto.IntegrationConfigDTO;
import br.com.supera.feedback360.service.mapper.IntegrationConfigMapper;
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
 * Integration tests for the {@link IntegrationConfigResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IntegrationConfigResourceIT {

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/integration-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IntegrationConfigRepository integrationConfigRepository;

    @Mock
    private IntegrationConfigRepository integrationConfigRepositoryMock;

    @Autowired
    private IntegrationConfigMapper integrationConfigMapper;

    @Mock
    private IntegrationConfigService integrationConfigServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IntegrationConfig integrationConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegrationConfig createEntity(EntityManager em) {
        IntegrationConfig integrationConfig = new IntegrationConfig()
            .serviceName(DEFAULT_SERVICE_NAME)
            .configData(DEFAULT_CONFIG_DATA)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return integrationConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegrationConfig createUpdatedEntity(EntityManager em) {
        IntegrationConfig integrationConfig = new IntegrationConfig()
            .serviceName(UPDATED_SERVICE_NAME)
            .configData(UPDATED_CONFIG_DATA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return integrationConfig;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IntegrationConfig.class).block();
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
        integrationConfig = createEntity(em);
    }

    @Test
    void createIntegrationConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IntegrationConfig
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);
        var returnedIntegrationConfigDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(IntegrationConfigDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the IntegrationConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIntegrationConfig = integrationConfigMapper.toEntity(returnedIntegrationConfigDTO);
        assertIntegrationConfigUpdatableFieldsEquals(returnedIntegrationConfig, getPersistedIntegrationConfig(returnedIntegrationConfig));
    }

    @Test
    void createIntegrationConfigWithExistingId() throws Exception {
        // Create the IntegrationConfig with an existing ID
        integrationConfig.setId(1L);
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegrationConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkServiceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        integrationConfig.setServiceName(null);

        // Create the IntegrationConfig, which fails.
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkConfigDataIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        integrationConfig.setConfigData(null);

        // Create the IntegrationConfig, which fails.
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        integrationConfig.setCreatedAt(null);

        // Create the IntegrationConfig, which fails.
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllIntegrationConfigsAsStream() {
        // Initialize the database
        integrationConfigRepository.save(integrationConfig).block();

        List<IntegrationConfig> integrationConfigList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(IntegrationConfigDTO.class)
            .getResponseBody()
            .map(integrationConfigMapper::toEntity)
            .filter(integrationConfig::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(integrationConfigList).isNotNull();
        assertThat(integrationConfigList).hasSize(1);
        IntegrationConfig testIntegrationConfig = integrationConfigList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertIntegrationConfigAllPropertiesEquals(integrationConfig, testIntegrationConfig);
        assertIntegrationConfigUpdatableFieldsEquals(integrationConfig, testIntegrationConfig);
    }

    @Test
    void getAllIntegrationConfigs() {
        // Initialize the database
        integrationConfigRepository.save(integrationConfig).block();

        // Get all the integrationConfigList
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
            .value(hasItem(integrationConfig.getId().intValue()))
            .jsonPath("$.[*].serviceName")
            .value(hasItem(DEFAULT_SERVICE_NAME))
            .jsonPath("$.[*].configData")
            .value(hasItem(DEFAULT_CONFIG_DATA))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIntegrationConfigsWithEagerRelationshipsIsEnabled() {
        when(integrationConfigServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(integrationConfigServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIntegrationConfigsWithEagerRelationshipsIsNotEnabled() {
        when(integrationConfigServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(integrationConfigRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getIntegrationConfig() {
        // Initialize the database
        integrationConfigRepository.save(integrationConfig).block();

        // Get the integrationConfig
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, integrationConfig.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(integrationConfig.getId().intValue()))
            .jsonPath("$.serviceName")
            .value(is(DEFAULT_SERVICE_NAME))
            .jsonPath("$.configData")
            .value(is(DEFAULT_CONFIG_DATA))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getNonExistingIntegrationConfig() {
        // Get the integrationConfig
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIntegrationConfig() throws Exception {
        // Initialize the database
        integrationConfigRepository.save(integrationConfig).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integrationConfig
        IntegrationConfig updatedIntegrationConfig = integrationConfigRepository.findById(integrationConfig.getId()).block();
        updatedIntegrationConfig
            .serviceName(UPDATED_SERVICE_NAME)
            .configData(UPDATED_CONFIG_DATA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(updatedIntegrationConfig);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, integrationConfigDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegrationConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIntegrationConfigToMatchAllProperties(updatedIntegrationConfig);
    }

    @Test
    void putNonExistingIntegrationConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrationConfig.setId(longCount.incrementAndGet());

        // Create the IntegrationConfig
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, integrationConfigDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegrationConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIntegrationConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrationConfig.setId(longCount.incrementAndGet());

        // Create the IntegrationConfig
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegrationConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIntegrationConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrationConfig.setId(longCount.incrementAndGet());

        // Create the IntegrationConfig
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntegrationConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIntegrationConfigWithPatch() throws Exception {
        // Initialize the database
        integrationConfigRepository.save(integrationConfig).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integrationConfig using partial update
        IntegrationConfig partialUpdatedIntegrationConfig = new IntegrationConfig();
        partialUpdatedIntegrationConfig.setId(integrationConfig.getId());

        partialUpdatedIntegrationConfig.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntegrationConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedIntegrationConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegrationConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIntegrationConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIntegrationConfig, integrationConfig),
            getPersistedIntegrationConfig(integrationConfig)
        );
    }

    @Test
    void fullUpdateIntegrationConfigWithPatch() throws Exception {
        // Initialize the database
        integrationConfigRepository.save(integrationConfig).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integrationConfig using partial update
        IntegrationConfig partialUpdatedIntegrationConfig = new IntegrationConfig();
        partialUpdatedIntegrationConfig.setId(integrationConfig.getId());

        partialUpdatedIntegrationConfig
            .serviceName(UPDATED_SERVICE_NAME)
            .configData(UPDATED_CONFIG_DATA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntegrationConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedIntegrationConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegrationConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIntegrationConfigUpdatableFieldsEquals(
            partialUpdatedIntegrationConfig,
            getPersistedIntegrationConfig(partialUpdatedIntegrationConfig)
        );
    }

    @Test
    void patchNonExistingIntegrationConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrationConfig.setId(longCount.incrementAndGet());

        // Create the IntegrationConfig
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, integrationConfigDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegrationConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIntegrationConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrationConfig.setId(longCount.incrementAndGet());

        // Create the IntegrationConfig
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegrationConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIntegrationConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrationConfig.setId(longCount.incrementAndGet());

        // Create the IntegrationConfig
        IntegrationConfigDTO integrationConfigDTO = integrationConfigMapper.toDto(integrationConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(integrationConfigDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntegrationConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIntegrationConfig() {
        // Initialize the database
        integrationConfigRepository.save(integrationConfig).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the integrationConfig
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, integrationConfig.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return integrationConfigRepository.count().block();
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

    protected IntegrationConfig getPersistedIntegrationConfig(IntegrationConfig integrationConfig) {
        return integrationConfigRepository.findById(integrationConfig.getId()).block();
    }

    protected void assertPersistedIntegrationConfigToMatchAllProperties(IntegrationConfig expectedIntegrationConfig) {
        // Test fails because reactive api returns an empty object instead of null
        // assertIntegrationConfigAllPropertiesEquals(expectedIntegrationConfig, getPersistedIntegrationConfig(expectedIntegrationConfig));
        assertIntegrationConfigUpdatableFieldsEquals(expectedIntegrationConfig, getPersistedIntegrationConfig(expectedIntegrationConfig));
    }

    protected void assertPersistedIntegrationConfigToMatchUpdatableProperties(IntegrationConfig expectedIntegrationConfig) {
        // Test fails because reactive api returns an empty object instead of null
        // assertIntegrationConfigAllUpdatablePropertiesEquals(expectedIntegrationConfig, getPersistedIntegrationConfig(expectedIntegrationConfig));
        assertIntegrationConfigUpdatableFieldsEquals(expectedIntegrationConfig, getPersistedIntegrationConfig(expectedIntegrationConfig));
    }
}
