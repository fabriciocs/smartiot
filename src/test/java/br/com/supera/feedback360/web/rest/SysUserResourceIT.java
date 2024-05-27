package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.SysUserAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.SysUserRepository;
import br.com.supera.feedback360.service.SysUserService;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import br.com.supera.feedback360.service.mapper.SysUserMapper;
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
 * Integration tests for the {@link SysUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SysUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sys-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Mock
    private SysUserRepository sysUserRepositoryMock;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Mock
    private SysUserService sysUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SysUser sysUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysUser createEntity(EntityManager em) {
        SysUser sysUser = new SysUser()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return sysUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysUser createUpdatedEntity(EntityManager em) {
        SysUser sysUser = new SysUser()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return sysUser;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SysUser.class).block();
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
        sysUser = createEntity(em);
    }

    @Test
    void createSysUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);
        var returnedSysUserDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SysUserDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the SysUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSysUser = sysUserMapper.toEntity(returnedSysUserDTO);
        assertSysUserUpdatableFieldsEquals(returnedSysUser, getPersistedSysUser(returnedSysUser));
    }

    @Test
    void createSysUserWithExistingId() throws Exception {
        // Create the SysUser with an existing ID
        sysUser.setId(1L);
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sysUser.setName(null);

        // Create the SysUser, which fails.
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sysUser.setEmail(null);

        // Create the SysUser, which fails.
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPasswordHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sysUser.setPasswordHash(null);

        // Create the SysUser, which fails.
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sysUser.setCreatedAt(null);

        // Create the SysUser, which fails.
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSysUsers() {
        // Initialize the database
        sysUserRepository.save(sysUser).block();

        // Get all the sysUserList
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
            .value(hasItem(sysUser.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].passwordHash")
            .value(hasItem(DEFAULT_PASSWORD_HASH))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSysUsersWithEagerRelationshipsIsEnabled() {
        when(sysUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(sysUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSysUsersWithEagerRelationshipsIsNotEnabled() {
        when(sysUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(sysUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSysUser() {
        // Initialize the database
        sysUserRepository.save(sysUser).block();

        // Get the sysUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sysUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sysUser.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.passwordHash")
            .value(is(DEFAULT_PASSWORD_HASH))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getNonExistingSysUser() {
        // Get the sysUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSysUser() throws Exception {
        // Initialize the database
        sysUserRepository.save(sysUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysUser
        SysUser updatedSysUser = sysUserRepository.findById(sysUser.getId()).block();
        updatedSysUser
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SysUserDTO sysUserDTO = sysUserMapper.toDto(updatedSysUser);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sysUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSysUserToMatchAllProperties(updatedSysUser);
    }

    @Test
    void putNonExistingSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sysUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSysUserWithPatch() throws Exception {
        // Initialize the database
        sysUserRepository.save(sysUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysUser using partial update
        SysUser partialUpdatedSysUser = new SysUser();
        partialUpdatedSysUser.setId(sysUser.getId());

        partialUpdatedSysUser.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSysUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSysUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SysUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSysUser, sysUser), getPersistedSysUser(sysUser));
    }

    @Test
    void fullUpdateSysUserWithPatch() throws Exception {
        // Initialize the database
        sysUserRepository.save(sysUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysUser using partial update
        SysUser partialUpdatedSysUser = new SysUser();
        partialUpdatedSysUser.setId(sysUser.getId());

        partialUpdatedSysUser
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSysUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSysUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SysUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysUserUpdatableFieldsEquals(partialUpdatedSysUser, getPersistedSysUser(partialUpdatedSysUser));
    }

    @Test
    void patchNonExistingSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sysUserDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sysUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSysUser() {
        // Initialize the database
        sysUserRepository.save(sysUser).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sysUser
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sysUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sysUserRepository.count().block();
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

    protected SysUser getPersistedSysUser(SysUser sysUser) {
        return sysUserRepository.findById(sysUser.getId()).block();
    }

    protected void assertPersistedSysUserToMatchAllProperties(SysUser expectedSysUser) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSysUserAllPropertiesEquals(expectedSysUser, getPersistedSysUser(expectedSysUser));
        assertSysUserUpdatableFieldsEquals(expectedSysUser, getPersistedSysUser(expectedSysUser));
    }

    protected void assertPersistedSysUserToMatchUpdatableProperties(SysUser expectedSysUser) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSysUserAllUpdatablePropertiesEquals(expectedSysUser, getPersistedSysUser(expectedSysUser));
        assertSysUserUpdatableFieldsEquals(expectedSysUser, getPersistedSysUser(expectedSysUser));
    }
}
