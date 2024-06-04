package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ResourceGroupAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.ResourceGroup;
import br.com.supera.smartiot.repository.ResourceGroupRepository;
import br.com.supera.smartiot.service.dto.ResourceGroupDTO;
import br.com.supera.smartiot.service.mapper.ResourceGroupMapper;
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
 * Integration tests for the {@link ResourceGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResourceGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/resource-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceGroupRepository resourceGroupRepository;

    @Autowired
    private ResourceGroupMapper resourceGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceGroupMockMvc;

    private ResourceGroup resourceGroup;

    private ResourceGroup insertedResourceGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceGroup createEntity(EntityManager em) {
        ResourceGroup resourceGroup = new ResourceGroup().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return resourceGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceGroup createUpdatedEntity(EntityManager em) {
        ResourceGroup resourceGroup = new ResourceGroup().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return resourceGroup;
    }

    @BeforeEach
    public void initTest() {
        resourceGroup = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedResourceGroup != null) {
            resourceGroupRepository.delete(insertedResourceGroup);
            insertedResourceGroup = null;
        }
    }

    @Test
    @Transactional
    void createResourceGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ResourceGroup
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);
        var returnedResourceGroupDTO = om.readValue(
            restResourceGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceGroupDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResourceGroupDTO.class
        );

        // Validate the ResourceGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResourceGroup = resourceGroupMapper.toEntity(returnedResourceGroupDTO);
        assertResourceGroupUpdatableFieldsEquals(returnedResourceGroup, getPersistedResourceGroup(returnedResourceGroup));

        insertedResourceGroup = returnedResourceGroup;
    }

    @Test
    @Transactional
    void createResourceGroupWithExistingId() throws Exception {
        // Create the ResourceGroup with an existing ID
        resourceGroup.setId(1L);
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resourceGroup.setName(null);

        // Create the ResourceGroup, which fails.
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        restResourceGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resourceGroup.setDescription(null);

        // Create the ResourceGroup, which fails.
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        restResourceGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResourceGroups() throws Exception {
        // Initialize the database
        insertedResourceGroup = resourceGroupRepository.saveAndFlush(resourceGroup);

        // Get all the resourceGroupList
        restResourceGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getResourceGroup() throws Exception {
        // Initialize the database
        insertedResourceGroup = resourceGroupRepository.saveAndFlush(resourceGroup);

        // Get the resourceGroup
        restResourceGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, resourceGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourceGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingResourceGroup() throws Exception {
        // Get the resourceGroup
        restResourceGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResourceGroup() throws Exception {
        // Initialize the database
        insertedResourceGroup = resourceGroupRepository.saveAndFlush(resourceGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceGroup
        ResourceGroup updatedResourceGroup = resourceGroupRepository.findById(resourceGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResourceGroup are not directly saved in db
        em.detach(updatedResourceGroup);
        updatedResourceGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(updatedResourceGroup);

        restResourceGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResourceGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResourceGroupToMatchAllProperties(updatedResourceGroup);
    }

    @Test
    @Transactional
    void putNonExistingResourceGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceGroup.setId(longCount.incrementAndGet());

        // Create the ResourceGroup
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResourceGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceGroup.setId(longCount.incrementAndGet());

        // Create the ResourceGroup
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResourceGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceGroup.setId(longCount.incrementAndGet());

        // Create the ResourceGroup
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResourceGroupWithPatch() throws Exception {
        // Initialize the database
        insertedResourceGroup = resourceGroupRepository.saveAndFlush(resourceGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceGroup using partial update
        ResourceGroup partialUpdatedResourceGroup = new ResourceGroup();
        partialUpdatedResourceGroup.setId(resourceGroup.getId());

        partialUpdatedResourceGroup.description(UPDATED_DESCRIPTION);

        restResourceGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResourceGroup))
            )
            .andExpect(status().isOk());

        // Validate the ResourceGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResourceGroup, resourceGroup),
            getPersistedResourceGroup(resourceGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateResourceGroupWithPatch() throws Exception {
        // Initialize the database
        insertedResourceGroup = resourceGroupRepository.saveAndFlush(resourceGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceGroup using partial update
        ResourceGroup partialUpdatedResourceGroup = new ResourceGroup();
        partialUpdatedResourceGroup.setId(resourceGroup.getId());

        partialUpdatedResourceGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restResourceGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResourceGroup))
            )
            .andExpect(status().isOk());

        // Validate the ResourceGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceGroupUpdatableFieldsEquals(partialUpdatedResourceGroup, getPersistedResourceGroup(partialUpdatedResourceGroup));
    }

    @Test
    @Transactional
    void patchNonExistingResourceGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceGroup.setId(longCount.incrementAndGet());

        // Create the ResourceGroup
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourceGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResourceGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceGroup.setId(longCount.incrementAndGet());

        // Create the ResourceGroup
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResourceGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceGroup.setId(longCount.incrementAndGet());

        // Create the ResourceGroup
        ResourceGroupDTO resourceGroupDTO = resourceGroupMapper.toDto(resourceGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resourceGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResourceGroup() throws Exception {
        // Initialize the database
        insertedResourceGroup = resourceGroupRepository.saveAndFlush(resourceGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resourceGroup
        restResourceGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, resourceGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resourceGroupRepository.count();
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

    protected ResourceGroup getPersistedResourceGroup(ResourceGroup resourceGroup) {
        return resourceGroupRepository.findById(resourceGroup.getId()).orElseThrow();
    }

    protected void assertPersistedResourceGroupToMatchAllProperties(ResourceGroup expectedResourceGroup) {
        assertResourceGroupAllPropertiesEquals(expectedResourceGroup, getPersistedResourceGroup(expectedResourceGroup));
    }

    protected void assertPersistedResourceGroupToMatchUpdatableProperties(ResourceGroup expectedResourceGroup) {
        assertResourceGroupAllUpdatablePropertiesEquals(expectedResourceGroup, getPersistedResourceGroup(expectedResourceGroup));
    }
}
