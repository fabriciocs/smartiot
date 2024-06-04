package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.Enrollment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Enrollment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {}
