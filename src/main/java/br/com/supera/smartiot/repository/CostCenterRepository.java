package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.CostCenter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CostCenter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Long> {}
