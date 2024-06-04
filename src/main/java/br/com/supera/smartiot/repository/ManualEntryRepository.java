package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.ManualEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ManualEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualEntryRepository extends JpaRepository<ManualEntry, Long> {}
