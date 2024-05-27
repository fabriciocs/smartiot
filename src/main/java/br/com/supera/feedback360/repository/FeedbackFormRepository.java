package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.FeedbackForm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FeedbackForm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackFormRepository extends ReactiveCrudRepository<FeedbackForm, Long>, FeedbackFormRepositoryInternal {
    Flux<FeedbackForm> findAllBy(Pageable pageable);

    @Override
    Mono<FeedbackForm> findOneWithEagerRelationships(Long id);

    @Override
    Flux<FeedbackForm> findAllWithEagerRelationships();

    @Override
    Flux<FeedbackForm> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM feedback_form entity WHERE entity.creator_id = :id")
    Flux<FeedbackForm> findByCreator(Long id);

    @Query("SELECT * FROM feedback_form entity WHERE entity.creator_id IS NULL")
    Flux<FeedbackForm> findAllWhereCreatorIsNull();

    @Override
    <S extends FeedbackForm> Mono<S> save(S entity);

    @Override
    Flux<FeedbackForm> findAll();

    @Override
    Mono<FeedbackForm> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FeedbackFormRepositoryInternal {
    <S extends FeedbackForm> Mono<S> save(S entity);

    Flux<FeedbackForm> findAllBy(Pageable pageable);

    Flux<FeedbackForm> findAll();

    Mono<FeedbackForm> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FeedbackForm> findAllBy(Pageable pageable, Criteria criteria);

    Mono<FeedbackForm> findOneWithEagerRelationships(Long id);

    Flux<FeedbackForm> findAllWithEagerRelationships();

    Flux<FeedbackForm> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
