package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.DadoSensor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DadoSensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DadoSensorRepository extends ReactiveCrudRepository<DadoSensor, Long>, DadoSensorRepositoryInternal {
    Flux<DadoSensor> findAllBy(Pageable pageable);

    @Override
    <S extends DadoSensor> Mono<S> save(S entity);

    @Override
    Flux<DadoSensor> findAll();

    @Override
    Mono<DadoSensor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DadoSensorRepositoryInternal {
    <S extends DadoSensor> Mono<S> save(S entity);

    Flux<DadoSensor> findAllBy(Pageable pageable);

    Flux<DadoSensor> findAll();

    Mono<DadoSensor> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DadoSensor> findAllBy(Pageable pageable, Criteria criteria);
}
