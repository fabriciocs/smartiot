package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.IntegrationConfigRepository;
import br.com.supera.feedback360.service.IntegrationConfigService;
import br.com.supera.feedback360.service.dto.IntegrationConfigDTO;
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
 * REST controller for managing {@link br.com.supera.feedback360.domain.IntegrationConfig}.
 */
@RestController
@RequestMapping("/api/integration-configs")
public class IntegrationConfigResource {

    private final Logger log = LoggerFactory.getLogger(IntegrationConfigResource.class);

    private static final String ENTITY_NAME = "integrationConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntegrationConfigService integrationConfigService;

    private final IntegrationConfigRepository integrationConfigRepository;

    public IntegrationConfigResource(
        IntegrationConfigService integrationConfigService,
        IntegrationConfigRepository integrationConfigRepository
    ) {
        this.integrationConfigService = integrationConfigService;
        this.integrationConfigRepository = integrationConfigRepository;
    }

    /**
     * {@code POST  /integration-configs} : Create a new integrationConfig.
     *
     * @param integrationConfigDTO the integrationConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new integrationConfigDTO, or with status {@code 400 (Bad Request)} if the integrationConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<IntegrationConfigDTO>> createIntegrationConfig(
        @Valid @RequestBody IntegrationConfigDTO integrationConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IntegrationConfig : {}", integrationConfigDTO);
        if (integrationConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new integrationConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return integrationConfigService
            .save(integrationConfigDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/integration-configs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /integration-configs/:id} : Updates an existing integrationConfig.
     *
     * @param id the id of the integrationConfigDTO to save.
     * @param integrationConfigDTO the integrationConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated integrationConfigDTO,
     * or with status {@code 400 (Bad Request)} if the integrationConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the integrationConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<IntegrationConfigDTO>> updateIntegrationConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IntegrationConfigDTO integrationConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IntegrationConfig : {}, {}", id, integrationConfigDTO);
        if (integrationConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, integrationConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return integrationConfigRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return integrationConfigService
                    .update(integrationConfigDTO)
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
     * {@code PATCH  /integration-configs/:id} : Partial updates given fields of an existing integrationConfig, field will ignore if it is null
     *
     * @param id the id of the integrationConfigDTO to save.
     * @param integrationConfigDTO the integrationConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated integrationConfigDTO,
     * or with status {@code 400 (Bad Request)} if the integrationConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the integrationConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the integrationConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IntegrationConfigDTO>> partialUpdateIntegrationConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IntegrationConfigDTO integrationConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IntegrationConfig partially : {}, {}", id, integrationConfigDTO);
        if (integrationConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, integrationConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return integrationConfigRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IntegrationConfigDTO> result = integrationConfigService.partialUpdate(integrationConfigDTO);

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
     * {@code GET  /integration-configs} : get all the integrationConfigs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of integrationConfigs in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<IntegrationConfigDTO>> getAllIntegrationConfigs(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all IntegrationConfigs");
        return integrationConfigService.findAll().collectList();
    }

    /**
     * {@code GET  /integration-configs} : get all the integrationConfigs as a stream.
     * @return the {@link Flux} of integrationConfigs.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IntegrationConfigDTO> getAllIntegrationConfigsAsStream() {
        log.debug("REST request to get all IntegrationConfigs as a stream");
        return integrationConfigService.findAll();
    }

    /**
     * {@code GET  /integration-configs/:id} : get the "id" integrationConfig.
     *
     * @param id the id of the integrationConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the integrationConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<IntegrationConfigDTO>> getIntegrationConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get IntegrationConfig : {}", id);
        Mono<IntegrationConfigDTO> integrationConfigDTO = integrationConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(integrationConfigDTO);
    }

    /**
     * {@code DELETE  /integration-configs/:id} : delete the "id" integrationConfig.
     *
     * @param id the id of the integrationConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteIntegrationConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete IntegrationConfig : {}", id);
        return integrationConfigService
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
