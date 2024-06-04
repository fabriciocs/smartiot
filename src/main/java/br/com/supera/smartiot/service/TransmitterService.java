package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.Transmitter;
import br.com.supera.smartiot.repository.TransmitterRepository;
import br.com.supera.smartiot.service.dto.TransmitterDTO;
import br.com.supera.smartiot.service.mapper.TransmitterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.Transmitter}.
 */
@Service
@Transactional
public class TransmitterService {

    private final Logger log = LoggerFactory.getLogger(TransmitterService.class);

    private final TransmitterRepository transmitterRepository;

    private final TransmitterMapper transmitterMapper;

    public TransmitterService(TransmitterRepository transmitterRepository, TransmitterMapper transmitterMapper) {
        this.transmitterRepository = transmitterRepository;
        this.transmitterMapper = transmitterMapper;
    }

    /**
     * Save a transmitter.
     *
     * @param transmitterDTO the entity to save.
     * @return the persisted entity.
     */
    public TransmitterDTO save(TransmitterDTO transmitterDTO) {
        log.debug("Request to save Transmitter : {}", transmitterDTO);
        Transmitter transmitter = transmitterMapper.toEntity(transmitterDTO);
        transmitter = transmitterRepository.save(transmitter);
        return transmitterMapper.toDto(transmitter);
    }

    /**
     * Update a transmitter.
     *
     * @param transmitterDTO the entity to save.
     * @return the persisted entity.
     */
    public TransmitterDTO update(TransmitterDTO transmitterDTO) {
        log.debug("Request to update Transmitter : {}", transmitterDTO);
        Transmitter transmitter = transmitterMapper.toEntity(transmitterDTO);
        transmitter = transmitterRepository.save(transmitter);
        return transmitterMapper.toDto(transmitter);
    }

    /**
     * Partially update a transmitter.
     *
     * @param transmitterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransmitterDTO> partialUpdate(TransmitterDTO transmitterDTO) {
        log.debug("Request to partially update Transmitter : {}", transmitterDTO);

        return transmitterRepository
            .findById(transmitterDTO.getId())
            .map(existingTransmitter -> {
                transmitterMapper.partialUpdate(existingTransmitter, transmitterDTO);

                return existingTransmitter;
            })
            .map(transmitterRepository::save)
            .map(transmitterMapper::toDto);
    }

    /**
     * Get all the transmitters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransmitterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transmitters");
        return transmitterRepository.findAll(pageable).map(transmitterMapper::toDto);
    }

    /**
     * Get one transmitter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransmitterDTO> findOne(Long id) {
        log.debug("Request to get Transmitter : {}", id);
        return transmitterRepository.findById(id).map(transmitterMapper::toDto);
    }

    /**
     * Delete the transmitter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Transmitter : {}", id);
        transmitterRepository.deleteById(id);
    }
}
