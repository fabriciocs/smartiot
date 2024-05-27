package br.com.supera.feedback360.service;

import br.com.supera.feedback360.repository.SensorRepository;
import br.com.supera.feedback360.service.dto.SensorDTO;
import br.com.supera.feedback360.service.mapper.SensorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.Sensor}.
 */
@Service
@Transactional
public class SensorService {

    private final Logger log = LoggerFactory.getLogger(SensorService.class);

    private final SensorRepository sensorRepository;

    private final SensorMapper sensorMapper;

    public SensorService(SensorRepository sensorRepository, SensorMapper sensorMapper) {
        this.sensorRepository = sensorRepository;
        this.sensorMapper = sensorMapper;
    }

    /**
     * Save a sensor.
     *
     * @param sensorDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SensorDTO> save(SensorDTO sensorDTO) {
        log.debug("Request to save Sensor : {}", sensorDTO);
        return sensorRepository.save(sensorMapper.toEntity(sensorDTO)).map(sensorMapper::toDto);
    }

    /**
     * Update a sensor.
     *
     * @param sensorDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SensorDTO> update(SensorDTO sensorDTO) {
        log.debug("Request to update Sensor : {}", sensorDTO);
        return sensorRepository.save(sensorMapper.toEntity(sensorDTO)).map(sensorMapper::toDto);
    }

    /**
     * Partially update a sensor.
     *
     * @param sensorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SensorDTO> partialUpdate(SensorDTO sensorDTO) {
        log.debug("Request to partially update Sensor : {}", sensorDTO);

        return sensorRepository
            .findById(sensorDTO.getId())
            .map(existingSensor -> {
                sensorMapper.partialUpdate(existingSensor, sensorDTO);

                return existingSensor;
            })
            .flatMap(sensorRepository::save)
            .map(sensorMapper::toDto);
    }

    /**
     * Get all the sensors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SensorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAllBy(pageable).map(sensorMapper::toDto);
    }

    /**
     * Get all the sensors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<SensorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sensorRepository.findAllWithEagerRelationships(pageable).map(sensorMapper::toDto);
    }

    /**
     * Returns the number of sensors available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return sensorRepository.count();
    }

    /**
     * Get one sensor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SensorDTO> findOne(Long id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findOneWithEagerRelationships(id).map(sensorMapper::toDto);
    }

    /**
     * Delete the sensor by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Sensor : {}", id);
        return sensorRepository.deleteById(id);
    }
}
