package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.ManualEntry;
import br.com.supera.smartiot.repository.ManualEntryRepository;
import br.com.supera.smartiot.service.dto.ManualEntryDTO;
import br.com.supera.smartiot.service.mapper.ManualEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.ManualEntry}.
 */
@Service
@Transactional
public class ManualEntryService {

    private final Logger log = LoggerFactory.getLogger(ManualEntryService.class);

    private final ManualEntryRepository manualEntryRepository;

    private final ManualEntryMapper manualEntryMapper;

    public ManualEntryService(ManualEntryRepository manualEntryRepository, ManualEntryMapper manualEntryMapper) {
        this.manualEntryRepository = manualEntryRepository;
        this.manualEntryMapper = manualEntryMapper;
    }

    /**
     * Save a manualEntry.
     *
     * @param manualEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public ManualEntryDTO save(ManualEntryDTO manualEntryDTO) {
        log.debug("Request to save ManualEntry : {}", manualEntryDTO);
        ManualEntry manualEntry = manualEntryMapper.toEntity(manualEntryDTO);
        manualEntry = manualEntryRepository.save(manualEntry);
        return manualEntryMapper.toDto(manualEntry);
    }

    /**
     * Update a manualEntry.
     *
     * @param manualEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public ManualEntryDTO update(ManualEntryDTO manualEntryDTO) {
        log.debug("Request to update ManualEntry : {}", manualEntryDTO);
        ManualEntry manualEntry = manualEntryMapper.toEntity(manualEntryDTO);
        manualEntry = manualEntryRepository.save(manualEntry);
        return manualEntryMapper.toDto(manualEntry);
    }

    /**
     * Partially update a manualEntry.
     *
     * @param manualEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ManualEntryDTO> partialUpdate(ManualEntryDTO manualEntryDTO) {
        log.debug("Request to partially update ManualEntry : {}", manualEntryDTO);

        return manualEntryRepository
            .findById(manualEntryDTO.getId())
            .map(existingManualEntry -> {
                manualEntryMapper.partialUpdate(existingManualEntry, manualEntryDTO);

                return existingManualEntry;
            })
            .map(manualEntryRepository::save)
            .map(manualEntryMapper::toDto);
    }

    /**
     * Get all the manualEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ManualEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ManualEntries");
        return manualEntryRepository.findAll(pageable).map(manualEntryMapper::toDto);
    }

    /**
     * Get one manualEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ManualEntryDTO> findOne(Long id) {
        log.debug("Request to get ManualEntry : {}", id);
        return manualEntryRepository.findById(id).map(manualEntryMapper::toDto);
    }

    /**
     * Delete the manualEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ManualEntry : {}", id);
        manualEntryRepository.deleteById(id);
    }
}
