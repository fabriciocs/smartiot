package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.Repeater;
import br.com.supera.smartiot.repository.RepeaterRepository;
import br.com.supera.smartiot.service.dto.RepeaterDTO;
import br.com.supera.smartiot.service.mapper.RepeaterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.Repeater}.
 */
@Service
@Transactional
public class RepeaterService {

    private final Logger log = LoggerFactory.getLogger(RepeaterService.class);

    private final RepeaterRepository repeaterRepository;

    private final RepeaterMapper repeaterMapper;

    public RepeaterService(RepeaterRepository repeaterRepository, RepeaterMapper repeaterMapper) {
        this.repeaterRepository = repeaterRepository;
        this.repeaterMapper = repeaterMapper;
    }

    /**
     * Save a repeater.
     *
     * @param repeaterDTO the entity to save.
     * @return the persisted entity.
     */
    public RepeaterDTO save(RepeaterDTO repeaterDTO) {
        log.debug("Request to save Repeater : {}", repeaterDTO);
        Repeater repeater = repeaterMapper.toEntity(repeaterDTO);
        repeater = repeaterRepository.save(repeater);
        return repeaterMapper.toDto(repeater);
    }

    /**
     * Update a repeater.
     *
     * @param repeaterDTO the entity to save.
     * @return the persisted entity.
     */
    public RepeaterDTO update(RepeaterDTO repeaterDTO) {
        log.debug("Request to update Repeater : {}", repeaterDTO);
        Repeater repeater = repeaterMapper.toEntity(repeaterDTO);
        repeater = repeaterRepository.save(repeater);
        return repeaterMapper.toDto(repeater);
    }

    /**
     * Partially update a repeater.
     *
     * @param repeaterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RepeaterDTO> partialUpdate(RepeaterDTO repeaterDTO) {
        log.debug("Request to partially update Repeater : {}", repeaterDTO);

        return repeaterRepository
            .findById(repeaterDTO.getId())
            .map(existingRepeater -> {
                repeaterMapper.partialUpdate(existingRepeater, repeaterDTO);

                return existingRepeater;
            })
            .map(repeaterRepository::save)
            .map(repeaterMapper::toDto);
    }

    /**
     * Get all the repeaters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RepeaterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Repeaters");
        return repeaterRepository.findAll(pageable).map(repeaterMapper::toDto);
    }

    /**
     * Get one repeater by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RepeaterDTO> findOne(Long id) {
        log.debug("Request to get Repeater : {}", id);
        return repeaterRepository.findById(id).map(repeaterMapper::toDto);
    }

    /**
     * Delete the repeater by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Repeater : {}", id);
        repeaterRepository.deleteById(id);
    }
}
