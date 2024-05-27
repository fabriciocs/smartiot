package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.FeedbackFormRepository;
import br.com.supera.feedback360.service.FeedbackFormService;
import br.com.supera.feedback360.service.dto.FeedbackFormDTO;
import br.com.supera.feedback360.service.mapper.FeedbackFormMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.FeedbackForm}.
 */
@Service
@Transactional
public class FeedbackFormServiceImpl implements FeedbackFormService {

    private final Logger log = LoggerFactory.getLogger(FeedbackFormServiceImpl.class);

    private final FeedbackFormRepository feedbackFormRepository;

    private final FeedbackFormMapper feedbackFormMapper;

    public FeedbackFormServiceImpl(FeedbackFormRepository feedbackFormRepository, FeedbackFormMapper feedbackFormMapper) {
        this.feedbackFormRepository = feedbackFormRepository;
        this.feedbackFormMapper = feedbackFormMapper;
    }

    @Override
    public Mono<FeedbackFormDTO> save(FeedbackFormDTO feedbackFormDTO) {
        log.debug("Request to save FeedbackForm : {}", feedbackFormDTO);
        return feedbackFormRepository.save(feedbackFormMapper.toEntity(feedbackFormDTO)).map(feedbackFormMapper::toDto);
    }

    @Override
    public Mono<FeedbackFormDTO> update(FeedbackFormDTO feedbackFormDTO) {
        log.debug("Request to update FeedbackForm : {}", feedbackFormDTO);
        return feedbackFormRepository.save(feedbackFormMapper.toEntity(feedbackFormDTO)).map(feedbackFormMapper::toDto);
    }

    @Override
    public Mono<FeedbackFormDTO> partialUpdate(FeedbackFormDTO feedbackFormDTO) {
        log.debug("Request to partially update FeedbackForm : {}", feedbackFormDTO);

        return feedbackFormRepository
            .findById(feedbackFormDTO.getId())
            .map(existingFeedbackForm -> {
                feedbackFormMapper.partialUpdate(existingFeedbackForm, feedbackFormDTO);

                return existingFeedbackForm;
            })
            .flatMap(feedbackFormRepository::save)
            .map(feedbackFormMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FeedbackFormDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FeedbackForms");
        return feedbackFormRepository.findAllBy(pageable).map(feedbackFormMapper::toDto);
    }

    public Flux<FeedbackFormDTO> findAllWithEagerRelationships(Pageable pageable) {
        return feedbackFormRepository.findAllWithEagerRelationships(pageable).map(feedbackFormMapper::toDto);
    }

    public Mono<Long> countAll() {
        return feedbackFormRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FeedbackFormDTO> findOne(Long id) {
        log.debug("Request to get FeedbackForm : {}", id);
        return feedbackFormRepository.findOneWithEagerRelationships(id).map(feedbackFormMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FeedbackForm : {}", id);
        return feedbackFormRepository.deleteById(id);
    }
}
