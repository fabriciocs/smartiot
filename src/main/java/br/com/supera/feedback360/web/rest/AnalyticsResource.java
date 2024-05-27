package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.AnalyticsRepository;
import br.com.supera.feedback360.service.AnalyticsService;
import br.com.supera.feedback360.service.dto.AnalyticsDTO;
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
 * REST controller for managing {@link br.com.supera.feedback360.domain.Analytics}.
 */
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsResource {

    private final Logger log = LoggerFactory.getLogger(AnalyticsResource.class);

    private static final String ENTITY_NAME = "analytics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalyticsService analyticsService;

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsResource(AnalyticsService analyticsService, AnalyticsRepository analyticsRepository) {
        this.analyticsService = analyticsService;
        this.analyticsRepository = analyticsRepository;
    }

    /**
     * {@code POST  /analytics} : Create a new analytics.
     *
     * @param analyticsDTO the analyticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analyticsDTO, or with status {@code 400 (Bad Request)} if the analytics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AnalyticsDTO>> createAnalytics(@Valid @RequestBody AnalyticsDTO analyticsDTO) throws URISyntaxException {
        log.debug("REST request to save Analytics : {}", analyticsDTO);
        if (analyticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new analytics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return analyticsService
            .save(analyticsDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/analytics/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /analytics/:id} : Updates an existing analytics.
     *
     * @param id the id of the analyticsDTO to save.
     * @param analyticsDTO the analyticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyticsDTO,
     * or with status {@code 400 (Bad Request)} if the analyticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analyticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AnalyticsDTO>> updateAnalytics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnalyticsDTO analyticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Analytics : {}, {}", id, analyticsDTO);
        if (analyticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return analyticsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return analyticsService
                    .update(analyticsDTO)
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
     * {@code PATCH  /analytics/:id} : Partial updates given fields of an existing analytics, field will ignore if it is null
     *
     * @param id the id of the analyticsDTO to save.
     * @param analyticsDTO the analyticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyticsDTO,
     * or with status {@code 400 (Bad Request)} if the analyticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the analyticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the analyticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AnalyticsDTO>> partialUpdateAnalytics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnalyticsDTO analyticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Analytics partially : {}, {}", id, analyticsDTO);
        if (analyticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return analyticsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AnalyticsDTO> result = analyticsService.partialUpdate(analyticsDTO);

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
     * {@code GET  /analytics} : get all the analytics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analytics in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<AnalyticsDTO>> getAllAnalytics() {
        log.debug("REST request to get all Analytics");
        return analyticsService.findAll().collectList();
    }

    /**
     * {@code GET  /analytics} : get all the analytics as a stream.
     * @return the {@link Flux} of analytics.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AnalyticsDTO> getAllAnalyticsAsStream() {
        log.debug("REST request to get all Analytics as a stream");
        return analyticsService.findAll();
    }

    /**
     * {@code GET  /analytics/:id} : get the "id" analytics.
     *
     * @param id the id of the analyticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analyticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AnalyticsDTO>> getAnalytics(@PathVariable("id") Long id) {
        log.debug("REST request to get Analytics : {}", id);
        Mono<AnalyticsDTO> analyticsDTO = analyticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(analyticsDTO);
    }

    /**
     * {@code DELETE  /analytics/:id} : delete the "id" analytics.
     *
     * @param id the id of the analyticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAnalytics(@PathVariable("id") Long id) {
        log.debug("REST request to delete Analytics : {}", id);
        return analyticsService
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
