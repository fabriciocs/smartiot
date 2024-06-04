package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.Consumer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Consumer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {}
