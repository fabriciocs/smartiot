package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.TransmitterRepository;
import br.com.supera.smartiot.service.TransmitterService;
import br.com.supera.smartiot.service.dto.TransmitterDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.Transmitter}.
 */
@RestController
@RequestMapping("/api/transmitters")
public class TransmitterResource {

    private final Logger log = LoggerFactory.getLogger(TransmitterResource.class);

    private static final String ENTITY_NAME = "transmitter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransmitterService transmitterService;

    private final TransmitterRepository transmitterRepository;

    public TransmitterResource(TransmitterService transmitterService, TransmitterRepository transmitterRepository) {
        this.transmitterService = transmitterService;
        this.transmitterRepository = transmitterRepository;
    }

    /**
     * {@code POST  /transmitters} : Create a new transmitter.
     *
     * @param transmitterDTO the transmitterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transmitterDTO, or with status {@code 400 (Bad Request)} if the transmitter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransmitterDTO> createTransmitter(@Valid @RequestBody TransmitterDTO transmitterDTO) throws URISyntaxException {
        log.debug("REST request to save Transmitter : {}", transmitterDTO);
        if (transmitterDTO.getId() != null) {
            throw new BadRequestAlertException("A new transmitter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transmitterDTO = transmitterService.save(transmitterDTO);
        return ResponseEntity.created(new URI("/api/transmitters/" + transmitterDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transmitterDTO.getId().toString()))
            .body(transmitterDTO);
    }

    /**
     * {@code PUT  /transmitters/:id} : Updates an existing transmitter.
     *
     * @param id the id of the transmitterDTO to save.
     * @param transmitterDTO the transmitterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transmitterDTO,
     * or with status {@code 400 (Bad Request)} if the transmitterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transmitterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransmitterDTO> updateTransmitter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransmitterDTO transmitterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transmitter : {}, {}", id, transmitterDTO);
        if (transmitterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transmitterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transmitterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transmitterDTO = transmitterService.update(transmitterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transmitterDTO.getId().toString()))
            .body(transmitterDTO);
    }

    /**
     * {@code PATCH  /transmitters/:id} : Partial updates given fields of an existing transmitter, field will ignore if it is null
     *
     * @param id the id of the transmitterDTO to save.
     * @param transmitterDTO the transmitterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transmitterDTO,
     * or with status {@code 400 (Bad Request)} if the transmitterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transmitterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transmitterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransmitterDTO> partialUpdateTransmitter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransmitterDTO transmitterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transmitter partially : {}, {}", id, transmitterDTO);
        if (transmitterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transmitterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transmitterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransmitterDTO> result = transmitterService.partialUpdate(transmitterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transmitterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transmitters} : get all the transmitters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transmitters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransmitterDTO>> getAllTransmitters(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Transmitters");
        Page<TransmitterDTO> page = transmitterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transmitters/:id} : get the "id" transmitter.
     *
     * @param id the id of the transmitterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transmitterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransmitterDTO> getTransmitter(@PathVariable("id") Long id) {
        log.debug("REST request to get Transmitter : {}", id);
        Optional<TransmitterDTO> transmitterDTO = transmitterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transmitterDTO);
    }

    /**
     * {@code DELETE  /transmitters/:id} : delete the "id" transmitter.
     *
     * @param id the id of the transmitterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransmitter(@PathVariable("id") Long id) {
        log.debug("REST request to delete Transmitter : {}", id);
        transmitterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
