package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.Meter;
import br.com.supera.smartiot.repository.MeterRepository;
import br.com.supera.smartiot.service.dto.MeterDTO;
import br.com.supera.smartiot.service.mapper.MeterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.Meter}.
 */
@Service
@Transactional
public class MeterService {

    private final Logger log = LoggerFactory.getLogger(MeterService.class);

    private final MeterRepository meterRepository;

    private final MeterMapper meterMapper;

    public MeterService(MeterRepository meterRepository, MeterMapper meterMapper) {
        this.meterRepository = meterRepository;
        this.meterMapper = meterMapper;
    }

    /**
     * Save a meter.
     *
     * @param meterDTO the entity to save.
     * @return the persisted entity.
     */
    public MeterDTO save(MeterDTO meterDTO) {
        log.debug("Request to save Meter : {}", meterDTO);
        Meter meter = meterMapper.toEntity(meterDTO);
        meter = meterRepository.save(meter);
        return meterMapper.toDto(meter);
    }

    /**
     * Update a meter.
     *
     * @param meterDTO the entity to save.
     * @return the persisted entity.
     */
    public MeterDTO update(MeterDTO meterDTO) {
        log.debug("Request to update Meter : {}", meterDTO);
        Meter meter = meterMapper.toEntity(meterDTO);
        meter = meterRepository.save(meter);
        return meterMapper.toDto(meter);
    }

    /**
     * Partially update a meter.
     *
     * @param meterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MeterDTO> partialUpdate(MeterDTO meterDTO) {
        log.debug("Request to partially update Meter : {}", meterDTO);

        return meterRepository
            .findById(meterDTO.getId())
            .map(existingMeter -> {
                meterMapper.partialUpdate(existingMeter, meterDTO);

                return existingMeter;
            })
            .map(meterRepository::save)
            .map(meterMapper::toDto);
    }

    /**
     * Get all the meters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MeterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Meters");
        return meterRepository.findAll(pageable).map(meterMapper::toDto);
    }

    /**
     * Get one meter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MeterDTO> findOne(Long id) {
        log.debug("Request to get Meter : {}", id);
        return meterRepository.findById(id).map(meterMapper::toDto);
    }

    /**
     * Delete the meter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Meter : {}", id);
        meterRepository.deleteById(id);
    }
}
