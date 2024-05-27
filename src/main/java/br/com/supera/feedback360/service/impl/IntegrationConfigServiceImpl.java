package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.IntegrationConfigRepository;
import br.com.supera.feedback360.service.IntegrationConfigService;
import br.com.supera.feedback360.service.dto.IntegrationConfigDTO;
import br.com.supera.feedback360.service.mapper.IntegrationConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.IntegrationConfig}.
 */
@Service
@Transactional
public class IntegrationConfigServiceImpl implements IntegrationConfigService {

    private final Logger log = LoggerFactory.getLogger(IntegrationConfigServiceImpl.class);

    private final IntegrationConfigRepository integrationConfigRepository;

    private final IntegrationConfigMapper integrationConfigMapper;

    public IntegrationConfigServiceImpl(
        IntegrationConfigRepository integrationConfigRepository,
        IntegrationConfigMapper integrationConfigMapper
    ) {
        this.integrationConfigRepository = integrationConfigRepository;
        this.integrationConfigMapper = integrationConfigMapper;
    }

    @Override
    public Mono<IntegrationConfigDTO> save(IntegrationConfigDTO integrationConfigDTO) {
        log.debug("Request to save IntegrationConfig : {}", integrationConfigDTO);
        return integrationConfigRepository.save(integrationConfigMapper.toEntity(integrationConfigDTO)).map(integrationConfigMapper::toDto);
    }

    @Override
    public Mono<IntegrationConfigDTO> update(IntegrationConfigDTO integrationConfigDTO) {
        log.debug("Request to update IntegrationConfig : {}", integrationConfigDTO);
        return integrationConfigRepository.save(integrationConfigMapper.toEntity(integrationConfigDTO)).map(integrationConfigMapper::toDto);
    }

    @Override
    public Mono<IntegrationConfigDTO> partialUpdate(IntegrationConfigDTO integrationConfigDTO) {
        log.debug("Request to partially update IntegrationConfig : {}", integrationConfigDTO);

        return integrationConfigRepository
            .findById(integrationConfigDTO.getId())
            .map(existingIntegrationConfig -> {
                integrationConfigMapper.partialUpdate(existingIntegrationConfig, integrationConfigDTO);

                return existingIntegrationConfig;
            })
            .flatMap(integrationConfigRepository::save)
            .map(integrationConfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IntegrationConfigDTO> findAll() {
        log.debug("Request to get all IntegrationConfigs");
        return integrationConfigRepository.findAll().map(integrationConfigMapper::toDto);
    }

    public Flux<IntegrationConfigDTO> findAllWithEagerRelationships(Pageable pageable) {
        return integrationConfigRepository.findAllWithEagerRelationships(pageable).map(integrationConfigMapper::toDto);
    }

    public Mono<Long> countAll() {
        return integrationConfigRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<IntegrationConfigDTO> findOne(Long id) {
        log.debug("Request to get IntegrationConfig : {}", id);
        return integrationConfigRepository.findOneWithEagerRelationships(id).map(integrationConfigMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IntegrationConfig : {}", id);
        return integrationConfigRepository.deleteById(id);
    }
}
