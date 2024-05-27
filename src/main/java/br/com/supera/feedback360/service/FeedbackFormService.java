package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.FeedbackFormDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.FeedbackForm}.
 */
public interface FeedbackFormService {
    /**
     * Save a feedbackForm.
     *
     * @param feedbackFormDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FeedbackFormDTO> save(FeedbackFormDTO feedbackFormDTO);

    /**
     * Updates a feedbackForm.
     *
     * @param feedbackFormDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FeedbackFormDTO> update(FeedbackFormDTO feedbackFormDTO);

    /**
     * Partially updates a feedbackForm.
     *
     * @param feedbackFormDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FeedbackFormDTO> partialUpdate(FeedbackFormDTO feedbackFormDTO);

    /**
     * Get all the feedbackForms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FeedbackFormDTO> findAll(Pageable pageable);

    /**
     * Get all the feedbackForms with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FeedbackFormDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of feedbackForms available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" feedbackForm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FeedbackFormDTO> findOne(Long id);

    /**
     * Delete the "id" feedbackForm.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
