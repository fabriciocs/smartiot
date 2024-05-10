package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.DadoSensorRepository;
import br.com.supera.smartiot.service.DadoSensorService;
import br.com.supera.smartiot.service.dto.DadoSensorDTO;
import br.com.supera.smartiot.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.DadoSensor}.
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
    public ResponseEntity<DadoSensorDTO> createDadoSensor(@RequestBody DadoSensorDTO dadoSensorDTO) throws URISyntaxException {
        log.debug("REST request to save DadoSensor : {}", dadoSensorDTO);
        if (dadoSensorDTO.getId() != null) {
            throw new BadRequestAlertException("A new dadoSensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dadoSensorDTO = dadoSensorService.save(dadoSensorDTO);
        return ResponseEntity.created(new URI("/api/dado-sensors/" + dadoSensorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dadoSensorDTO.getId().toString()))
            .body(dadoSensorDTO);
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
    public ResponseEntity<DadoSensorDTO> updateDadoSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DadoSensorDTO dadoSensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DadoSensor : {}, {}", id, dadoSensorDTO);
        if (dadoSensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dadoSensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dadoSensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dadoSensorDTO = dadoSensorService.update(dadoSensorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dadoSensorDTO.getId().toString()))
            .body(dadoSensorDTO);
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
    public ResponseEntity<DadoSensorDTO> partialUpdateDadoSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DadoSensorDTO dadoSensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DadoSensor partially : {}, {}", id, dadoSensorDTO);
        if (dadoSensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dadoSensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dadoSensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DadoSensorDTO> result = dadoSensorService.partialUpdate(dadoSensorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dadoSensorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dado-sensors} : get all the dadoSensors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dadoSensors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DadoSensorDTO>> getAllDadoSensors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DadoSensors");
        Page<DadoSensorDTO> page = dadoSensorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dado-sensors/:id} : get the "id" dadoSensor.
     *
     * @param id the id of the dadoSensorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dadoSensorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DadoSensorDTO> getDadoSensor(@PathVariable("id") Long id) {
        log.debug("REST request to get DadoSensor : {}", id);
        Optional<DadoSensorDTO> dadoSensorDTO = dadoSensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dadoSensorDTO);
    }

    /**
     * {@code DELETE  /dado-sensors/:id} : delete the "id" dadoSensor.
     *
     * @param id the id of the dadoSensorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDadoSensor(@PathVariable("id") Long id) {
        log.debug("REST request to delete DadoSensor : {}", id);
        dadoSensorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
