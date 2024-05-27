package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.ExternalSystem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ExternalSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalSystemRepository extends ReactiveCrudRepository<ExternalSystem, Long>, ExternalSystemRepositoryInternal {
    @Override
    <S extends ExternalSystem> Mono<S> save(S entity);

    @Override
    Flux<ExternalSystem> findAll();

    @Override
    Mono<ExternalSystem> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ExternalSystemRepositoryInternal {
    <S extends ExternalSystem> Mono<S> save(S entity);

    Flux<ExternalSystem> findAllBy(Pageable pageable);

    Flux<ExternalSystem> findAll();

    Mono<ExternalSystem> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ExternalSystem> findAllBy(Pageable pageable, Criteria criteria);
}
