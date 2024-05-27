package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.ReportRepository;
import br.com.supera.feedback360.service.ReportService;
import br.com.supera.feedback360.service.dto.ReportDTO;
import br.com.supera.feedback360.service.mapper.ReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.Report}.
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    public ReportServiceImpl(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    public Mono<ReportDTO> save(ReportDTO reportDTO) {
        log.debug("Request to save Report : {}", reportDTO);
        return reportRepository.save(reportMapper.toEntity(reportDTO)).map(reportMapper::toDto);
    }

    @Override
    public Mono<ReportDTO> update(ReportDTO reportDTO) {
        log.debug("Request to update Report : {}", reportDTO);
        return reportRepository.save(reportMapper.toEntity(reportDTO)).map(reportMapper::toDto);
    }

    @Override
    public Mono<ReportDTO> partialUpdate(ReportDTO reportDTO) {
        log.debug("Request to partially update Report : {}", reportDTO);

        return reportRepository
            .findById(reportDTO.getId())
            .map(existingReport -> {
                reportMapper.partialUpdate(existingReport, reportDTO);

                return existingReport;
            })
            .flatMap(reportRepository::save)
            .map(reportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        return reportRepository.findAllBy(pageable).map(reportMapper::toDto);
    }

    public Flux<ReportDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reportRepository.findAllWithEagerRelationships(pageable).map(reportMapper::toDto);
    }

    public Mono<Long> countAll() {
        return reportRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ReportDTO> findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findOneWithEagerRelationships(id).map(reportMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        return reportRepository.deleteById(id);
    }
}
