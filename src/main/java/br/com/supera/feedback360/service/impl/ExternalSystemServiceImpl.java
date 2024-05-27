package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.ExternalSystemRepository;
import br.com.supera.feedback360.service.ExternalSystemService;
import br.com.supera.feedback360.service.dto.ExternalSystemDTO;
import br.com.supera.feedback360.service.mapper.ExternalSystemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.ExternalSystem}.
 */
@Service
@Transactional
public class ExternalSystemServiceImpl implements ExternalSystemService {

    private final Logger log = LoggerFactory.getLogger(ExternalSystemServiceImpl.class);

    private final ExternalSystemRepository externalSystemRepository;

    private final ExternalSystemMapper externalSystemMapper;

    public ExternalSystemServiceImpl(ExternalSystemRepository externalSystemRepository, ExternalSystemMapper externalSystemMapper) {
        this.externalSystemRepository = externalSystemRepository;
        this.externalSystemMapper = externalSystemMapper;
    }

    @Override
    public Mono<ExternalSystemDTO> save(ExternalSystemDTO externalSystemDTO) {
        log.debug("Request to save ExternalSystem : {}", externalSystemDTO);
        return externalSystemRepository.save(externalSystemMapper.toEntity(externalSystemDTO)).map(externalSystemMapper::toDto);
    }

    @Override
    public Mono<ExternalSystemDTO> update(ExternalSystemDTO externalSystemDTO) {
        log.debug("Request to update ExternalSystem : {}", externalSystemDTO);
        return externalSystemRepository.save(externalSystemMapper.toEntity(externalSystemDTO)).map(externalSystemMapper::toDto);
    }

    @Override
    public Mono<ExternalSystemDTO> partialUpdate(ExternalSystemDTO externalSystemDTO) {
        log.debug("Request to partially update ExternalSystem : {}", externalSystemDTO);

        return externalSystemRepository
            .findById(externalSystemDTO.getId())
            .map(existingExternalSystem -> {
                externalSystemMapper.partialUpdate(existingExternalSystem, externalSystemDTO);

                return existingExternalSystem;
            })
            .flatMap(externalSystemRepository::save)
            .map(externalSystemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ExternalSystemDTO> findAll() {
        log.debug("Request to get all ExternalSystems");
        return externalSystemRepository.findAll().map(externalSystemMapper::toDto);
    }

    public Mono<Long> countAll() {
        return externalSystemRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ExternalSystemDTO> findOne(Long id) {
        log.debug("Request to get ExternalSystem : {}", id);
        return externalSystemRepository.findById(id).map(externalSystemMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ExternalSystem : {}", id);
        return externalSystemRepository.deleteById(id);
    }
}
