package br.com.supera.feedback360.web.rest;

import br.com.supera.feedback360.repository.FeedbackResponseRepository;
import br.com.supera.feedback360.service.FeedbackResponseService;
import br.com.supera.feedback360.service.dto.FeedbackResponseDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link br.com.supera.feedback360.domain.FeedbackResponse}.
 */
@RestController
@RequestMapping("/api/feedback-responses")
public class FeedbackResponseResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackResponseResource.class);

    private static final String ENTITY_NAME = "feedbackResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackResponseService feedbackResponseService;

    private final FeedbackResponseRepository feedbackResponseRepository;

    public FeedbackResponseResource(
        FeedbackResponseService feedbackResponseService,
        FeedbackResponseRepository feedbackResponseRepository
    ) {
        this.feedbackResponseService = feedbackResponseService;
        this.feedbackResponseRepository = feedbackResponseRepository;
    }

    /**
     * {@code POST  /feedback-responses} : Create a new feedbackResponse.
     *
     * @param feedbackResponseDTO the feedbackResponseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackResponseDTO, or with status {@code 400 (Bad Request)} if the feedbackResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FeedbackResponseDTO>> createFeedbackResponse(@Valid @RequestBody FeedbackResponseDTO feedbackResponseDTO)
        throws URISyntaxException {
        log.debug("REST request to save FeedbackResponse : {}", feedbackResponseDTO);
        if (feedbackResponseDTO.getId() != null) {
            throw new BadRequestAlertException("A new feedbackResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return feedbackResponseService
            .save(feedbackResponseDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/feedback-responses/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /feedback-responses/:id} : Updates an existing feedbackResponse.
     *
     * @param id the id of the feedbackResponseDTO to save.
     * @param feedbackResponseDTO the feedbackResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackResponseDTO,
     * or with status {@code 400 (Bad Request)} if the feedbackResponseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackResponseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FeedbackResponseDTO>> updateFeedbackResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeedbackResponseDTO feedbackResponseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackResponse : {}, {}", id, feedbackResponseDTO);
        if (feedbackResponseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackResponseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return feedbackResponseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return feedbackResponseService
                    .update(feedbackResponseDTO)
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
     * {@code PATCH  /feedback-responses/:id} : Partial updates given fields of an existing feedbackResponse, field will ignore if it is null
     *
     * @param id the id of the feedbackResponseDTO to save.
     * @param feedbackResponseDTO the feedbackResponseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackResponseDTO,
     * or with status {@code 400 (Bad Request)} if the feedbackResponseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackResponseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackResponseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FeedbackResponseDTO>> partialUpdateFeedbackResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeedbackResponseDTO feedbackResponseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackResponse partially : {}, {}", id, feedbackResponseDTO);
        if (feedbackResponseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackResponseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return feedbackResponseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FeedbackResponseDTO> result = feedbackResponseService.partialUpdate(feedbackResponseDTO);

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
     * {@code GET  /feedback-responses} : get all the feedbackResponses.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackResponses in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FeedbackResponseDTO>>> getAllFeedbackResponses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of FeedbackResponses");
        return feedbackResponseService
            .countAll()
            .zipWith(feedbackResponseService.findAll(pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /feedback-responses/:id} : get the "id" feedbackResponse.
     *
     * @param id the id of the feedbackResponseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackResponseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FeedbackResponseDTO>> getFeedbackResponse(@PathVariable("id") Long id) {
        log.debug("REST request to get FeedbackResponse : {}", id);
        Mono<FeedbackResponseDTO> feedbackResponseDTO = feedbackResponseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackResponseDTO);
    }

    /**
     * {@code DELETE  /feedback-responses/:id} : delete the "id" feedbackResponse.
     *
     * @param id the id of the feedbackResponseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFeedbackResponse(@PathVariable("id") Long id) {
        log.debug("REST request to delete FeedbackResponse : {}", id);
        return feedbackResponseService
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
