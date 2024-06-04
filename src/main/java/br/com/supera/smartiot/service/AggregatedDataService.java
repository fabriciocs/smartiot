package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.AggregatedData;
import br.com.supera.smartiot.repository.AggregatedDataRepository;
import br.com.supera.smartiot.service.dto.AggregatedDataDTO;
import br.com.supera.smartiot.service.mapper.AggregatedDataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.AggregatedData}.
 */
@Service
@Transactional
public class AggregatedDataService {

    private final Logger log = LoggerFactory.getLogger(AggregatedDataService.class);

    private final AggregatedDataRepository aggregatedDataRepository;

    private final AggregatedDataMapper aggregatedDataMapper;

    public AggregatedDataService(AggregatedDataRepository aggregatedDataRepository, AggregatedDataMapper aggregatedDataMapper) {
        this.aggregatedDataRepository = aggregatedDataRepository;
        this.aggregatedDataMapper = aggregatedDataMapper;
    }

    /**
     * Save a aggregatedData.
     *
     * @param aggregatedDataDTO the entity to save.
     * @return the persisted entity.
     */
    public AggregatedDataDTO save(AggregatedDataDTO aggregatedDataDTO) {
        log.debug("Request to save AggregatedData : {}", aggregatedDataDTO);
        AggregatedData aggregatedData = aggregatedDataMapper.toEntity(aggregatedDataDTO);
        aggregatedData = aggregatedDataRepository.save(aggregatedData);
        return aggregatedDataMapper.toDto(aggregatedData);
    }

    /**
     * Update a aggregatedData.
     *
     * @param aggregatedDataDTO the entity to save.
     * @return the persisted entity.
     */
    public AggregatedDataDTO update(AggregatedDataDTO aggregatedDataDTO) {
        log.debug("Request to update AggregatedData : {}", aggregatedDataDTO);
        AggregatedData aggregatedData = aggregatedDataMapper.toEntity(aggregatedDataDTO);
        aggregatedData = aggregatedDataRepository.save(aggregatedData);
        return aggregatedDataMapper.toDto(aggregatedData);
    }

    /**
     * Partially update a aggregatedData.
     *
     * @param aggregatedDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AggregatedDataDTO> partialUpdate(AggregatedDataDTO aggregatedDataDTO) {
        log.debug("Request to partially update AggregatedData : {}", aggregatedDataDTO);

        return aggregatedDataRepository
            .findById(aggregatedDataDTO.getId())
            .map(existingAggregatedData -> {
                aggregatedDataMapper.partialUpdate(existingAggregatedData, aggregatedDataDTO);

                return existingAggregatedData;
            })
            .map(aggregatedDataRepository::save)
            .map(aggregatedDataMapper::toDto);
    }

    /**
     * Get all the aggregatedData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AggregatedDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AggregatedData");
        return aggregatedDataRepository.findAll(pageable).map(aggregatedDataMapper::toDto);
    }

    /**
     * Get one aggregatedData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AggregatedDataDTO> findOne(Long id) {
        log.debug("Request to get AggregatedData : {}", id);
        return aggregatedDataRepository.findById(id).map(aggregatedDataMapper::toDto);
    }

    /**
     * Delete the aggregatedData by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AggregatedData : {}", id);
        aggregatedDataRepository.deleteById(id);
    }
}
