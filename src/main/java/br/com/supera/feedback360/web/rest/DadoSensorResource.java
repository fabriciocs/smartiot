package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.DadoSensorRepository;
import br.com.supera.feedback360.service.DadoSensorService;
import br.com.supera.feedback360.service.dto.DadoSensorDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link br.com.supera.feedback360.domain.DadoSensor}.
 */
@RestController
@RequestMapping("/api/dado-sensors")
public class DadoSensorResource {

    private final Logger log = LoggerFactory.getLogger(DadoSensorResource.class);

    private static final String ENTITY_NAME = "dadoSensor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DadoSensorService dadoSensorService;

    private final DadoSensorRepository dadoSensorRepository;

    public DadoSensorResource(DadoSensorService dadoSensorService, DadoSensorRepository dadoSensorRepository) {
        this.dadoSensorService = dadoSensorService;
        this.dadoSensorRepository = dadoSensorRepository;
    }

    /**
     * {@code POST  /dado-sensors} : Create a new dadoSensor.
     *
     * @param dadoSensorDTO the dadoSensorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dadoSensorDTO, or with status {@code 400 (Bad Request)} if the dadoSensor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DadoSensorDTO>> createDadoSensor(@Valid @RequestBody DadoSensorDTO dadoSensorDTO) throws URISyntaxException {
        log.debug("REST request to save DadoSensor : {}", dadoSensorDTO);
        if (dadoSensorDTO.getId() != null) {
            throw new BadRequestAlertException("A new dadoSensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return dadoSensorService
            .save(dadoSensorDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/dado-sensors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /dado-sensors/:id} : Updates an existing dadoSensor.
     *
     * @param id the id of the dadoSensorDTO to save.
     * @param dadoSensorDTO the dadoSensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dadoSensorDTO,
     * or with status {@code 400 (Bad Request)} if the dadoSensorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dadoSensorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DadoSensorDTO>> updateDadoSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DadoSensorDTO dadoSensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DadoSensor : {}, {}", id, dadoSensorDTO);
        if (dadoSensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dadoSensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dadoSensorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return dadoSensorService
                    .update(dadoSensorDTO)
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
     * {@code PATCH  /dado-sensors/:id} : Partial updates given fields of an existing dadoSensor, field will ignore if it is null
     *
     * @param id the id of the dadoSensorDTO to save.
     * @param dadoSensorDTO the dadoSensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dadoSensorDTO,
     * or with status {@code 400 (Bad Request)} if the dadoSensorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dadoSensorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dadoSensorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DadoSensorDTO>> partialUpdateDadoSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DadoSensorDTO dadoSensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DadoSensor partially : {}, {}", id, dadoSensorDTO);
        if (dadoSensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dadoSensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dadoSensorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DadoSensorDTO> result = dadoSensorService.partialUpdate(dadoSensorDTO);

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
     * {@code GET  /dado-sensors} : get all the dadoSensors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dadoSensors in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<DadoSensorDTO>>> getAllDadoSensors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of DadoSensors");
        return dadoSensorService
            .countAll()
            .zipWith(dadoSensorService.findAll(pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /dado-sensors/:id} : get the "id" dadoSensor.
     *
     * @param id the id of the dadoSensorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dadoSensorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DadoSensorDTO>> getDadoSensor(@PathVariable("id") Long id) {
        log.debug("REST request to get DadoSensor : {}", id);
        Mono<DadoSensorDTO> dadoSensorDTO = dadoSensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dadoSensorDTO);
    }

    /**
     * {@code DELETE  /dado-sensors/:id} : delete the "id" dadoSensor.
     *
     * @param id the id of the dadoSensorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDadoSensor(@PathVariable("id") Long id) {
        log.debug("REST request to delete DadoSensor : {}", id);
        return dadoSensorService
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
