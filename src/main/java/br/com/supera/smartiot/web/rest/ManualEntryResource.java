package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.ManualEntryRepository;
import br.com.supera.smartiot.service.ManualEntryService;
import br.com.supera.smartiot.service.dto.ManualEntryDTO;
import br.com.supera.smartiot.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.supera.smartiot.domain.ManualEntry}.
 */
@RestController
@RequestMapping("/api/manual-entries")
public class ManualEntryResource {

    private final Logger log = LoggerFactory.getLogger(ManualEntryResource.class);

    private static final String ENTITY_NAME = "manualEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManualEntryService manualEntryService;

    private final ManualEntryRepository manualEntryRepository;

    public ManualEntryResource(ManualEntryService manualEntryService, ManualEntryRepository manualEntryRepository) {
        this.manualEntryService = manualEntryService;
        this.manualEntryRepository = manualEntryRepository;
    }

    /**
     * {@code POST  /manual-entries} : Create a new manualEntry.
     *
     * @param manualEntryDTO the manualEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manualEntryDTO, or with status {@code 400 (Bad Request)} if the manualEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ManualEntryDTO> createManualEntry(@Valid @RequestBody ManualEntryDTO manualEntryDTO) throws URISyntaxException {
        log.debug("REST request to save ManualEntry : {}", manualEntryDTO);
        if (manualEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        manualEntryDTO = manualEntryService.save(manualEntryDTO);
        return ResponseEntity.created(new URI("/api/manual-entries/" + manualEntryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, manualEntryDTO.getId().toString()))
            .body(manualEntryDTO);
    }

    /**
     * {@code PUT  /manual-entries/:id} : Updates an existing manualEntry.
     *
     * @param id the id of the manualEntryDTO to save.
     * @param manualEntryDTO the manualEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualEntryDTO,
     * or with status {@code 400 (Bad Request)} if the manualEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manualEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ManualEntryDTO> updateManualEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ManualEntryDTO manualEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ManualEntry : {}, {}", id, manualEntryDTO);
        if (manualEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        manualEntryDTO = manualEntryService.update(manualEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualEntryDTO.getId().toString()))
            .body(manualEntryDTO);
    }

    /**
     * {@code PATCH  /manual-entries/:id} : Partial updates given fields of an existing manualEntry, field will ignore if it is null
     *
     * @param id the id of the manualEntryDTO to save.
     * @param manualEntryDTO the manualEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualEntryDTO,
     * or with status {@code 400 (Bad Request)} if the manualEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the manualEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the manualEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManualEntryDTO> partialUpdateManualEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManualEntryDTO manualEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ManualEntry partially : {}, {}", id, manualEntryDTO);
        if (manualEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManualEntryDTO> result = manualEntryService.partialUpdate(manualEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /manual-entries} : get all the manualEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manualEntries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ManualEntryDTO>> getAllManualEntries(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ManualEntries");
        Page<ManualEntryDTO> page = manualEntryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manual-entries/:id} : get the "id" manualEntry.
     *
     * @param id the id of the manualEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manualEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManualEntryDTO> getManualEntry(@PathVariable("id") Long id) {
        log.debug("REST request to get ManualEntry : {}", id);
        Optional<ManualEntryDTO> manualEntryDTO = manualEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manualEntryDTO);
    }

    /**
     * {@code DELETE  /manual-entries/:id} : delete the "id" manualEntry.
     *
     * @param id the id of the manualEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManualEntry(@PathVariable("id") Long id) {
        log.debug("REST request to delete ManualEntry : {}", id);
        manualEntryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
