package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Profile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileRepository extends ReactiveCrudRepository<Profile, Long>, ProfileRepositoryInternal {
    @Override
    Mono<Profile> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Profile> findAllWithEagerRelationships();

    @Override
    Flux<Profile> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM profile entity WHERE entity.user_id = :id")
    Flux<Profile> findByUser(Long id);

    @Query("SELECT * FROM profile entity WHERE entity.user_id IS NULL")
    Flux<Profile> findAllWhereUserIsNull();

    @Override
    <S extends Profile> Mono<S> save(S entity);

    @Override
    Flux<Profile> findAll();

    @Override
    Mono<Profile> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProfileRepositoryInternal {
    <S extends Profile> Mono<S> save(S entity);

    Flux<Profile> findAllBy(Pageable pageable);

    Flux<Profile> findAll();

    Mono<Profile> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Profile> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Profile> findOneWithEagerRelationships(Long id);

    Flux<Profile> findAllWithEagerRelationships();

    Flux<Profile> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
