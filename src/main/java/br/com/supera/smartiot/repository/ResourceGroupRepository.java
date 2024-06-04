package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.ResourceGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ResourceGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceGroupRepository extends JpaRepository<ResourceGroup, Long> {}
