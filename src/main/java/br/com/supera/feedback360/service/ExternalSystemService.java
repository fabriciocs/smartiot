package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.ExternalSystemDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.ExternalSystem}.
 */
public interface ExternalSystemService {
    /**
     * Save a externalSystem.
     *
     * @param externalSystemDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ExternalSystemDTO> save(ExternalSystemDTO externalSystemDTO);

    /**
     * Updates a externalSystem.
     *
     * @param externalSystemDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ExternalSystemDTO> update(ExternalSystemDTO externalSystemDTO);

    /**
     * Partially updates a externalSystem.
     *
     * @param externalSystemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ExternalSystemDTO> partialUpdate(ExternalSystemDTO externalSystemDTO);

    /**
     * Get all the externalSystems.
     *
     * @return the list of entities.
     */
    Flux<ExternalSystemDTO> findAll();

    /**
     * Returns the number of externalSystems available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" externalSystem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ExternalSystemDTO> findOne(Long id);

    /**
     * Delete the "id" externalSystem.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
