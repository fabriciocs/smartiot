package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.IntegrationConfigDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.IntegrationConfig}.
 */
public interface IntegrationConfigService {
    /**
     * Save a integrationConfig.
     *
     * @param integrationConfigDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<IntegrationConfigDTO> save(IntegrationConfigDTO integrationConfigDTO);

    /**
     * Updates a integrationConfig.
     *
     * @param integrationConfigDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<IntegrationConfigDTO> update(IntegrationConfigDTO integrationConfigDTO);

    /**
     * Partially updates a integrationConfig.
     *
     * @param integrationConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<IntegrationConfigDTO> partialUpdate(IntegrationConfigDTO integrationConfigDTO);

    /**
     * Get all the integrationConfigs.
     *
     * @return the list of entities.
     */
    Flux<IntegrationConfigDTO> findAll();

    /**
     * Get all the integrationConfigs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<IntegrationConfigDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of integrationConfigs available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" integrationConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<IntegrationConfigDTO> findOne(Long id);

    /**
     * Delete the "id" integrationConfig.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
