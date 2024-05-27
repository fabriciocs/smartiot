package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.FeedbackResponseRepository;
import br.com.supera.feedback360.service.FeedbackResponseService;
import br.com.supera.feedback360.service.dto.FeedbackResponseDTO;
import br.com.supera.feedback360.service.mapper.FeedbackResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.FeedbackResponse}.
 */
@Service
@Transactional
public class FeedbackResponseServiceImpl implements FeedbackResponseService {

    private final Logger log = LoggerFactory.getLogger(FeedbackResponseServiceImpl.class);

    private final FeedbackResponseRepository feedbackResponseRepository;

    private final FeedbackResponseMapper feedbackResponseMapper;

    public FeedbackResponseServiceImpl(
        FeedbackResponseRepository feedbackResponseRepository,
        FeedbackResponseMapper feedbackResponseMapper
    ) {
        this.feedbackResponseRepository = feedbackResponseRepository;
        this.feedbackResponseMapper = feedbackResponseMapper;
    }

    @Override
    public Mono<FeedbackResponseDTO> save(FeedbackResponseDTO feedbackResponseDTO) {
        log.debug("Request to save FeedbackResponse : {}", feedbackResponseDTO);
        return feedbackResponseRepository.save(feedbackResponseMapper.toEntity(feedbackResponseDTO)).map(feedbackResponseMapper::toDto);
    }

    @Override
    public Mono<FeedbackResponseDTO> update(FeedbackResponseDTO feedbackResponseDTO) {
        log.debug("Request to update FeedbackResponse : {}", feedbackResponseDTO);
        return feedbackResponseRepository.save(feedbackResponseMapper.toEntity(feedbackResponseDTO)).map(feedbackResponseMapper::toDto);
    }

    @Override
    public Mono<FeedbackResponseDTO> partialUpdate(FeedbackResponseDTO feedbackResponseDTO) {
        log.debug("Request to partially update FeedbackResponse : {}", feedbackResponseDTO);

        return feedbackResponseRepository
            .findById(feedbackResponseDTO.getId())
            .map(existingFeedbackResponse -> {
                feedbackResponseMapper.partialUpdate(existingFeedbackResponse, feedbackResponseDTO);

                return existingFeedbackResponse;
            })
            .flatMap(feedbackResponseRepository::save)
            .map(feedbackResponseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FeedbackResponseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FeedbackResponses");
        return feedbackResponseRepository.findAllBy(pageable).map(feedbackResponseMapper::toDto);
    }

    public Flux<FeedbackResponseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return feedbackResponseRepository.findAllWithEagerRelationships(pageable).map(feedbackResponseMapper::toDto);
    }

    public Mono<Long> countAll() {
        return feedbackResponseRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FeedbackResponseDTO> findOne(Long id) {
        log.debug("Request to get FeedbackResponse : {}", id);
        return feedbackResponseRepository.findOneWithEagerRelationships(id).map(feedbackResponseMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FeedbackResponse : {}", id);
        return feedbackResponseRepository.deleteById(id);
    }
}
