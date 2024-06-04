package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.Measurement;
import br.com.supera.smartiot.repository.MeasurementRepository;
import br.com.supera.smartiot.service.dto.MeasurementDTO;
import br.com.supera.smartiot.service.mapper.MeasurementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.Measurement}.
 */
@Service
@Transactional
public class MeasurementService {

    private final Logger log = LoggerFactory.getLogger(MeasurementService.class);

    private final MeasurementRepository measurementRepository;

    private final MeasurementMapper measurementMapper;

    public MeasurementService(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
    }

    /**
     * Save a measurement.
     *
     * @param measurementDTO the entity to save.
     * @return the persisted entity.
     */
    public MeasurementDTO save(MeasurementDTO measurementDTO) {
        log.debug("Request to save Measurement : {}", measurementDTO);
        Measurement measurement = measurementMapper.toEntity(measurementDTO);
        measurement = measurementRepository.save(measurement);
        return measurementMapper.toDto(measurement);
    }

    /**
     * Update a measurement.
     *
     * @param measurementDTO the entity to save.
     * @return the persisted entity.
     */
    public MeasurementDTO update(MeasurementDTO measurementDTO) {
        log.debug("Request to update Measurement : {}", measurementDTO);
        Measurement measurement = measurementMapper.toEntity(measurementDTO);
        measurement = measurementRepository.save(measurement);
        return measurementMapper.toDto(measurement);
    }

    /**
     * Partially update a measurement.
     *
     * @param measurementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MeasurementDTO> partialUpdate(MeasurementDTO measurementDTO) {
        log.debug("Request to partially update Measurement : {}", measurementDTO);

        return measurementRepository
            .findById(measurementDTO.getId())
            .map(existingMeasurement -> {
                measurementMapper.partialUpdate(existingMeasurement, measurementDTO);

                return existingMeasurement;
            })
            .map(measurementRepository::save)
            .map(measurementMapper::toDto);
    }

    /**
     * Get all the measurements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MeasurementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Measurements");
        return measurementRepository.findAll(pageable).map(measurementMapper::toDto);
    }

    /**
     * Get one measurement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MeasurementDTO> findOne(Long id) {
        log.debug("Request to get Measurement : {}", id);
        return measurementRepository.findById(id).map(measurementMapper::toDto);
    }

    /**
     * Delete the measurement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Measurement : {}", id);
        measurementRepository.deleteById(id);
    }
}
