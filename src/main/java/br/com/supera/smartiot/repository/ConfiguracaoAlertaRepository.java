package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.ConfiguracaoAlerta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConfiguracaoAlerta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfiguracaoAlertaRepository extends JpaRepository<ConfiguracaoAlerta, Long> {}
