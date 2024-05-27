package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.ConfiguracaoAlerta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ConfiguracaoAlerta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfiguracaoAlertaRepository
    extends ReactiveCrudRepository<ConfiguracaoAlerta, Long>, ConfiguracaoAlertaRepositoryInternal {
    Flux<ConfiguracaoAlerta> findAllBy(Pageable pageable);

    @Override
    Mono<ConfiguracaoAlerta> findOneWithEagerRelationships(Long id);

    @Override
    Flux<ConfiguracaoAlerta> findAllWithEagerRelationships();

    @Override
    Flux<ConfiguracaoAlerta> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM configuracao_alerta entity WHERE entity.sensor_id = :id")
    Flux<ConfiguracaoAlerta> findBySensor(Long id);

    @Query("SELECT * FROM configuracao_alerta entity WHERE entity.sensor_id IS NULL")
    Flux<ConfiguracaoAlerta> findAllWhereSensorIsNull();

    @Override
    <S extends ConfiguracaoAlerta> Mono<S> save(S entity);

    @Override
    Flux<ConfiguracaoAlerta> findAll();

    @Override
    Mono<ConfiguracaoAlerta> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ConfiguracaoAlertaRepositoryInternal {
    <S extends ConfiguracaoAlerta> Mono<S> save(S entity);

    Flux<ConfiguracaoAlerta> findAllBy(Pageable pageable);

    Flux<ConfiguracaoAlerta> findAll();

    Mono<ConfiguracaoAlerta> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ConfiguracaoAlerta> findAllBy(Pageable pageable, Criteria criteria);

    Mono<ConfiguracaoAlerta> findOneWithEagerRelationships(Long id);

    Flux<ConfiguracaoAlerta> findAllWithEagerRelationships();

    Flux<ConfiguracaoAlerta> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
