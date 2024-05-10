package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.SensorRepository;
import br.com.supera.smartiot.service.SensorService;
import br.com.supera.smartiot.service.dto.SensorDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.Sensor}.
 */
@RestController
@RequestMapping("/api/sensors")
public class SensorResource {

    private final Logger log = LoggerFactory.getLogger(SensorResource.class);

    private static final String ENTITY_NAME = "sensor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SensorService sensorService;

    private final SensorRepository sensorRepository;

    public SensorResource(SensorService sensorService, SensorRepository sensorRepository) {
        this.sensorService = sensorService;
        this.sensorRepository = sensorRepository;
    }

    /**
     * {@code POST  /sensors} : Create a new sensor.
     *
     * @param sensorDTO the sensorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensorDTO, or with status {@code 400 (Bad Request)} if the sensor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SensorDTO> createSensor(@Valid @RequestBody SensorDTO sensorDTO) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensorDTO);
        if (sensorDTO.getId() != null) {
            throw new BadRequestAlertException("A new sensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sensorDTO = sensorService.save(sensorDTO);
        return ResponseEntity.created(new URI("/api/sensors/" + sensorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sensorDTO.getId().toString()))
            .body(sensorDTO);
    }

    /**
     * {@code PUT  /sensors/:id} : Updates an existing sensor.
     *
     * @param id the id of the sensorDTO to save.
     * @param sensorDTO the sensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorDTO,
     * or with status {@code 400 (Bad Request)} if the sensorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SensorDTO> updateSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SensorDTO sensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sensor : {}, {}", id, sensorDTO);
        if (sensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sensorDTO = sensorService.update(sensorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensorDTO.getId().toString()))
            .body(sensorDTO);
    }

    /**
     * {@code PATCH  /sensors/:id} : Partial updates given fields of an existing sensor, field will ignore if it is null
     *
     * @param id the id of the sensorDTO to save.
     * @param sensorDTO the sensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorDTO,
     * or with status {@code 400 (Bad Request)} if the sensorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sensorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sensorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SensorDTO> partialUpdateSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SensorDTO sensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sensor partially : {}, {}", id, sensorDTO);
        if (sensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SensorDTO> result = sensorService.partialUpdate(sensorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sensors} : get all the sensors.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SensorDTO>> getAllSensors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Sensors");
        Page<SensorDTO> page;
        if (eagerload) {
            page = sensorService.findAllWithEagerRelationships(pageable);
        } else {
            page = sensorService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sensors/:id} : get the "id" sensor.
     *
     * @param id the id of the sensorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> getSensor(@PathVariable("id") Long id) {
        log.debug("REST request to get Sensor : {}", id);
        Optional<SensorDTO> sensorDTO = sensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensorDTO);
    }

    /**
     * {@code DELETE  /sensors/:id} : delete the "id" sensor.
     *
     * @param id the id of the sensorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable("id") Long id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
