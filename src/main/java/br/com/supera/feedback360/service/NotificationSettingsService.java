package br.com.supera.feedback360.service;

import br.com.supera.feedback360.service.dto.NotificationSettingsDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link br.com.supera.feedback360.domain.NotificationSettings}.
 */
public interface NotificationSettingsService {
    /**
     * Save a notificationSettings.
     *
     * @param notificationSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<NotificationSettingsDTO> save(NotificationSettingsDTO notificationSettingsDTO);

    /**
     * Updates a notificationSettings.
     *
     * @param notificationSettingsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<NotificationSettingsDTO> update(NotificationSettingsDTO notificationSettingsDTO);

    /**
     * Partially updates a notificationSettings.
     *
     * @param notificationSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<NotificationSettingsDTO> partialUpdate(NotificationSettingsDTO notificationSettingsDTO);

    /**
     * Get all the notificationSettings.
     *
     * @return the list of entities.
     */
    Flux<NotificationSettingsDTO> findAll();

    /**
     * Get all the notificationSettings with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<NotificationSettingsDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of notificationSettings available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" notificationSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<NotificationSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" notificationSettings.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
