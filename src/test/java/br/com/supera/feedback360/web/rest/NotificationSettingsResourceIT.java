package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.NotificationSettingsAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.NotificationSettings;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.NotificationSettingsRepository;
import br.com.supera.feedback360.service.NotificationSettingsService;
import br.com.supera.feedback360.service.dto.NotificationSettingsDTO;
import br.com.supera.feedback360.service.mapper.NotificationSettingsMapper;
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
 * Integration tests for the {@link NotificationSettingsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NotificationSettingsResourceIT {

    private static final String DEFAULT_PREFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_PREFERENCES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/notification-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;

    @Mock
    private NotificationSettingsRepository notificationSettingsRepositoryMock;

    @Autowired
    private NotificationSettingsMapper notificationSettingsMapper;

    @Mock
    private NotificationSettingsService notificationSettingsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private NotificationSettings notificationSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationSettings createEntity(EntityManager em) {
        NotificationSettings notificationSettings = new NotificationSettings().preferences(DEFAULT_PREFERENCES);
        return notificationSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationSettings createUpdatedEntity(EntityManager em) {
        NotificationSettings notificationSettings = new NotificationSettings().preferences(UPDATED_PREFERENCES);
        return notificationSettings;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(NotificationSettings.class).block();
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
        notificationSettings = createEntity(em);
    }

    @Test
    void createNotificationSettings() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);
        var returnedNotificationSettingsDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(NotificationSettingsDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the NotificationSettings in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationSettings = notificationSettingsMapper.toEntity(returnedNotificationSettingsDTO);
        assertNotificationSettingsUpdatableFieldsEquals(
            returnedNotificationSettings,
            getPersistedNotificationSettings(returnedNotificationSettings)
        );
    }

    @Test
    void createNotificationSettingsWithExistingId() throws Exception {
        // Create the NotificationSettings with an existing ID
        notificationSettings.setId(1L);
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkPreferencesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationSettings.setPreferences(null);

        // Create the NotificationSettings, which fails.
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllNotificationSettingsAsStream() {
        // Initialize the database
        notificationSettingsRepository.save(notificationSettings).block();

        List<NotificationSettings> notificationSettingsList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(NotificationSettingsDTO.class)
            .getResponseBody()
            .map(notificationSettingsMapper::toEntity)
            .filter(notificationSettings::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(notificationSettingsList).isNotNull();
        assertThat(notificationSettingsList).hasSize(1);
        NotificationSettings testNotificationSettings = notificationSettingsList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertNotificationSettingsAllPropertiesEquals(notificationSettings, testNotificationSettings);
        assertNotificationSettingsUpdatableFieldsEquals(notificationSettings, testNotificationSettings);
    }

    @Test
    void getAllNotificationSettings() {
        // Initialize the database
        notificationSettingsRepository.save(notificationSettings).block();

        // Get all the notificationSettingsList
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
            .value(hasItem(notificationSettings.getId().intValue()))
            .jsonPath("$.[*].preferences")
            .value(hasItem(DEFAULT_PREFERENCES));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificationSettingsWithEagerRelationshipsIsEnabled() {
        when(notificationSettingsServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(notificationSettingsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificationSettingsWithEagerRelationshipsIsNotEnabled() {
        when(notificationSettingsServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(notificationSettingsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getNotificationSettings() {
        // Initialize the database
        notificationSettingsRepository.save(notificationSettings).block();

        // Get the notificationSettings
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, notificationSettings.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(notificationSettings.getId().intValue()))
            .jsonPath("$.preferences")
            .value(is(DEFAULT_PREFERENCES));
    }

    @Test
    void getNonExistingNotificationSettings() {
        // Get the notificationSettings
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNotificationSettings() throws Exception {
        // Initialize the database
        notificationSettingsRepository.save(notificationSettings).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationSettings
        NotificationSettings updatedNotificationSettings = notificationSettingsRepository.findById(notificationSettings.getId()).block();
        updatedNotificationSettings.preferences(UPDATED_PREFERENCES);
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(updatedNotificationSettings);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationSettingsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NotificationSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationSettingsToMatchAllProperties(updatedNotificationSettings);
    }

    @Test
    void putNonExistingNotificationSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSettings.setId(longCount.incrementAndGet());

        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationSettingsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNotificationSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSettings.setId(longCount.incrementAndGet());

        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNotificationSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSettings.setId(longCount.incrementAndGet());

        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NotificationSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNotificationSettingsWithPatch() throws Exception {
        // Initialize the database
        notificationSettingsRepository.save(notificationSettings).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationSettings using partial update
        NotificationSettings partialUpdatedNotificationSettings = new NotificationSettings();
        partialUpdatedNotificationSettings.setId(notificationSettings.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotificationSettings.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedNotificationSettings))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NotificationSettings in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationSettingsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationSettings, notificationSettings),
            getPersistedNotificationSettings(notificationSettings)
        );
    }

    @Test
    void fullUpdateNotificationSettingsWithPatch() throws Exception {
        // Initialize the database
        notificationSettingsRepository.save(notificationSettings).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationSettings using partial update
        NotificationSettings partialUpdatedNotificationSettings = new NotificationSettings();
        partialUpdatedNotificationSettings.setId(notificationSettings.getId());

        partialUpdatedNotificationSettings.preferences(UPDATED_PREFERENCES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotificationSettings.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedNotificationSettings))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NotificationSettings in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationSettingsUpdatableFieldsEquals(
            partialUpdatedNotificationSettings,
            getPersistedNotificationSettings(partialUpdatedNotificationSettings)
        );
    }

    @Test
    void patchNonExistingNotificationSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSettings.setId(longCount.incrementAndGet());

        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, notificationSettingsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNotificationSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSettings.setId(longCount.incrementAndGet());

        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNotificationSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSettings.setId(longCount.incrementAndGet());

        // Create the NotificationSettings
        NotificationSettingsDTO notificationSettingsDTO = notificationSettingsMapper.toDto(notificationSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationSettingsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NotificationSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNotificationSettings() {
        // Initialize the database
        notificationSettingsRepository.save(notificationSettings).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationSettings
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, notificationSettings.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationSettingsRepository.count().block();
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

    protected NotificationSettings getPersistedNotificationSettings(NotificationSettings notificationSettings) {
        return notificationSettingsRepository.findById(notificationSettings.getId()).block();
    }

    protected void assertPersistedNotificationSettingsToMatchAllProperties(NotificationSettings expectedNotificationSettings) {
        // Test fails because reactive api returns an empty object instead of null
        // assertNotificationSettingsAllPropertiesEquals(expectedNotificationSettings, getPersistedNotificationSettings(expectedNotificationSettings));
        assertNotificationSettingsUpdatableFieldsEquals(
            expectedNotificationSettings,
            getPersistedNotificationSettings(expectedNotificationSettings)
        );
    }

    protected void assertPersistedNotificationSettingsToMatchUpdatableProperties(NotificationSettings expectedNotificationSettings) {
        // Test fails because reactive api returns an empty object instead of null
        // assertNotificationSettingsAllUpdatablePropertiesEquals(expectedNotificationSettings, getPersistedNotificationSettings(expectedNotificationSettings));
        assertNotificationSettingsUpdatableFieldsEquals(
            expectedNotificationSettings,
            getPersistedNotificationSettings(expectedNotificationSettings)
        );
    }
}
