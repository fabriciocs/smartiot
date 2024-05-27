package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.ConfiguracaoAlertaRepository;
import br.com.supera.feedback360.service.ConfiguracaoAlertaService;
import br.com.supera.feedback360.service.dto.ConfiguracaoAlertaDTO;
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
 * REST controller for managing {@link br.com.supera.feedback360.domain.ConfiguracaoAlerta}.
 */
@RestController
@RequestMapping("/api/configuracao-alertas")
public class ConfiguracaoAlertaResource {

    private final Logger log = LoggerFactory.getLogger(ConfiguracaoAlertaResource.class);

    private static final String ENTITY_NAME = "configuracaoAlerta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfiguracaoAlertaService configuracaoAlertaService;

    private final ConfiguracaoAlertaRepository configuracaoAlertaRepository;

    public ConfiguracaoAlertaResource(
        ConfiguracaoAlertaService configuracaoAlertaService,
        ConfiguracaoAlertaRepository configuracaoAlertaRepository
    ) {
        this.configuracaoAlertaService = configuracaoAlertaService;
        this.configuracaoAlertaRepository = configuracaoAlertaRepository;
    }

    /**
     * {@code POST  /configuracao-alertas} : Create a new configuracaoAlerta.
     *
     * @param configuracaoAlertaDTO the configuracaoAlertaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configuracaoAlertaDTO, or with status {@code 400 (Bad Request)} if the configuracaoAlerta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ConfiguracaoAlertaDTO>> createConfiguracaoAlerta(
        @Valid @RequestBody ConfiguracaoAlertaDTO configuracaoAlertaDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ConfiguracaoAlerta : {}", configuracaoAlertaDTO);
        if (configuracaoAlertaDTO.getId() != null) {
            throw new BadRequestAlertException("A new configuracaoAlerta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return configuracaoAlertaService
            .save(configuracaoAlertaDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/configuracao-alertas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /configuracao-alertas/:id} : Updates an existing configuracaoAlerta.
     *
     * @param id the id of the configuracaoAlertaDTO to save.
     * @param configuracaoAlertaDTO the configuracaoAlertaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracaoAlertaDTO,
     * or with status {@code 400 (Bad Request)} if the configuracaoAlertaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configuracaoAlertaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ConfiguracaoAlertaDTO>> updateConfiguracaoAlerta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConfiguracaoAlertaDTO configuracaoAlertaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ConfiguracaoAlerta : {}, {}", id, configuracaoAlertaDTO);
        if (configuracaoAlertaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configuracaoAlertaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return configuracaoAlertaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return configuracaoAlertaService
                    .update(configuracaoAlertaDTO)
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
     * {@code PATCH  /configuracao-alertas/:id} : Partial updates given fields of an existing configuracaoAlerta, field will ignore if it is null
     *
     * @param id the id of the configuracaoAlertaDTO to save.
     * @param configuracaoAlertaDTO the configuracaoAlertaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracaoAlertaDTO,
     * or with status {@code 400 (Bad Request)} if the configuracaoAlertaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the configuracaoAlertaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the configuracaoAlertaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ConfiguracaoAlertaDTO>> partialUpdateConfiguracaoAlerta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConfiguracaoAlertaDTO configuracaoAlertaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConfiguracaoAlerta partially : {}, {}", id, configuracaoAlertaDTO);
        if (configuracaoAlertaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configuracaoAlertaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return configuracaoAlertaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ConfiguracaoAlertaDTO> result = configuracaoAlertaService.partialUpdate(configuracaoAlertaDTO);

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
     * {@code GET  /configuracao-alertas} : get all the configuracaoAlertas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configuracaoAlertas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ConfiguracaoAlertaDTO>>> getAllConfiguracaoAlertas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ConfiguracaoAlertas");
        return configuracaoAlertaService
            .countAll()
            .zipWith(configuracaoAlertaService.findAll(pageable).collectList())
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
     * {@code GET  /configuracao-alertas/:id} : get the "id" configuracaoAlerta.
     *
     * @param id the id of the configuracaoAlertaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configuracaoAlertaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ConfiguracaoAlertaDTO>> getConfiguracaoAlerta(@PathVariable("id") Long id) {
        log.debug("REST request to get ConfiguracaoAlerta : {}", id);
        Mono<ConfiguracaoAlertaDTO> configuracaoAlertaDTO = configuracaoAlertaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configuracaoAlertaDTO);
    }

    /**
     * {@code DELETE  /configuracao-alertas/:id} : delete the "id" configuracaoAlerta.
     *
     * @param id the id of the configuracaoAlertaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteConfiguracaoAlerta(@PathVariable("id") Long id) {
        log.debug("REST request to delete ConfiguracaoAlerta : {}", id);
        return configuracaoAlertaService
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
