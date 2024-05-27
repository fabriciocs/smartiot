package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.Analytics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Analytics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyticsRepository extends ReactiveCrudRepository<Analytics, Long>, AnalyticsRepositoryInternal {
    @Override
    <S extends Analytics> Mono<S> save(S entity);

    @Override
    Flux<Analytics> findAll();

    @Override
    Mono<Analytics> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AnalyticsRepositoryInternal {
    <S extends Analytics> Mono<S> save(S entity);

    Flux<Analytics> findAllBy(Pageable pageable);

    Flux<Analytics> findAll();

    Mono<Analytics> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Analytics> findAllBy(Pageable pageable, Criteria criteria);
}
