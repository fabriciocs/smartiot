package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.Measurement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Measurement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {}
