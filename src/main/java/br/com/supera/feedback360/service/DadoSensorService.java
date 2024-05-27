package br.com.supera.feedback360.service;

import br.com.supera.feedback360.repository.DadoSensorRepository;
import br.com.supera.feedback360.service.dto.DadoSensorDTO;
import br.com.supera.feedback360.service.mapper.DadoSensorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.DadoSensor}.
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
    public Mono<DadoSensorDTO> save(DadoSensorDTO dadoSensorDTO) {
        log.debug("Request to save DadoSensor : {}", dadoSensorDTO);
        return dadoSensorRepository.save(dadoSensorMapper.toEntity(dadoSensorDTO)).map(dadoSensorMapper::toDto);
    }

    /**
     * Update a dadoSensor.
     *
     * @param dadoSensorDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DadoSensorDTO> update(DadoSensorDTO dadoSensorDTO) {
        log.debug("Request to update DadoSensor : {}", dadoSensorDTO);
        return dadoSensorRepository.save(dadoSensorMapper.toEntity(dadoSensorDTO)).map(dadoSensorMapper::toDto);
    }

    /**
     * Partially update a dadoSensor.
     *
     * @param dadoSensorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DadoSensorDTO> partialUpdate(DadoSensorDTO dadoSensorDTO) {
        log.debug("Request to partially update DadoSensor : {}", dadoSensorDTO);

        return dadoSensorRepository
            .findById(dadoSensorDTO.getId())
            .map(existingDadoSensor -> {
                dadoSensorMapper.partialUpdate(existingDadoSensor, dadoSensorDTO);

                return existingDadoSensor;
            })
            .flatMap(dadoSensorRepository::save)
            .map(dadoSensorMapper::toDto);
    }

    /**
     * Get all the dadoSensors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DadoSensorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DadoSensors");
        return dadoSensorRepository.findAllBy(pageable).map(dadoSensorMapper::toDto);
    }

    /**
     * Returns the number of dadoSensors available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return dadoSensorRepository.count();
    }

    /**
     * Get one dadoSensor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DadoSensorDTO> findOne(Long id) {
        log.debug("Request to get DadoSensor : {}", id);
        return dadoSensorRepository.findById(id).map(dadoSensorMapper::toDto);
    }

    /**
     * Delete the dadoSensor by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DadoSensor : {}", id);
        return dadoSensorRepository.deleteById(id);
    }
}
