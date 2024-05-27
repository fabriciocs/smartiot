package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.SysRoleDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.SysRole}.
 */
public interface SysRoleService {
    /**
     * Save a sysRole.
     *
     * @param sysRoleDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SysRoleDTO> save(SysRoleDTO sysRoleDTO);

    /**
     * Updates a sysRole.
     *
     * @param sysRoleDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SysRoleDTO> update(SysRoleDTO sysRoleDTO);

    /**
     * Partially updates a sysRole.
     *
     * @param sysRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SysRoleDTO> partialUpdate(SysRoleDTO sysRoleDTO);

    /**
     * Get all the sysRoles.
     *
     * @return the list of entities.
     */
    Flux<SysRoleDTO> findAll();

    /**
     * Returns the number of sysRoles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" sysRole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SysRoleDTO> findOne(Long id);

    /**
     * Delete the "id" sysRole.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
