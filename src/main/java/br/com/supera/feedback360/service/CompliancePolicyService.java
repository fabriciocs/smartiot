package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.CompliancePolicyDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.CompliancePolicy}.
 */
public interface CompliancePolicyService {
    /**
     * Save a compliancePolicy.
     *
     * @param compliancePolicyDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CompliancePolicyDTO> save(CompliancePolicyDTO compliancePolicyDTO);

    /**
     * Updates a compliancePolicy.
     *
     * @param compliancePolicyDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CompliancePolicyDTO> update(CompliancePolicyDTO compliancePolicyDTO);

    /**
     * Partially updates a compliancePolicy.
     *
     * @param compliancePolicyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CompliancePolicyDTO> partialUpdate(CompliancePolicyDTO compliancePolicyDTO);

    /**
     * Get all the compliancePolicies.
     *
     * @return the list of entities.
     */
    Flux<CompliancePolicyDTO> findAll();

    /**
     * Returns the number of compliancePolicies available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" compliancePolicy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CompliancePolicyDTO> findOne(Long id);

    /**
     * Delete the "id" compliancePolicy.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
