package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.SysRoleRepository;
import br.com.supera.feedback360.service.SysRoleService;
import br.com.supera.feedback360.service.dto.SysRoleDTO;
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
 * REST controller for managing {@link br.com.supera.feedback360.domain.SysRole}.
 */
@RestController
@RequestMapping("/api/sys-roles")
public class SysRoleResource {

    private final Logger log = LoggerFactory.getLogger(SysRoleResource.class);

    private static final String ENTITY_NAME = "sysRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysRoleService sysRoleService;

    private final SysRoleRepository sysRoleRepository;

    public SysRoleResource(SysRoleService sysRoleService, SysRoleRepository sysRoleRepository) {
        this.sysRoleService = sysRoleService;
        this.sysRoleRepository = sysRoleRepository;
    }

    /**
     * {@code POST  /sys-roles} : Create a new sysRole.
     *
     * @param sysRoleDTO the sysRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysRoleDTO, or with status {@code 400 (Bad Request)} if the sysRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SysRoleDTO>> createSysRole(@Valid @RequestBody SysRoleDTO sysRoleDTO) throws URISyntaxException {
        log.debug("REST request to save SysRole : {}", sysRoleDTO);
        if (sysRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sysRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return sysRoleService
            .save(sysRoleDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/sys-roles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sys-roles/:id} : Updates an existing sysRole.
     *
     * @param id the id of the sysRoleDTO to save.
     * @param sysRoleDTO the sysRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleDTO,
     * or with status {@code 400 (Bad Request)} if the sysRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SysRoleDTO>> updateSysRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SysRoleDTO sysRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SysRole : {}, {}", id, sysRoleDTO);
        if (sysRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sysRoleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return sysRoleService
                    .update(sysRoleDTO)
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
     * {@code PATCH  /sys-roles/:id} : Partial updates given fields of an existing sysRole, field will ignore if it is null
     *
     * @param id the id of the sysRoleDTO to save.
     * @param sysRoleDTO the sysRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleDTO,
     * or with status {@code 400 (Bad Request)} if the sysRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sysRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SysRoleDTO>> partialUpdateSysRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SysRoleDTO sysRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysRole partially : {}, {}", id, sysRoleDTO);
        if (sysRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return sysRoleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SysRoleDTO> result = sysRoleService.partialUpdate(sysRoleDTO);

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
     * {@code GET  /sys-roles} : get all the sysRoles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysRoles in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<SysRoleDTO>> getAllSysRoles() {
        log.debug("REST request to get all SysRoles");
        return sysRoleService.findAll().collectList();
    }

    /**
     * {@code GET  /sys-roles} : get all the sysRoles as a stream.
     * @return the {@link Flux} of sysRoles.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SysRoleDTO> getAllSysRolesAsStream() {
        log.debug("REST request to get all SysRoles as a stream");
        return sysRoleService.findAll();
    }

    /**
     * {@code GET  /sys-roles/:id} : get the "id" sysRole.
     *
     * @param id the id of the sysRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SysRoleDTO>> getSysRole(@PathVariable("id") Long id) {
        log.debug("REST request to get SysRole : {}", id);
        Mono<SysRoleDTO> sysRoleDTO = sysRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sysRoleDTO);
    }

    /**
     * {@code DELETE  /sys-roles/:id} : delete the "id" sysRole.
     *
     * @param id the id of the sysRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSysRole(@PathVariable("id") Long id) {
        log.debug("REST request to delete SysRole : {}", id);
        return sysRoleService
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
