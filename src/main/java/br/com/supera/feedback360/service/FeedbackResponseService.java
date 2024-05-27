package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.FeedbackResponseDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.FeedbackResponse}.
 */
public interface FeedbackResponseService {
    /**
     * Save a feedbackResponse.
     *
     * @param feedbackResponseDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FeedbackResponseDTO> save(FeedbackResponseDTO feedbackResponseDTO);

    /**
     * Updates a feedbackResponse.
     *
     * @param feedbackResponseDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FeedbackResponseDTO> update(FeedbackResponseDTO feedbackResponseDTO);

    /**
     * Partially updates a feedbackResponse.
     *
     * @param feedbackResponseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FeedbackResponseDTO> partialUpdate(FeedbackResponseDTO feedbackResponseDTO);

    /**
     * Get all the feedbackResponses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FeedbackResponseDTO> findAll(Pageable pageable);

    /**
     * Get all the feedbackResponses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FeedbackResponseDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of feedbackResponses available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" feedbackResponse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FeedbackResponseDTO> findOne(Long id);

    /**
     * Delete the "id" feedbackResponse.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
