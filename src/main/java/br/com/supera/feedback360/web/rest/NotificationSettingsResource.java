package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.NotificationSettingsRepository;
import br.com.supera.feedback360.service.NotificationSettingsService;
import br.com.supera.feedback360.service.dto.NotificationSettingsDTO;
import br.com.supera.feedback360.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link br.com.supera.feedback360.domain.NotificationSettings}.
 */
@RestController
@RequestMapping("/api/notification-settings")
public class NotificationSettingsResource {

    private final Logger log = LoggerFactory.getLogger(NotificationSettingsResource.class);

    private static final String ENTITY_NAME = "notificationSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationSettingsService notificationSettingsService;

    private final NotificationSettingsRepository notificationSettingsRepository;

    public NotificationSettingsResource(
        NotificationSettingsService notificationSettingsService,
        NotificationSettingsRepository notificationSettingsRepository
    ) {
        this.notificationSettingsService = notificationSettingsService;
        this.notificationSettingsRepository = notificationSettingsRepository;
    }

    /**
     * {@code POST  /notification-settings} : Create a new notificationSettings.
     *
     * @param notificationSettingsDTO the notificationSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationSettingsDTO, or with status {@code 400 (Bad Request)} if the notificationSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<NotificationSettingsDTO>> createNotificationSettings(
        @Valid @RequestBody NotificationSettingsDTO notificationSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save NotificationSettings : {}", notificationSettingsDTO);
        if (notificationSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return notificationSettingsService
            .save(notificationSettingsDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/notification-settings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /notification-settings/:id} : Updates an existing notificationSettings.
     *
     * @param id the id of the notificationSettingsDTO to save.
     * @param notificationSettingsDTO the notificationSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the notificationSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<NotificationSettingsDTO>> updateNotificationSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationSettingsDTO notificationSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NotificationSettings : {}, {}", id, notificationSettingsDTO);
        if (notificationSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return notificationSettingsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return notificationSettingsService
                    .update(notificationSettingsDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /notification-settings/:id} : Partial updates given fields of an existing notificationSettings, field will ignore if it is null
     *
     * @param id the id of the notificationSettingsDTO to save.
     * @param notificationSettingsDTO the notificationSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the notificationSettingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationSettingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NotificationSettingsDTO>> partialUpdateNotificationSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationSettingsDTO notificationSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NotificationSettings partially : {}, {}", id, notificationSettingsDTO);
        if (notificationSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return notificationSettingsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<NotificationSettingsDTO> result = notificationSettingsService.partialUpdate(notificationSettingsDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /notification-settings} : get all the notificationSettings.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationSettings in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<NotificationSettingsDTO>> getAllNotificationSettings(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all NotificationSettings");
        return notificationSettingsService.findAll().collectList();
    }

    /**
     * {@code GET  /notification-settings} : get all the notificationSettings as a stream.
     * @return the {@link Flux} of notificationSettings.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<NotificationSettingsDTO> getAllNotificationSettingsAsStream() {
        log.debug("REST request to get all NotificationSettings as a stream");
        return notificationSettingsService.findAll();
    }

    /**
     * {@code GET  /notification-settings/:id} : get the "id" notificationSettings.
     *
     * @param id the id of the notificationSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NotificationSettingsDTO>> getNotificationSettings(@PathVariable("id") Long id) {
        log.debug("REST request to get NotificationSettings : {}", id);
        Mono<NotificationSettingsDTO> notificationSettingsDTO = notificationSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationSettingsDTO);
    }

    /**
     * {@code DELETE  /notification-settings/:id} : delete the "id" notificationSettings.
     *
     * @param id the id of the notificationSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteNotificationSettings(@PathVariable("id") Long id) {
        log.debug("REST request to delete NotificationSettings : {}", id);
        return notificationSettingsService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
