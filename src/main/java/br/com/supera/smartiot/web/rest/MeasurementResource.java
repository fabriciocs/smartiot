package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.MeasurementRepository;
import br.com.supera.smartiot.service.MeasurementService;
import br.com.supera.smartiot.service.dto.MeasurementDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.Measurement}.
 */
@RestController
@RequestMapping("/api/measurements")
public class MeasurementResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementResource.class);

    private static final String ENTITY_NAME = "measurement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasurementService measurementService;

    private final MeasurementRepository measurementRepository;

    public MeasurementResource(MeasurementService measurementService, MeasurementRepository measurementRepository) {
        this.measurementService = measurementService;
        this.measurementRepository = measurementRepository;
    }

    /**
     * {@code POST  /measurements} : Create a new measurement.
     *
     * @param measurementDTO the measurementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measurementDTO, or with status {@code 400 (Bad Request)} if the measurement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MeasurementDTO> createMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO) throws URISyntaxException {
        log.debug("REST request to save Measurement : {}", measurementDTO);
        if (measurementDTO.getId() != null) {
            throw new BadRequestAlertException("A new measurement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        measurementDTO = measurementService.save(measurementDTO);
        return ResponseEntity.created(new URI("/api/measurements/" + measurementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, measurementDTO.getId().toString()))
            .body(measurementDTO);
    }

    /**
     * {@code PUT  /measurements/:id} : Updates an existing measurement.
     *
     * @param id the id of the measurementDTO to save.
     * @param measurementDTO the measurementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measurementDTO,
     * or with status {@code 400 (Bad Request)} if the measurementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measurementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MeasurementDTO> updateMeasurement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MeasurementDTO measurementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Measurement : {}, {}", id, measurementDTO);
        if (measurementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measurementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measurementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        measurementDTO = measurementService.update(measurementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measurementDTO.getId().toString()))
            .body(measurementDTO);
    }

    /**
     * {@code PATCH  /measurements/:id} : Partial updates given fields of an existing measurement, field will ignore if it is null
     *
     * @param id the id of the measurementDTO to save.
     * @param measurementDTO the measurementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measurementDTO,
     * or with status {@code 400 (Bad Request)} if the measurementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the measurementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the measurementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MeasurementDTO> partialUpdateMeasurement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MeasurementDTO measurementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Measurement partially : {}, {}", id, measurementDTO);
        if (measurementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measurementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measurementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeasurementDTO> result = measurementService.partialUpdate(measurementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measurementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /measurements} : get all the measurements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measurements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MeasurementDTO>> getAllMeasurements(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Measurements");
        Page<MeasurementDTO> page = measurementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /measurements/:id} : get the "id" measurement.
     *
     * @param id the id of the measurementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measurementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MeasurementDTO> getMeasurement(@PathVariable("id") Long id) {
        log.debug("REST request to get Measurement : {}", id);
        Optional<MeasurementDTO> measurementDTO = measurementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(measurementDTO);
    }

    /**
     * {@code DELETE  /measurements/:id} : delete the "id" measurement.
     *
     * @param id the id of the measurementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable("id") Long id) {
        log.debug("REST request to delete Measurement : {}", id);
        measurementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
