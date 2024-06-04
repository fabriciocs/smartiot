package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.SysUserAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.SysUser;
import br.com.supera.smartiot.repository.SysUserRepository;
import br.com.supera.smartiot.service.dto.SysUserDTO;
import br.com.supera.smartiot.service.mapper.SysUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SysUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SysUserResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sys-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSysUserMockMvc;

    private SysUser sysUser;

    private SysUser insertedSysUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysUser createEntity(EntityManager em) {
        SysUser sysUser = new SysUser().username(DEFAULT_USERNAME).email(DEFAULT_EMAIL).role(DEFAULT_ROLE);
        return sysUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysUser createUpdatedEntity(EntityManager em) {
        SysUser sysUser = new SysUser().username(UPDATED_USERNAME).email(UPDATED_EMAIL).role(UPDATED_ROLE);
        return sysUser;
    }

    @BeforeEach
    public void initTest() {
        sysUser = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSysUser != null) {
            sysUserRepository.delete(insertedSysUser);
            insertedSysUser = null;
        }
    }

    @Test
    @Transactional
    void createSysUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);
        var returnedSysUserDTO = om.readValue(
            restSysUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SysUserDTO.class
        );

        // Validate the SysUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSysUser = sysUserMapper.toEntity(returnedSysUserDTO);
        assertSysUserUpdatableFieldsEquals(returnedSysUser, getPersistedSysUser(returnedSysUser));

        insertedSysUser = returnedSysUser;
    }

    @Test
    @Transactional
    void createSysUserWithExistingId() throws Exception {
        // Create the SysUser with an existing ID
        sysUser.setId(1L);
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sysUser.setUsername(null);

        // Create the SysUser, which fails.
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        restSysUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sysUser.setEmail(null);

        // Create the SysUser, which fails.
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        restSysUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sysUser.setRole(null);

        // Create the SysUser, which fails.
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        restSysUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSysUsers() throws Exception {
        // Initialize the database
        insertedSysUser = sysUserRepository.saveAndFlush(sysUser);

        // Get all the sysUserList
        restSysUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }

    @Test
    @Transactional
    void getSysUser() throws Exception {
        // Initialize the database
        insertedSysUser = sysUserRepository.saveAndFlush(sysUser);

        // Get the sysUser
        restSysUserMockMvc
            .perform(get(ENTITY_API_URL_ID, sysUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }

    @Test
    @Transactional
    void getNonExistingSysUser() throws Exception {
        // Get the sysUser
        restSysUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSysUser() throws Exception {
        // Initialize the database
        insertedSysUser = sysUserRepository.saveAndFlush(sysUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysUser
        SysUser updatedSysUser = sysUserRepository.findById(sysUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSysUser are not directly saved in db
        em.detach(updatedSysUser);
        updatedSysUser.username(UPDATED_USERNAME).email(UPDATED_EMAIL).role(UPDATED_ROLE);
        SysUserDTO sysUserDTO = sysUserMapper.toDto(updatedSysUser);

        restSysUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysUserDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSysUserToMatchAllProperties(updatedSysUser);
    }

    @Test
    @Transactional
    void putNonExistingSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysUserDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sysUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysUserWithPatch() throws Exception {
        // Initialize the database
        insertedSysUser = sysUserRepository.saveAndFlush(sysUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysUser using partial update
        SysUser partialUpdatedSysUser = new SysUser();
        partialUpdatedSysUser.setId(sysUser.getId());

        restSysUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSysUser))
            )
            .andExpect(status().isOk());

        // Validate the SysUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSysUser, sysUser), getPersistedSysUser(sysUser));
    }

    @Test
    @Transactional
    void fullUpdateSysUserWithPatch() throws Exception {
        // Initialize the database
        insertedSysUser = sysUserRepository.saveAndFlush(sysUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysUser using partial update
        SysUser partialUpdatedSysUser = new SysUser();
        partialUpdatedSysUser.setId(sysUser.getId());

        partialUpdatedSysUser.username(UPDATED_USERNAME).email(UPDATED_EMAIL).role(UPDATED_ROLE);

        restSysUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSysUser))
            )
            .andExpect(status().isOk());

        // Validate the SysUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysUserUpdatableFieldsEquals(partialUpdatedSysUser, getPersistedSysUser(partialUpdatedSysUser));
    }

    @Test
    @Transactional
    void patchNonExistingSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sysUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sysUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysUser.setId(longCount.incrementAndGet());

        // Create the SysUser
        SysUserDTO sysUserDTO = sysUserMapper.toDto(sysUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sysUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysUser() throws Exception {
        // Initialize the database
        insertedSysUser = sysUserRepository.saveAndFlush(sysUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sysUser
        restSysUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sysUserRepository.count();
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
        return sysUserRepository.findById(sysUser.getId()).orElseThrow();
    }

    protected void assertPersistedSysUserToMatchAllProperties(SysUser expectedSysUser) {
        assertSysUserAllPropertiesEquals(expectedSysUser, getPersistedSysUser(expectedSysUser));
    }

    protected void assertPersistedSysUserToMatchUpdatableProperties(SysUser expectedSysUser) {
        assertSysUserAllUpdatablePropertiesEquals(expectedSysUser, getPersistedSysUser(expectedSysUser));
    }
}
