package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.Sensor;
import br.com.supera.smartiot.repository.SensorRepository;
import br.com.supera.smartiot.service.dto.SensorDTO;
import br.com.supera.smartiot.service.mapper.SensorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.Sensor}.
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
    public SensorDTO save(SensorDTO sensorDTO) {
        log.debug("Request to save Sensor : {}", sensorDTO);
        Sensor sensor = sensorMapper.toEntity(sensorDTO);
        sensor = sensorRepository.save(sensor);
        return sensorMapper.toDto(sensor);
    }

    /**
     * Update a sensor.
     *
     * @param sensorDTO the entity to save.
     * @return the persisted entity.
     */
    public SensorDTO update(SensorDTO sensorDTO) {
        log.debug("Request to update Sensor : {}", sensorDTO);
        Sensor sensor = sensorMapper.toEntity(sensorDTO);
        sensor = sensorRepository.save(sensor);
        return sensorMapper.toDto(sensor);
    }

    /**
     * Partially update a sensor.
     *
     * @param sensorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SensorDTO> partialUpdate(SensorDTO sensorDTO) {
        log.debug("Request to partially update Sensor : {}", sensorDTO);

        return sensorRepository
            .findById(sensorDTO.getId())
            .map(existingSensor -> {
                sensorMapper.partialUpdate(existingSensor, sensorDTO);

                return existingSensor;
            })
            .map(sensorRepository::save)
            .map(sensorMapper::toDto);
    }

    /**
     * Get all the sensors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SensorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAll(pageable).map(sensorMapper::toDto);
    }

    /**
     * Get all the sensors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SensorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sensorRepository.findAllWithEagerRelationships(pageable).map(sensorMapper::toDto);
    }

    /**
     * Get one sensor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SensorDTO> findOne(Long id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findOneWithEagerRelationships(id).map(sensorMapper::toDto);
    }

    /**
     * Delete the sensor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sensor : {}", id);
        sensorRepository.deleteById(id);
    }
}
