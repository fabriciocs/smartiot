package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.Transmitter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transmitter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransmitterRepository extends JpaRepository<Transmitter, Long> {}
