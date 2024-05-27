package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.CompliancePolicyRepository;
import br.com.supera.feedback360.service.CompliancePolicyService;
import br.com.supera.feedback360.service.dto.CompliancePolicyDTO;
import br.com.supera.feedback360.service.mapper.CompliancePolicyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.CompliancePolicy}.
 */
@Service
@Transactional
public class CompliancePolicyServiceImpl implements CompliancePolicyService {

    private final Logger log = LoggerFactory.getLogger(CompliancePolicyServiceImpl.class);

    private final CompliancePolicyRepository compliancePolicyRepository;

    private final CompliancePolicyMapper compliancePolicyMapper;

    public CompliancePolicyServiceImpl(
        CompliancePolicyRepository compliancePolicyRepository,
        CompliancePolicyMapper compliancePolicyMapper
    ) {
        this.compliancePolicyRepository = compliancePolicyRepository;
        this.compliancePolicyMapper = compliancePolicyMapper;
    }

    @Override
    public Mono<CompliancePolicyDTO> save(CompliancePolicyDTO compliancePolicyDTO) {
        log.debug("Request to save CompliancePolicy : {}", compliancePolicyDTO);
        return compliancePolicyRepository.save(compliancePolicyMapper.toEntity(compliancePolicyDTO)).map(compliancePolicyMapper::toDto);
    }

    @Override
    public Mono<CompliancePolicyDTO> update(CompliancePolicyDTO compliancePolicyDTO) {
        log.debug("Request to update CompliancePolicy : {}", compliancePolicyDTO);
        return compliancePolicyRepository.save(compliancePolicyMapper.toEntity(compliancePolicyDTO)).map(compliancePolicyMapper::toDto);
    }

    @Override
    public Mono<CompliancePolicyDTO> partialUpdate(CompliancePolicyDTO compliancePolicyDTO) {
        log.debug("Request to partially update CompliancePolicy : {}", compliancePolicyDTO);

        return compliancePolicyRepository
            .findById(compliancePolicyDTO.getId())
            .map(existingCompliancePolicy -> {
                compliancePolicyMapper.partialUpdate(existingCompliancePolicy, compliancePolicyDTO);

                return existingCompliancePolicy;
            })
            .flatMap(compliancePolicyRepository::save)
            .map(compliancePolicyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CompliancePolicyDTO> findAll() {
        log.debug("Request to get all CompliancePolicies");
        return compliancePolicyRepository.findAll().map(compliancePolicyMapper::toDto);
    }

    public Mono<Long> countAll() {
        return compliancePolicyRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CompliancePolicyDTO> findOne(Long id) {
        log.debug("Request to get CompliancePolicy : {}", id);
        return compliancePolicyRepository.findById(id).map(compliancePolicyMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CompliancePolicy : {}", id);
        return compliancePolicyRepository.deleteById(id);
    }
}
