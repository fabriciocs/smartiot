package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.AuditLogDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.AuditLog}.
 */
public interface AuditLogService {
    /**
     * Save a auditLog.
     *
     * @param auditLogDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AuditLogDTO> save(AuditLogDTO auditLogDTO);

    /**
     * Updates a auditLog.
     *
     * @param auditLogDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AuditLogDTO> update(AuditLogDTO auditLogDTO);

    /**
     * Partially updates a auditLog.
     *
     * @param auditLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AuditLogDTO> partialUpdate(AuditLogDTO auditLogDTO);

    /**
     * Get all the auditLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AuditLogDTO> findAll(Pageable pageable);

    /**
     * Get all the auditLogs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AuditLogDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of auditLogs available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" auditLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AuditLogDTO> findOne(Long id);

    /**
     * Delete the "id" auditLog.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
