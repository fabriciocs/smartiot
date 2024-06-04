package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.AggregatedDataRepository;
import br.com.supera.smartiot.service.AggregatedDataService;
import br.com.supera.smartiot.service.dto.AggregatedDataDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.AggregatedData}.
 */
@RestController
@RequestMapping("/api/aggregated-data")
public class AggregatedDataResource {

    private final Logger log = LoggerFactory.getLogger(AggregatedDataResource.class);

    private static final String ENTITY_NAME = "aggregatedData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AggregatedDataService aggregatedDataService;

    private final AggregatedDataRepository aggregatedDataRepository;

    public AggregatedDataResource(AggregatedDataService aggregatedDataService, AggregatedDataRepository aggregatedDataRepository) {
        this.aggregatedDataService = aggregatedDataService;
        this.aggregatedDataRepository = aggregatedDataRepository;
    }

    /**
     * {@code POST  /aggregated-data} : Create a new aggregatedData.
     *
     * @param aggregatedDataDTO the aggregatedDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aggregatedDataDTO, or with status {@code 400 (Bad Request)} if the aggregatedData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AggregatedDataDTO> createAggregatedData(@Valid @RequestBody AggregatedDataDTO aggregatedDataDTO)
        throws URISyntaxException {
        log.debug("REST request to save AggregatedData : {}", aggregatedDataDTO);
        if (aggregatedDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new aggregatedData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aggregatedDataDTO = aggregatedDataService.save(aggregatedDataDTO);
        return ResponseEntity.created(new URI("/api/aggregated-data/" + aggregatedDataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aggregatedDataDTO.getId().toString()))
            .body(aggregatedDataDTO);
    }

    /**
     * {@code PUT  /aggregated-data/:id} : Updates an existing aggregatedData.
     *
     * @param id the id of the aggregatedDataDTO to save.
     * @param aggregatedDataDTO the aggregatedDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aggregatedDataDTO,
     * or with status {@code 400 (Bad Request)} if the aggregatedDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aggregatedDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AggregatedDataDTO> updateAggregatedData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AggregatedDataDTO aggregatedDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AggregatedData : {}, {}", id, aggregatedDataDTO);
        if (aggregatedDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aggregatedDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aggregatedDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aggregatedDataDTO = aggregatedDataService.update(aggregatedDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aggregatedDataDTO.getId().toString()))
            .body(aggregatedDataDTO);
    }

    /**
     * {@code PATCH  /aggregated-data/:id} : Partial updates given fields of an existing aggregatedData, field will ignore if it is null
     *
     * @param id the id of the aggregatedDataDTO to save.
     * @param aggregatedDataDTO the aggregatedDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aggregatedDataDTO,
     * or with status {@code 400 (Bad Request)} if the aggregatedDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aggregatedDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aggregatedDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AggregatedDataDTO> partialUpdateAggregatedData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AggregatedDataDTO aggregatedDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AggregatedData partially : {}, {}", id, aggregatedDataDTO);
        if (aggregatedDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aggregatedDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aggregatedDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AggregatedDataDTO> result = aggregatedDataService.partialUpdate(aggregatedDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aggregatedDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /aggregated-data} : get all the aggregatedData.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aggregatedData in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AggregatedDataDTO>> getAllAggregatedData(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AggregatedData");
        Page<AggregatedDataDTO> page = aggregatedDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aggregated-data/:id} : get the "id" aggregatedData.
     *
     * @param id the id of the aggregatedDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aggregatedDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AggregatedDataDTO> getAggregatedData(@PathVariable("id") Long id) {
        log.debug("REST request to get AggregatedData : {}", id);
        Optional<AggregatedDataDTO> aggregatedDataDTO = aggregatedDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aggregatedDataDTO);
    }

    /**
     * {@code DELETE  /aggregated-data/:id} : delete the "id" aggregatedData.
     *
     * @param id the id of the aggregatedDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAggregatedData(@PathVariable("id") Long id) {
        log.debug("REST request to delete AggregatedData : {}", id);
        aggregatedDataService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
