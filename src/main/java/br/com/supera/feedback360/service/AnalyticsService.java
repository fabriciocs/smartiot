package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.AnalyticsDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.Analytics}.
 */
public interface AnalyticsService {
    /**
     * Save a analytics.
     *
     * @param analyticsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AnalyticsDTO> save(AnalyticsDTO analyticsDTO);

    /**
     * Updates a analytics.
     *
     * @param analyticsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AnalyticsDTO> update(AnalyticsDTO analyticsDTO);

    /**
     * Partially updates a analytics.
     *
     * @param analyticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AnalyticsDTO> partialUpdate(AnalyticsDTO analyticsDTO);

    /**
     * Get all the analytics.
     *
     * @return the list of entities.
     */
    Flux<AnalyticsDTO> findAll();

    /**
     * Returns the number of analytics available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" analytics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AnalyticsDTO> findOne(Long id);

    /**
     * Delete the "id" analytics.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
