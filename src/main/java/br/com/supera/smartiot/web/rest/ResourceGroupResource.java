package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.ResourceGroupRepository;
import br.com.supera.smartiot.service.ResourceGroupService;
import br.com.supera.smartiot.service.dto.ResourceGroupDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.ResourceGroup}.
 */
@RestController
@RequestMapping("/api/resource-groups")
public class ResourceGroupResource {

    private final Logger log = LoggerFactory.getLogger(ResourceGroupResource.class);

    private static final String ENTITY_NAME = "resourceGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceGroupService resourceGroupService;

    private final ResourceGroupRepository resourceGroupRepository;

    public ResourceGroupResource(ResourceGroupService resourceGroupService, ResourceGroupRepository resourceGroupRepository) {
        this.resourceGroupService = resourceGroupService;
        this.resourceGroupRepository = resourceGroupRepository;
    }

    /**
     * {@code POST  /resource-groups} : Create a new resourceGroup.
     *
     * @param resourceGroupDTO the resourceGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceGroupDTO, or with status {@code 400 (Bad Request)} if the resourceGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ResourceGroupDTO> createResourceGroup(@Valid @RequestBody ResourceGroupDTO resourceGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResourceGroup : {}", resourceGroupDTO);
        if (resourceGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        resourceGroupDTO = resourceGroupService.save(resourceGroupDTO);
        return ResponseEntity.created(new URI("/api/resource-groups/" + resourceGroupDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, resourceGroupDTO.getId().toString()))
            .body(resourceGroupDTO);
    }

    /**
     * {@code PUT  /resource-groups/:id} : Updates an existing resourceGroup.
     *
     * @param id the id of the resourceGroupDTO to save.
     * @param resourceGroupDTO the resourceGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceGroupDTO,
     * or with status {@code 400 (Bad Request)} if the resourceGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResourceGroupDTO> updateResourceGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourceGroupDTO resourceGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceGroup : {}, {}", id, resourceGroupDTO);
        if (resourceGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        resourceGroupDTO = resourceGroupService.update(resourceGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceGroupDTO.getId().toString()))
            .body(resourceGroupDTO);
    }

    /**
     * {@code PATCH  /resource-groups/:id} : Partial updates given fields of an existing resourceGroup, field will ignore if it is null
     *
     * @param id the id of the resourceGroupDTO to save.
     * @param resourceGroupDTO the resourceGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceGroupDTO,
     * or with status {@code 400 (Bad Request)} if the resourceGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResourceGroupDTO> partialUpdateResourceGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourceGroupDTO resourceGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourceGroup partially : {}, {}", id, resourceGroupDTO);
        if (resourceGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResourceGroupDTO> result = resourceGroupService.partialUpdate(resourceGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resource-groups} : get all the resourceGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ResourceGroupDTO>> getAllResourceGroups(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ResourceGroups");
        Page<ResourceGroupDTO> page = resourceGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resource-groups/:id} : get the "id" resourceGroup.
     *
     * @param id the id of the resourceGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResourceGroupDTO> getResourceGroup(@PathVariable("id") Long id) {
        log.debug("REST request to get ResourceGroup : {}", id);
        Optional<ResourceGroupDTO> resourceGroupDTO = resourceGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceGroupDTO);
    }

    /**
     * {@code DELETE  /resource-groups/:id} : delete the "id" resourceGroup.
     *
     * @param id the id of the resourceGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceGroup(@PathVariable("id") Long id) {
        log.debug("REST request to delete ResourceGroup : {}", id);
        resourceGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
