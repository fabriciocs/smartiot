package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.Repeater;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Repeater entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepeaterRepository extends JpaRepository<Repeater, Long> {}
