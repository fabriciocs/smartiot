package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.ConcentratorRepository;
import br.com.supera.smartiot.service.ConcentratorService;
import br.com.supera.smartiot.service.dto.ConcentratorDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.Concentrator}.
 */
@RestController
@RequestMapping("/api/concentrators")
public class ConcentratorResource {

    private final Logger log = LoggerFactory.getLogger(ConcentratorResource.class);

    private static final String ENTITY_NAME = "concentrator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConcentratorService concentratorService;

    private final ConcentratorRepository concentratorRepository;

    public ConcentratorResource(ConcentratorService concentratorService, ConcentratorRepository concentratorRepository) {
        this.concentratorService = concentratorService;
        this.concentratorRepository = concentratorRepository;
    }

    /**
     * {@code POST  /concentrators} : Create a new concentrator.
     *
     * @param concentratorDTO the concentratorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new concentratorDTO, or with status {@code 400 (Bad Request)} if the concentrator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConcentratorDTO> createConcentrator(@Valid @RequestBody ConcentratorDTO concentratorDTO)
        throws URISyntaxException {
        log.debug("REST request to save Concentrator : {}", concentratorDTO);
        if (concentratorDTO.getId() != null) {
            throw new BadRequestAlertException("A new concentrator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        concentratorDTO = concentratorService.save(concentratorDTO);
        return ResponseEntity.created(new URI("/api/concentrators/" + concentratorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, concentratorDTO.getId().toString()))
            .body(concentratorDTO);
    }

    /**
     * {@code PUT  /concentrators/:id} : Updates an existing concentrator.
     *
     * @param id the id of the concentratorDTO to save.
     * @param concentratorDTO the concentratorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated concentratorDTO,
     * or with status {@code 400 (Bad Request)} if the concentratorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the concentratorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConcentratorDTO> updateConcentrator(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConcentratorDTO concentratorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Concentrator : {}, {}", id, concentratorDTO);
        if (concentratorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, concentratorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!concentratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        concentratorDTO = concentratorService.update(concentratorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, concentratorDTO.getId().toString()))
            .body(concentratorDTO);
    }

    /**
     * {@code PATCH  /concentrators/:id} : Partial updates given fields of an existing concentrator, field will ignore if it is null
     *
     * @param id the id of the concentratorDTO to save.
     * @param concentratorDTO the concentratorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated concentratorDTO,
     * or with status {@code 400 (Bad Request)} if the concentratorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the concentratorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the concentratorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConcentratorDTO> partialUpdateConcentrator(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConcentratorDTO concentratorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Concentrator partially : {}, {}", id, concentratorDTO);
        if (concentratorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, concentratorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!concentratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConcentratorDTO> result = concentratorService.partialUpdate(concentratorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, concentratorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /concentrators} : get all the concentrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of concentrators in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConcentratorDTO>> getAllConcentrators(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Concentrators");
        Page<ConcentratorDTO> page = concentratorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /concentrators/:id} : get the "id" concentrator.
     *
     * @param id the id of the concentratorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the concentratorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConcentratorDTO> getConcentrator(@PathVariable("id") Long id) {
        log.debug("REST request to get Concentrator : {}", id);
        Optional<ConcentratorDTO> concentratorDTO = concentratorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(concentratorDTO);
    }

    /**
     * {@code DELETE  /concentrators/:id} : delete the "id" concentrator.
     *
     * @param id the id of the concentratorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcentrator(@PathVariable("id") Long id) {
        log.debug("REST request to delete Concentrator : {}", id);
        concentratorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
