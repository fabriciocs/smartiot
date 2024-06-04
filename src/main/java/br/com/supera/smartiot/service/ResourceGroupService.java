package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.ResourceGroup;
import br.com.supera.smartiot.repository.ResourceGroupRepository;
import br.com.supera.smartiot.service.dto.ResourceGroupDTO;
import br.com.supera.smartiot.service.mapper.ResourceGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.ResourceGroup}.
 */
@Service
@Transactional
public class ResourceGroupService {

    private final Logger log = LoggerFactory.getLogger(ResourceGroupService.class);

    private final ResourceGroupRepository resourceGroupRepository;

    private final ResourceGroupMapper resourceGroupMapper;

    public ResourceGroupService(ResourceGroupRepository resourceGroupRepository, ResourceGroupMapper resourceGroupMapper) {
        this.resourceGroupRepository = resourceGroupRepository;
        this.resourceGroupMapper = resourceGroupMapper;
    }

    /**
     * Save a resourceGroup.
     *
     * @param resourceGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceGroupDTO save(ResourceGroupDTO resourceGroupDTO) {
        log.debug("Request to save ResourceGroup : {}", resourceGroupDTO);
        ResourceGroup resourceGroup = resourceGroupMapper.toEntity(resourceGroupDTO);
        resourceGroup = resourceGroupRepository.save(resourceGroup);
        return resourceGroupMapper.toDto(resourceGroup);
    }

    /**
     * Update a resourceGroup.
     *
     * @param resourceGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceGroupDTO update(ResourceGroupDTO resourceGroupDTO) {
        log.debug("Request to update ResourceGroup : {}", resourceGroupDTO);
        ResourceGroup resourceGroup = resourceGroupMapper.toEntity(resourceGroupDTO);
        resourceGroup = resourceGroupRepository.save(resourceGroup);
        return resourceGroupMapper.toDto(resourceGroup);
    }

    /**
     * Partially update a resourceGroup.
     *
     * @param resourceGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResourceGroupDTO> partialUpdate(ResourceGroupDTO resourceGroupDTO) {
        log.debug("Request to partially update ResourceGroup : {}", resourceGroupDTO);

        return resourceGroupRepository
            .findById(resourceGroupDTO.getId())
            .map(existingResourceGroup -> {
                resourceGroupMapper.partialUpdate(existingResourceGroup, resourceGroupDTO);

                return existingResourceGroup;
            })
            .map(resourceGroupRepository::save)
            .map(resourceGroupMapper::toDto);
    }

    /**
     * Get all the resourceGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ResourceGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResourceGroups");
        return resourceGroupRepository.findAll(pageable).map(resourceGroupMapper::toDto);
    }

    /**
     * Get one resourceGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResourceGroupDTO> findOne(Long id) {
        log.debug("Request to get ResourceGroup : {}", id);
        return resourceGroupRepository.findById(id).map(resourceGroupMapper::toDto);
    }

    /**
     * Delete the resourceGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ResourceGroup : {}", id);
        resourceGroupRepository.deleteById(id);
    }
}
