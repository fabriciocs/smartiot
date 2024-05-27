package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.QuestionRepository;
import br.com.supera.feedback360.service.QuestionService;
import br.com.supera.feedback360.service.dto.QuestionDTO;
import br.com.supera.feedback360.service.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.Question}.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public Mono<QuestionDTO> save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        return questionRepository.save(questionMapper.toEntity(questionDTO)).map(questionMapper::toDto);
    }

    @Override
    public Mono<QuestionDTO> update(QuestionDTO questionDTO) {
        log.debug("Request to update Question : {}", questionDTO);
        return questionRepository.save(questionMapper.toEntity(questionDTO)).map(questionMapper::toDto);
    }

    @Override
    public Mono<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        log.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(existingQuestion -> {
                questionMapper.partialUpdate(existingQuestion, questionDTO);

                return existingQuestion;
            })
            .flatMap(questionRepository::save)
            .map(questionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<QuestionDTO> findAll() {
        log.debug("Request to get all Questions");
        return questionRepository.findAll().map(questionMapper::toDto);
    }

    public Flux<QuestionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return questionRepository.findAllWithEagerRelationships(pageable).map(questionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return questionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<QuestionDTO> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findOneWithEagerRelationships(id).map(questionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        return questionRepository.deleteById(id);
    }
}
