package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.NotificationSettingsRepository;
import br.com.supera.feedback360.service.NotificationSettingsService;
import br.com.supera.feedback360.service.dto.NotificationSettingsDTO;
import br.com.supera.feedback360.service.mapper.NotificationSettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.NotificationSettings}.
 */
@Service
@Transactional
public class NotificationSettingsServiceImpl implements NotificationSettingsService {

    private final Logger log = LoggerFactory.getLogger(NotificationSettingsServiceImpl.class);

    private final NotificationSettingsRepository notificationSettingsRepository;

    private final NotificationSettingsMapper notificationSettingsMapper;

    public NotificationSettingsServiceImpl(
        NotificationSettingsRepository notificationSettingsRepository,
        NotificationSettingsMapper notificationSettingsMapper
    ) {
        this.notificationSettingsRepository = notificationSettingsRepository;
        this.notificationSettingsMapper = notificationSettingsMapper;
    }

    @Override
    public Mono<NotificationSettingsDTO> save(NotificationSettingsDTO notificationSettingsDTO) {
        log.debug("Request to save NotificationSettings : {}", notificationSettingsDTO);
        return notificationSettingsRepository
            .save(notificationSettingsMapper.toEntity(notificationSettingsDTO))
            .map(notificationSettingsMapper::toDto);
    }

    @Override
    public Mono<NotificationSettingsDTO> update(NotificationSettingsDTO notificationSettingsDTO) {
        log.debug("Request to update NotificationSettings : {}", notificationSettingsDTO);
        return notificationSettingsRepository
            .save(notificationSettingsMapper.toEntity(notificationSettingsDTO))
            .map(notificationSettingsMapper::toDto);
    }

    @Override
    public Mono<NotificationSettingsDTO> partialUpdate(NotificationSettingsDTO notificationSettingsDTO) {
        log.debug("Request to partially update NotificationSettings : {}", notificationSettingsDTO);

        return notificationSettingsRepository
            .findById(notificationSettingsDTO.getId())
            .map(existingNotificationSettings -> {
                notificationSettingsMapper.partialUpdate(existingNotificationSettings, notificationSettingsDTO);

                return existingNotificationSettings;
            })
            .flatMap(notificationSettingsRepository::save)
            .map(notificationSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<NotificationSettingsDTO> findAll() {
        log.debug("Request to get all NotificationSettings");
        return notificationSettingsRepository.findAll().map(notificationSettingsMapper::toDto);
    }

    public Flux<NotificationSettingsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return notificationSettingsRepository.findAllWithEagerRelationships(pageable).map(notificationSettingsMapper::toDto);
    }

    public Mono<Long> countAll() {
        return notificationSettingsRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<NotificationSettingsDTO> findOne(Long id) {
        log.debug("Request to get NotificationSettings : {}", id);
        return notificationSettingsRepository.findOneWithEagerRelationships(id).map(notificationSettingsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete NotificationSettings : {}", id);
        return notificationSettingsRepository.deleteById(id);
    }
}
