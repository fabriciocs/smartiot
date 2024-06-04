package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.Concentrator;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Concentrator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConcentratorRepository extends JpaRepository<Concentrator, Long> {}
