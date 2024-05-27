package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.IntegrationConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IntegrationConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegrationConfigRepository extends ReactiveCrudRepository<IntegrationConfig, Long>, IntegrationConfigRepositoryInternal {
    @Override
    Mono<IntegrationConfig> findOneWithEagerRelationships(Long id);

    @Override
    Flux<IntegrationConfig> findAllWithEagerRelationships();

    @Override
    Flux<IntegrationConfig> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM integration_config entity WHERE entity.external_system_id = :id")
    Flux<IntegrationConfig> findByExternalSystem(Long id);

    @Query("SELECT * FROM integration_config entity WHERE entity.external_system_id IS NULL")
    Flux<IntegrationConfig> findAllWhereExternalSystemIsNull();

    @Override
    <S extends IntegrationConfig> Mono<S> save(S entity);

    @Override
    Flux<IntegrationConfig> findAll();

    @Override
    Mono<IntegrationConfig> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IntegrationConfigRepositoryInternal {
    <S extends IntegrationConfig> Mono<S> save(S entity);

    Flux<IntegrationConfig> findAllBy(Pageable pageable);

    Flux<IntegrationConfig> findAll();

    Mono<IntegrationConfig> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IntegrationConfig> findAllBy(Pageable pageable, Criteria criteria);

    Mono<IntegrationConfig> findOneWithEagerRelationships(Long id);

    Flux<IntegrationConfig> findAllWithEagerRelationships();

    Flux<IntegrationConfig> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
