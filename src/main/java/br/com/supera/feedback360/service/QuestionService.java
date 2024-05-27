package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.QuestionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.Question}.
 */
public interface QuestionService {
    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<QuestionDTO> save(QuestionDTO questionDTO);

    /**
     * Updates a question.
     *
     * @param questionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<QuestionDTO> update(QuestionDTO questionDTO);

    /**
     * Partially updates a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<QuestionDTO> partialUpdate(QuestionDTO questionDTO);

    /**
     * Get all the questions.
     *
     * @return the list of entities.
     */
    Flux<QuestionDTO> findAll();

    /**
     * Get all the questions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<QuestionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of questions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<QuestionDTO> findOne(Long id);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
