package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.SysRoleAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.SysRole;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.SysRoleRepository;
import br.com.supera.feedback360.service.dto.SysRoleDTO;
import br.com.supera.feedback360.service.mapper.SysRoleMapper;
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
 * Integration tests for the {@link SysRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SysRoleResourceIT {

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sys-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SysRole sysRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysRole createEntity(EntityManager em) {
        SysRole sysRole = new SysRole().roleName(DEFAULT_ROLE_NAME).description(DEFAULT_DESCRIPTION);
        return sysRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysRole createUpdatedEntity(EntityManager em) {
        SysRole sysRole = new SysRole().roleName(UPDATED_ROLE_NAME).description(UPDATED_DESCRIPTION);
        return sysRole;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SysRole.class).block();
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
        sysRole = createEntity(em);
    }

    @Test
    void createSysRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SysRole
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);
        var returnedSysRoleDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SysRoleDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the SysRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSysRole = sysRoleMapper.toEntity(returnedSysRoleDTO);
        assertSysRoleUpdatableFieldsEquals(returnedSysRole, getPersistedSysRole(returnedSysRole));
    }

    @Test
    void createSysRoleWithExistingId() throws Exception {
        // Create the SysRole with an existing ID
        sysRole.setId(1L);
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkRoleNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sysRole.setRoleName(null);

        // Create the SysRole, which fails.
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSysRolesAsStream() {
        // Initialize the database
        sysRoleRepository.save(sysRole).block();

        List<SysRole> sysRoleList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SysRoleDTO.class)
            .getResponseBody()
            .map(sysRoleMapper::toEntity)
            .filter(sysRole::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sysRoleList).isNotNull();
        assertThat(sysRoleList).hasSize(1);
        SysRole testSysRole = sysRoleList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertSysRoleAllPropertiesEquals(sysRole, testSysRole);
        assertSysRoleUpdatableFieldsEquals(sysRole, testSysRole);
    }

    @Test
    void getAllSysRoles() {
        // Initialize the database
        sysRoleRepository.save(sysRole).block();

        // Get all the sysRoleList
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
            .value(hasItem(sysRole.getId().intValue()))
            .jsonPath("$.[*].roleName")
            .value(hasItem(DEFAULT_ROLE_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getSysRole() {
        // Initialize the database
        sysRoleRepository.save(sysRole).block();

        // Get the sysRole
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sysRole.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sysRole.getId().intValue()))
            .jsonPath("$.roleName")
            .value(is(DEFAULT_ROLE_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingSysRole() {
        // Get the sysRole
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSysRole() throws Exception {
        // Initialize the database
        sysRoleRepository.save(sysRole).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysRole
        SysRole updatedSysRole = sysRoleRepository.findById(sysRole.getId()).block();
        updatedSysRole.roleName(UPDATED_ROLE_NAME).description(UPDATED_DESCRIPTION);
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(updatedSysRole);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sysRoleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SysRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSysRoleToMatchAllProperties(updatedSysRole);
    }

    @Test
    void putNonExistingSysRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysRole.setId(longCount.incrementAndGet());

        // Create the SysRole
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sysRoleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSysRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysRole.setId(longCount.incrementAndGet());

        // Create the SysRole
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSysRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysRole.setId(longCount.incrementAndGet());

        // Create the SysRole
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SysRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSysRoleWithPatch() throws Exception {
        // Initialize the database
        sysRoleRepository.save(sysRole).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysRole using partial update
        SysRole partialUpdatedSysRole = new SysRole();
        partialUpdatedSysRole.setId(sysRole.getId());

        partialUpdatedSysRole.roleName(UPDATED_ROLE_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSysRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSysRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SysRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysRoleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSysRole, sysRole), getPersistedSysRole(sysRole));
    }

    @Test
    void fullUpdateSysRoleWithPatch() throws Exception {
        // Initialize the database
        sysRoleRepository.save(sysRole).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysRole using partial update
        SysRole partialUpdatedSysRole = new SysRole();
        partialUpdatedSysRole.setId(sysRole.getId());

        partialUpdatedSysRole.roleName(UPDATED_ROLE_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSysRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSysRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SysRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysRoleUpdatableFieldsEquals(partialUpdatedSysRole, getPersistedSysRole(partialUpdatedSysRole));
    }

    @Test
    void patchNonExistingSysRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysRole.setId(longCount.incrementAndGet());

        // Create the SysRole
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sysRoleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSysRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysRole.setId(longCount.incrementAndGet());

        // Create the SysRole
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SysRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSysRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysRole.setId(longCount.incrementAndGet());

        // Create the SysRole
        SysRoleDTO sysRoleDTO = sysRoleMapper.toDto(sysRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sysRoleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SysRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSysRole() {
        // Initialize the database
        sysRoleRepository.save(sysRole).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sysRole
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sysRole.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sysRoleRepository.count().block();
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

    protected SysRole getPersistedSysRole(SysRole sysRole) {
        return sysRoleRepository.findById(sysRole.getId()).block();
    }

    protected void assertPersistedSysRoleToMatchAllProperties(SysRole expectedSysRole) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSysRoleAllPropertiesEquals(expectedSysRole, getPersistedSysRole(expectedSysRole));
        assertSysRoleUpdatableFieldsEquals(expectedSysRole, getPersistedSysRole(expectedSysRole));
    }

    protected void assertPersistedSysRoleToMatchUpdatableProperties(SysRole expectedSysRole) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSysRoleAllUpdatablePropertiesEquals(expectedSysRole, getPersistedSysRole(expectedSysRole));
        assertSysRoleUpdatableFieldsEquals(expectedSysRole, getPersistedSysRole(expectedSysRole));
    }
}
