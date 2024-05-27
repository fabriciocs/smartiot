package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.ExternalSystemRepository;
import br.com.supera.feedback360.service.ExternalSystemService;
import br.com.supera.feedback360.service.dto.ExternalSystemDTO;
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
 * REST controller for managing {@link br.com.supera.feedback360.domain.ExternalSystem}.
 */
@RestController
@RequestMapping("/api/external-systems")
public class ExternalSystemResource {

    private final Logger log = LoggerFactory.getLogger(ExternalSystemResource.class);

    private static final String ENTITY_NAME = "externalSystem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExternalSystemService externalSystemService;

    private final ExternalSystemRepository externalSystemRepository;

    public ExternalSystemResource(ExternalSystemService externalSystemService, ExternalSystemRepository externalSystemRepository) {
        this.externalSystemService = externalSystemService;
        this.externalSystemRepository = externalSystemRepository;
    }

    /**
     * {@code POST  /external-systems} : Create a new externalSystem.
     *
     * @param externalSystemDTO the externalSystemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new externalSystemDTO, or with status {@code 400 (Bad Request)} if the externalSystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ExternalSystemDTO>> createExternalSystem(@Valid @RequestBody ExternalSystemDTO externalSystemDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExternalSystem : {}", externalSystemDTO);
        if (externalSystemDTO.getId() != null) {
            throw new BadRequestAlertException("A new externalSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return externalSystemService
            .save(externalSystemDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/external-systems/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /external-systems/:id} : Updates an existing externalSystem.
     *
     * @param id the id of the externalSystemDTO to save.
     * @param externalSystemDTO the externalSystemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalSystemDTO,
     * or with status {@code 400 (Bad Request)} if the externalSystemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the externalSystemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ExternalSystemDTO>> updateExternalSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExternalSystemDTO externalSystemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExternalSystem : {}, {}", id, externalSystemDTO);
        if (externalSystemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalSystemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return externalSystemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return externalSystemService
                    .update(externalSystemDTO)
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
     * {@code PATCH  /external-systems/:id} : Partial updates given fields of an existing externalSystem, field will ignore if it is null
     *
     * @param id the id of the externalSystemDTO to save.
     * @param externalSystemDTO the externalSystemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalSystemDTO,
     * or with status {@code 400 (Bad Request)} if the externalSystemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the externalSystemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the externalSystemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ExternalSystemDTO>> partialUpdateExternalSystem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExternalSystemDTO externalSystemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExternalSystem partially : {}, {}", id, externalSystemDTO);
        if (externalSystemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalSystemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return externalSystemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ExternalSystemDTO> result = externalSystemService.partialUpdate(externalSystemDTO);

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
     * {@code GET  /external-systems} : get all the externalSystems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of externalSystems in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ExternalSystemDTO>> getAllExternalSystems() {
        log.debug("REST request to get all ExternalSystems");
        return externalSystemService.findAll().collectList();
    }

    /**
     * {@code GET  /external-systems} : get all the externalSystems as a stream.
     * @return the {@link Flux} of externalSystems.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ExternalSystemDTO> getAllExternalSystemsAsStream() {
        log.debug("REST request to get all ExternalSystems as a stream");
        return externalSystemService.findAll();
    }

    /**
     * {@code GET  /external-systems/:id} : get the "id" externalSystem.
     *
     * @param id the id of the externalSystemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the externalSystemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ExternalSystemDTO>> getExternalSystem(@PathVariable("id") Long id) {
        log.debug("REST request to get ExternalSystem : {}", id);
        Mono<ExternalSystemDTO> externalSystemDTO = externalSystemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(externalSystemDTO);
    }

    /**
     * {@code DELETE  /external-systems/:id} : delete the "id" externalSystem.
     *
     * @param id the id of the externalSystemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteExternalSystem(@PathVariable("id") Long id) {
        log.debug("REST request to delete ExternalSystem : {}", id);
        return externalSystemService
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
