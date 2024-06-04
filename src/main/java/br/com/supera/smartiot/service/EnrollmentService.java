package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.Enrollment;
import br.com.supera.smartiot.repository.EnrollmentRepository;
import br.com.supera.smartiot.service.dto.EnrollmentDTO;
import br.com.supera.smartiot.service.mapper.EnrollmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.Enrollment}.
 */
@Service
@Transactional
public class EnrollmentService {

    private final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    private final EnrollmentRepository enrollmentRepository;

    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    /**
     * Save a enrollment.
     *
     * @param enrollmentDTO the entity to save.
     * @return the persisted entity.
     */
    public EnrollmentDTO save(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to save Enrollment : {}", enrollmentDTO);
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        enrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    /**
     * Update a enrollment.
     *
     * @param enrollmentDTO the entity to save.
     * @return the persisted entity.
     */
    public EnrollmentDTO update(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to update Enrollment : {}", enrollmentDTO);
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        enrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    /**
     * Partially update a enrollment.
     *
     * @param enrollmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EnrollmentDTO> partialUpdate(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to partially update Enrollment : {}", enrollmentDTO);

        return enrollmentRepository
            .findById(enrollmentDTO.getId())
            .map(existingEnrollment -> {
                enrollmentMapper.partialUpdate(existingEnrollment, enrollmentDTO);

                return existingEnrollment;
            })
            .map(enrollmentRepository::save)
            .map(enrollmentMapper::toDto);
    }

    /**
     * Get all the enrollments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enrollments");
        return enrollmentRepository.findAll(pageable).map(enrollmentMapper::toDto);
    }

    /**
     * Get one enrollment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EnrollmentDTO> findOne(Long id) {
        log.debug("Request to get Enrollment : {}", id);
        return enrollmentRepository.findById(id).map(enrollmentMapper::toDto);
    }

    /**
     * Delete the enrollment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Enrollment : {}", id);
        enrollmentRepository.deleteById(id);
    }
}
