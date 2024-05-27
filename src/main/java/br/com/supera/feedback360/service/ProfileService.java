package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.ProfileDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.Profile}.
 */
public interface ProfileService {
    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProfileDTO> save(ProfileDTO profileDTO);

    /**
     * Updates a profile.
     *
     * @param profileDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProfileDTO> update(ProfileDTO profileDTO);

    /**
     * Partially updates a profile.
     *
     * @param profileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProfileDTO> partialUpdate(ProfileDTO profileDTO);

    /**
     * Get all the profiles.
     *
     * @return the list of entities.
     */
    Flux<ProfileDTO> findAll();

    /**
     * Get all the profiles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProfileDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of profiles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" profile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProfileDTO> findOne(Long id);

    /**
     * Delete the "id" profile.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
