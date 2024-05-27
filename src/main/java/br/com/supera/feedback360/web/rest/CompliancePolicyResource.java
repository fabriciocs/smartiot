package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.CompliancePolicyRepository;
import br.com.supera.feedback360.service.CompliancePolicyService;
import br.com.supera.feedback360.service.dto.CompliancePolicyDTO;
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
 * REST controller for managing {@link br.com.supera.feedback360.domain.CompliancePolicy}.
 */
@RestController
@RequestMapping("/api/compliance-policies")
public class CompliancePolicyResource {

    private final Logger log = LoggerFactory.getLogger(CompliancePolicyResource.class);

    private static final String ENTITY_NAME = "compliancePolicy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompliancePolicyService compliancePolicyService;

    private final CompliancePolicyRepository compliancePolicyRepository;

    public CompliancePolicyResource(
        CompliancePolicyService compliancePolicyService,
        CompliancePolicyRepository compliancePolicyRepository
    ) {
        this.compliancePolicyService = compliancePolicyService;
        this.compliancePolicyRepository = compliancePolicyRepository;
    }

    /**
     * {@code POST  /compliance-policies} : Create a new compliancePolicy.
     *
     * @param compliancePolicyDTO the compliancePolicyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compliancePolicyDTO, or with status {@code 400 (Bad Request)} if the compliancePolicy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CompliancePolicyDTO>> createCompliancePolicy(@Valid @RequestBody CompliancePolicyDTO compliancePolicyDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompliancePolicy : {}", compliancePolicyDTO);
        if (compliancePolicyDTO.getId() != null) {
            throw new BadRequestAlertException("A new compliancePolicy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return compliancePolicyService
            .save(compliancePolicyDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/compliance-policies/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /compliance-policies/:id} : Updates an existing compliancePolicy.
     *
     * @param id the id of the compliancePolicyDTO to save.
     * @param compliancePolicyDTO the compliancePolicyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compliancePolicyDTO,
     * or with status {@code 400 (Bad Request)} if the compliancePolicyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compliancePolicyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CompliancePolicyDTO>> updateCompliancePolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompliancePolicyDTO compliancePolicyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompliancePolicy : {}, {}", id, compliancePolicyDTO);
        if (compliancePolicyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compliancePolicyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return compliancePolicyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return compliancePolicyService
                    .update(compliancePolicyDTO)
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
     * {@code PATCH  /compliance-policies/:id} : Partial updates given fields of an existing compliancePolicy, field will ignore if it is null
     *
     * @param id the id of the compliancePolicyDTO to save.
     * @param compliancePolicyDTO the compliancePolicyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compliancePolicyDTO,
     * or with status {@code 400 (Bad Request)} if the compliancePolicyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compliancePolicyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compliancePolicyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CompliancePolicyDTO>> partialUpdateCompliancePolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompliancePolicyDTO compliancePolicyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompliancePolicy partially : {}, {}", id, compliancePolicyDTO);
        if (compliancePolicyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compliancePolicyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return compliancePolicyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CompliancePolicyDTO> result = compliancePolicyService.partialUpdate(compliancePolicyDTO);

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
     * {@code GET  /compliance-policies} : get all the compliancePolicies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compliancePolicies in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CompliancePolicyDTO>> getAllCompliancePolicies() {
        log.debug("REST request to get all CompliancePolicies");
        return compliancePolicyService.findAll().collectList();
    }

    /**
     * {@code GET  /compliance-policies} : get all the compliancePolicies as a stream.
     * @return the {@link Flux} of compliancePolicies.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CompliancePolicyDTO> getAllCompliancePoliciesAsStream() {
        log.debug("REST request to get all CompliancePolicies as a stream");
        return compliancePolicyService.findAll();
    }

    /**
     * {@code GET  /compliance-policies/:id} : get the "id" compliancePolicy.
     *
     * @param id the id of the compliancePolicyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compliancePolicyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CompliancePolicyDTO>> getCompliancePolicy(@PathVariable("id") Long id) {
        log.debug("REST request to get CompliancePolicy : {}", id);
        Mono<CompliancePolicyDTO> compliancePolicyDTO = compliancePolicyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compliancePolicyDTO);
    }

    /**
     * {@code DELETE  /compliance-policies/:id} : delete the "id" compliancePolicy.
     *
     * @param id the id of the compliancePolicyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCompliancePolicy(@PathVariable("id") Long id) {
        log.debug("REST request to delete CompliancePolicy : {}", id);
        return compliancePolicyService
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
