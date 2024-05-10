package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.DadoSensor;
import br.com.supera.smartiot.repository.DadoSensorRepository;
import br.com.supera.smartiot.service.dto.DadoSensorDTO;
import br.com.supera.smartiot.service.mapper.DadoSensorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.DadoSensor}.
 */
@Service
@Transactional
public class DadoSensorService {

    private final Logger log = LoggerFactory.getLogger(DadoSensorService.class);

    private final DadoSensorRepository dadoSensorRepository;

    private final DadoSensorMapper dadoSensorMapper;

    public DadoSensorService(DadoSensorRepository dadoSensorRepository, DadoSensorMapper dadoSensorMapper) {
        this.dadoSensorRepository = dadoSensorRepository;
        this.dadoSensorMapper = dadoSensorMapper;
    }

    /**
     * Save a dadoSensor.
     *
     * @param dadoSensorDTO the entity to save.
     * @return the persisted entity.
     */
    public DadoSensorDTO save(DadoSensorDTO dadoSensorDTO) {
        log.debug("Request to save DadoSensor : {}", dadoSensorDTO);
        DadoSensor dadoSensor = dadoSensorMapper.toEntity(dadoSensorDTO);
        dadoSensor = dadoSensorRepository.save(dadoSensor);
        return dadoSensorMapper.toDto(dadoSensor);
    }

    /**
     * Update a dadoSensor.
     *
     * @param dadoSensorDTO the entity to save.
     * @return the persisted entity.
     */
    public DadoSensorDTO update(DadoSensorDTO dadoSensorDTO) {
        log.debug("Request to update DadoSensor : {}", dadoSensorDTO);
        DadoSensor dadoSensor = dadoSensorMapper.toEntity(dadoSensorDTO);
        dadoSensor = dadoSensorRepository.save(dadoSensor);
        return dadoSensorMapper.toDto(dadoSensor);
    }

    /**
     * Partially update a dadoSensor.
     *
     * @param dadoSensorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DadoSensorDTO> partialUpdate(DadoSensorDTO dadoSensorDTO) {
        log.debug("Request to partially update DadoSensor : {}", dadoSensorDTO);

        return dadoSensorRepository
            .findById(dadoSensorDTO.getId())
            .map(existingDadoSensor -> {
                dadoSensorMapper.partialUpdate(existingDadoSensor, dadoSensorDTO);

                return existingDadoSensor;
            })
            .map(dadoSensorRepository::save)
            .map(dadoSensorMapper::toDto);
    }

    /**
     * Get all the dadoSensors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DadoSensorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DadoSensors");
        return dadoSensorRepository.findAll(pageable).map(dadoSensorMapper::toDto);
    }

    /**
     * Get one dadoSensor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DadoSensorDTO> findOne(Long id) {
        log.debug("Request to get DadoSensor : {}", id);
        return dadoSensorRepository.findById(id).map(dadoSensorMapper::toDto);
    }

    /**
     * Delete the dadoSensor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DadoSensor : {}", id);
        dadoSensorRepository.deleteById(id);
    }
}
