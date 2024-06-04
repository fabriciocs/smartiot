package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.RepeaterRepository;
import br.com.supera.smartiot.service.RepeaterService;
import br.com.supera.smartiot.service.dto.RepeaterDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.Repeater}.
 */
@RestController
@RequestMapping("/api/repeaters")
public class RepeaterResource {

    private final Logger log = LoggerFactory.getLogger(RepeaterResource.class);

    private static final String ENTITY_NAME = "repeater";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepeaterService repeaterService;

    private final RepeaterRepository repeaterRepository;

    public RepeaterResource(RepeaterService repeaterService, RepeaterRepository repeaterRepository) {
        this.repeaterService = repeaterService;
        this.repeaterRepository = repeaterRepository;
    }

    /**
     * {@code POST  /repeaters} : Create a new repeater.
     *
     * @param repeaterDTO the repeaterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repeaterDTO, or with status {@code 400 (Bad Request)} if the repeater has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RepeaterDTO> createRepeater(@Valid @RequestBody RepeaterDTO repeaterDTO) throws URISyntaxException {
        log.debug("REST request to save Repeater : {}", repeaterDTO);
        if (repeaterDTO.getId() != null) {
            throw new BadRequestAlertException("A new repeater cannot already have an ID", ENTITY_NAME, "idexists");
        }
        repeaterDTO = repeaterService.save(repeaterDTO);
        return ResponseEntity.created(new URI("/api/repeaters/" + repeaterDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, repeaterDTO.getId().toString()))
            .body(repeaterDTO);
    }

    /**
     * {@code PUT  /repeaters/:id} : Updates an existing repeater.
     *
     * @param id the id of the repeaterDTO to save.
     * @param repeaterDTO the repeaterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repeaterDTO,
     * or with status {@code 400 (Bad Request)} if the repeaterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repeaterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RepeaterDTO> updateRepeater(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RepeaterDTO repeaterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Repeater : {}, {}", id, repeaterDTO);
        if (repeaterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repeaterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repeaterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        repeaterDTO = repeaterService.update(repeaterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repeaterDTO.getId().toString()))
            .body(repeaterDTO);
    }

    /**
     * {@code PATCH  /repeaters/:id} : Partial updates given fields of an existing repeater, field will ignore if it is null
     *
     * @param id the id of the repeaterDTO to save.
     * @param repeaterDTO the repeaterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repeaterDTO,
     * or with status {@code 400 (Bad Request)} if the repeaterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the repeaterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the repeaterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RepeaterDTO> partialUpdateRepeater(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RepeaterDTO repeaterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Repeater partially : {}, {}", id, repeaterDTO);
        if (repeaterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repeaterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repeaterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RepeaterDTO> result = repeaterService.partialUpdate(repeaterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repeaterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /repeaters} : get all the repeaters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repeaters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RepeaterDTO>> getAllRepeaters(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Repeaters");
        Page<RepeaterDTO> page = repeaterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /repeaters/:id} : get the "id" repeater.
     *
     * @param id the id of the repeaterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repeaterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RepeaterDTO> getRepeater(@PathVariable("id") Long id) {
        log.debug("REST request to get Repeater : {}", id);
        Optional<RepeaterDTO> repeaterDTO = repeaterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repeaterDTO);
    }

    /**
     * {@code DELETE  /repeaters/:id} : delete the "id" repeater.
     *
     * @param id the id of the repeaterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepeater(@PathVariable("id") Long id) {
        log.debug("REST request to delete Repeater : {}", id);
        repeaterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
