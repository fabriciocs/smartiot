package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.DadoSensor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DadoSensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DadoSensorRepository extends JpaRepository<DadoSensor, Long> {}
