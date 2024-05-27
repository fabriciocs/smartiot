package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.Sensor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends ReactiveCrudRepository<Sensor, Long>, SensorRepositoryInternal {
    Flux<Sensor> findAllBy(Pageable pageable);

    @Override
    Mono<Sensor> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Sensor> findAllWithEagerRelationships();

    @Override
    Flux<Sensor> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM sensor entity WHERE entity.cliente_id = :id")
    Flux<Sensor> findByCliente(Long id);

    @Query("SELECT * FROM sensor entity WHERE entity.cliente_id IS NULL")
    Flux<Sensor> findAllWhereClienteIsNull();

    @Query("SELECT * FROM sensor entity WHERE entity.dado_sensores_id = :id")
    Flux<Sensor> findByDadoSensores(Long id);

    @Query("SELECT * FROM sensor entity WHERE entity.dado_sensores_id IS NULL")
    Flux<Sensor> findAllWhereDadoSensoresIsNull();

    @Override
    <S extends Sensor> Mono<S> save(S entity);

    @Override
    Flux<Sensor> findAll();

    @Override
    Mono<Sensor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SensorRepositoryInternal {
    <S extends Sensor> Mono<S> save(S entity);

    Flux<Sensor> findAllBy(Pageable pageable);

    Flux<Sensor> findAll();

    Mono<Sensor> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Sensor> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Sensor> findOneWithEagerRelationships(Long id);

    Flux<Sensor> findAllWithEagerRelationships();

    Flux<Sensor> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
