package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.Pricing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pricing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {}
