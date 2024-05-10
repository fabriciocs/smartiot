package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.ConfiguracaoAlerta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConfiguracaoAlerta entity.
 */
@Repository
public interface ConfiguracaoAlertaRepository extends JpaRepository<ConfiguracaoAlerta, Long> {
    default Optional<ConfiguracaoAlerta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ConfiguracaoAlerta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ConfiguracaoAlerta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select configuracaoAlerta from ConfiguracaoAlerta configuracaoAlerta left join fetch configuracaoAlerta.sensor",
        countQuery = "select count(configuracaoAlerta) from ConfiguracaoAlerta configuracaoAlerta"
    )
    Page<ConfiguracaoAlerta> findAllWithToOneRelationships(Pageable pageable);

    @Query("select configuracaoAlerta from ConfiguracaoAlerta configuracaoAlerta left join fetch configuracaoAlerta.sensor")
    List<ConfiguracaoAlerta> findAllWithToOneRelationships();

    @Query(
        "select configuracaoAlerta from ConfiguracaoAlerta configuracaoAlerta left join fetch configuracaoAlerta.sensor where configuracaoAlerta.id =:id"
    )
    Optional<ConfiguracaoAlerta> findOneWithToOneRelationships(@Param("id") Long id);
}
