package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.SysUserDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.SysUser}.
 */
public interface SysUserService {
    /**
     * Save a sysUser.
     *
     * @param sysUserDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SysUserDTO> save(SysUserDTO sysUserDTO);

    /**
     * Updates a sysUser.
     *
     * @param sysUserDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SysUserDTO> update(SysUserDTO sysUserDTO);

    /**
     * Partially updates a sysUser.
     *
     * @param sysUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SysUserDTO> partialUpdate(SysUserDTO sysUserDTO);

    /**
     * Get all the sysUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SysUserDTO> findAll(Pageable pageable);

    /**
     * Get all the sysUsers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SysUserDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of sysUsers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" sysUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SysUserDTO> findOne(Long id);

    /**
     * Delete the "id" sysUser.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
