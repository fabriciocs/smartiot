package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.Alert;
import br.com.supera.smartiot.repository.AlertRepository;
import br.com.supera.smartiot.service.dto.AlertDTO;
import br.com.supera.smartiot.service.mapper.AlertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.Alert}.
 */
@Service
@Transactional
public class AlertService {

    private final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final AlertRepository alertRepository;

    private final AlertMapper alertMapper;

    public AlertService(AlertRepository alertRepository, AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    /**
     * Save a alert.
     *
     * @param alertDTO the entity to save.
     * @return the persisted entity.
     */
    public AlertDTO save(AlertDTO alertDTO) {
        log.debug("Request to save Alert : {}", alertDTO);
        Alert alert = alertMapper.toEntity(alertDTO);
        alert = alertRepository.save(alert);
        return alertMapper.toDto(alert);
    }

    /**
     * Update a alert.
     *
     * @param alertDTO the entity to save.
     * @return the persisted entity.
     */
    public AlertDTO update(AlertDTO alertDTO) {
        log.debug("Request to update Alert : {}", alertDTO);
        Alert alert = alertMapper.toEntity(alertDTO);
        alert = alertRepository.save(alert);
        return alertMapper.toDto(alert);
    }

    /**
     * Partially update a alert.
     *
     * @param alertDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlertDTO> partialUpdate(AlertDTO alertDTO) {
        log.debug("Request to partially update Alert : {}", alertDTO);

        return alertRepository
            .findById(alertDTO.getId())
            .map(existingAlert -> {
                alertMapper.partialUpdate(existingAlert, alertDTO);

                return existingAlert;
            })
            .map(alertRepository::save)
            .map(alertMapper::toDto);
    }

    /**
     * Get all the alerts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AlertDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Alerts");
        return alertRepository.findAll(pageable).map(alertMapper::toDto);
    }

    /**
     * Get one alert by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlertDTO> findOne(Long id) {
        log.debug("Request to get Alert : {}", id);
        return alertRepository.findById(id).map(alertMapper::toDto);
    }

    /**
     * Delete the alert by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Alert : {}", id);
        alertRepository.deleteById(id);
    }
}
