package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.Meter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Meter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {}
