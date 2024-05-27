package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.AnalyticsRepository;
import br.com.supera.feedback360.service.AnalyticsService;
import br.com.supera.feedback360.service.dto.AnalyticsDTO;
import br.com.supera.feedback360.service.mapper.AnalyticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.Analytics}.
 */
@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    private final AnalyticsRepository analyticsRepository;

    private final AnalyticsMapper analyticsMapper;

    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository, AnalyticsMapper analyticsMapper) {
        this.analyticsRepository = analyticsRepository;
        this.analyticsMapper = analyticsMapper;
    }

    @Override
    public Mono<AnalyticsDTO> save(AnalyticsDTO analyticsDTO) {
        log.debug("Request to save Analytics : {}", analyticsDTO);
        return analyticsRepository.save(analyticsMapper.toEntity(analyticsDTO)).map(analyticsMapper::toDto);
    }

    @Override
    public Mono<AnalyticsDTO> update(AnalyticsDTO analyticsDTO) {
        log.debug("Request to update Analytics : {}", analyticsDTO);
        return analyticsRepository.save(analyticsMapper.toEntity(analyticsDTO)).map(analyticsMapper::toDto);
    }

    @Override
    public Mono<AnalyticsDTO> partialUpdate(AnalyticsDTO analyticsDTO) {
        log.debug("Request to partially update Analytics : {}", analyticsDTO);

        return analyticsRepository
            .findById(analyticsDTO.getId())
            .map(existingAnalytics -> {
                analyticsMapper.partialUpdate(existingAnalytics, analyticsDTO);

                return existingAnalytics;
            })
            .flatMap(analyticsRepository::save)
            .map(analyticsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AnalyticsDTO> findAll() {
        log.debug("Request to get all Analytics");
        return analyticsRepository.findAll().map(analyticsMapper::toDto);
    }

    public Mono<Long> countAll() {
        return analyticsRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AnalyticsDTO> findOne(Long id) {
        log.debug("Request to get Analytics : {}", id);
        return analyticsRepository.findById(id).map(analyticsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Analytics : {}", id);
        return analyticsRepository.deleteById(id);
    }
}
