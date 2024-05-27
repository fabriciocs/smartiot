package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.ProfileAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.Profile;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.ProfileRepository;
import br.com.supera.feedback360.service.ProfileService;
import br.com.supera.feedback360.service.dto.ProfileDTO;
import br.com.supera.feedback360.service.mapper.ProfileMapper;
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
 * Integration tests for the {@link ProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProfileResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PICTURE = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_PREFERENCES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfileRepository profileRepository;

    @Mock
    private ProfileRepository profileRepositoryMock;

    @Autowired
    private ProfileMapper profileMapper;

    @Mock
    private ProfileService profileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Profile profile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .profilePicture(DEFAULT_PROFILE_PICTURE)
            .preferences(DEFAULT_PREFERENCES);
        return profile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createUpdatedEntity(EntityManager em) {
        Profile profile = new Profile()
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .profilePicture(UPDATED_PROFILE_PICTURE)
            .preferences(UPDATED_PREFERENCES);
        return profile;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Profile.class).block();
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
        profile = createEntity(em);
    }

    @Test
    void createProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        var returnedProfileDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ProfileDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Profile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfile = profileMapper.toEntity(returnedProfileDTO);
        assertProfileUpdatableFieldsEquals(returnedProfile, getPersistedProfile(returnedProfile));
    }

    @Test
    void createProfileWithExistingId() throws Exception {
        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProfilesAsStream() {
        // Initialize the database
        profileRepository.save(profile).block();

        List<Profile> profileList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ProfileDTO.class)
            .getResponseBody()
            .map(profileMapper::toEntity)
            .filter(profile::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(profileList).isNotNull();
        assertThat(profileList).hasSize(1);
        Profile testProfile = profileList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertProfileAllPropertiesEquals(profile, testProfile);
        assertProfileUpdatableFieldsEquals(profile, testProfile);
    }

    @Test
    void getAllProfiles() {
        // Initialize the database
        profileRepository.save(profile).block();

        // Get all the profileList
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
            .value(hasItem(profile.getId().intValue()))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS))
            .jsonPath("$.[*].profilePicture")
            .value(hasItem(DEFAULT_PROFILE_PICTURE))
            .jsonPath("$.[*].preferences")
            .value(hasItem(DEFAULT_PREFERENCES));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfilesWithEagerRelationshipsIsEnabled() {
        when(profileServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(profileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfilesWithEagerRelationshipsIsNotEnabled() {
        when(profileServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(profileRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getProfile() {
        // Initialize the database
        profileRepository.save(profile).block();

        // Get the profile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, profile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(profile.getId().intValue()))
            .jsonPath("$.phoneNumber")
            .value(is(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS))
            .jsonPath("$.profilePicture")
            .value(is(DEFAULT_PROFILE_PICTURE))
            .jsonPath("$.preferences")
            .value(is(DEFAULT_PREFERENCES));
    }

    @Test
    void getNonExistingProfile() {
        // Get the profile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProfile() throws Exception {
        // Initialize the database
        profileRepository.save(profile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).block();
        updatedProfile
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .profilePicture(UPDATED_PROFILE_PICTURE)
            .preferences(UPDATED_PREFERENCES);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, profileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfileToMatchAllProperties(updatedProfile);
    }

    @Test
    void putNonExistingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, profileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProfileWithPatch() throws Exception {
        // Initialize the database
        profileRepository.save(profile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile using partial update
        Profile partialUpdatedProfile = new Profile();
        partialUpdatedProfile.setId(profile.getId());

        partialUpdatedProfile.address(UPDATED_ADDRESS).profilePicture(UPDATED_PROFILE_PICTURE).preferences(UPDATED_PREFERENCES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProfile, profile), getPersistedProfile(profile));
    }

    @Test
    void fullUpdateProfileWithPatch() throws Exception {
        // Initialize the database
        profileRepository.save(profile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile using partial update
        Profile partialUpdatedProfile = new Profile();
        partialUpdatedProfile.setId(profile.getId());

        partialUpdatedProfile
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .profilePicture(UPDATED_PROFILE_PICTURE)
            .preferences(UPDATED_PREFERENCES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileUpdatableFieldsEquals(partialUpdatedProfile, getPersistedProfile(partialUpdatedProfile));
    }

    @Test
    void patchNonExistingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, profileDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(profileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProfile() {
        // Initialize the database
        profileRepository.save(profile).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profile
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, profile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profileRepository.count().block();
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

    protected Profile getPersistedProfile(Profile profile) {
        return profileRepository.findById(profile.getId()).block();
    }

    protected void assertPersistedProfileToMatchAllProperties(Profile expectedProfile) {
        // Test fails because reactive api returns an empty object instead of null
        // assertProfileAllPropertiesEquals(expectedProfile, getPersistedProfile(expectedProfile));
        assertProfileUpdatableFieldsEquals(expectedProfile, getPersistedProfile(expectedProfile));
    }

    protected void assertPersistedProfileToMatchUpdatableProperties(Profile expectedProfile) {
        // Test fails because reactive api returns an empty object instead of null
        // assertProfileAllUpdatablePropertiesEquals(expectedProfile, getPersistedProfile(expectedProfile));
        assertProfileUpdatableFieldsEquals(expectedProfile, getPersistedProfile(expectedProfile));
    }
}
