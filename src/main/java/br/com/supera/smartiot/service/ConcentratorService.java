package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.Concentrator;
import br.com.supera.smartiot.repository.ConcentratorRepository;
import br.com.supera.smartiot.service.dto.ConcentratorDTO;
import br.com.supera.smartiot.service.mapper.ConcentratorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.Concentrator}.
 */
@Service
@Transactional
public class ConcentratorService {

    private final Logger log = LoggerFactory.getLogger(ConcentratorService.class);

    private final ConcentratorRepository concentratorRepository;

    private final ConcentratorMapper concentratorMapper;

    public ConcentratorService(ConcentratorRepository concentratorRepository, ConcentratorMapper concentratorMapper) {
        this.concentratorRepository = concentratorRepository;
        this.concentratorMapper = concentratorMapper;
    }

    /**
     * Save a concentrator.
     *
     * @param concentratorDTO the entity to save.
     * @return the persisted entity.
     */
    public ConcentratorDTO save(ConcentratorDTO concentratorDTO) {
        log.debug("Request to save Concentrator : {}", concentratorDTO);
        Concentrator concentrator = concentratorMapper.toEntity(concentratorDTO);
        concentrator = concentratorRepository.save(concentrator);
        return concentratorMapper.toDto(concentrator);
    }

    /**
     * Update a concentrator.
     *
     * @param concentratorDTO the entity to save.
     * @return the persisted entity.
     */
    public ConcentratorDTO update(ConcentratorDTO concentratorDTO) {
        log.debug("Request to update Concentrator : {}", concentratorDTO);
        Concentrator concentrator = concentratorMapper.toEntity(concentratorDTO);
        concentrator = concentratorRepository.save(concentrator);
        return concentratorMapper.toDto(concentrator);
    }

    /**
     * Partially update a concentrator.
     *
     * @param concentratorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConcentratorDTO> partialUpdate(ConcentratorDTO concentratorDTO) {
        log.debug("Request to partially update Concentrator : {}", concentratorDTO);

        return concentratorRepository
            .findById(concentratorDTO.getId())
            .map(existingConcentrator -> {
                concentratorMapper.partialUpdate(existingConcentrator, concentratorDTO);

                return existingConcentrator;
            })
            .map(concentratorRepository::save)
            .map(concentratorMapper::toDto);
    }

    /**
     * Get all the concentrators.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConcentratorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Concentrators");
        return concentratorRepository.findAll(pageable).map(concentratorMapper::toDto);
    }

    /**
     * Get one concentrator by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConcentratorDTO> findOne(Long id) {
        log.debug("Request to get Concentrator : {}", id);
        return concentratorRepository.findById(id).map(concentratorMapper::toDto);
    }

    /**
     * Delete the concentrator by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Concentrator : {}", id);
        concentratorRepository.deleteById(id);
    }
}
