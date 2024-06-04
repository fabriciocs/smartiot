package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.AggregatedData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AggregatedData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AggregatedDataRepository extends JpaRepository<AggregatedData, Long> {}
