package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.FeedbackResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FeedbackResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackResponseRepository extends ReactiveCrudRepository<FeedbackResponse, Long>, FeedbackResponseRepositoryInternal {
    Flux<FeedbackResponse> findAllBy(Pageable pageable);

    @Override
    Mono<FeedbackResponse> findOneWithEagerRelationships(Long id);

    @Override
    Flux<FeedbackResponse> findAllWithEagerRelationships();

    @Override
    Flux<FeedbackResponse> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM feedback_response entity WHERE entity.form_id = :id")
    Flux<FeedbackResponse> findByForm(Long id);

    @Query("SELECT * FROM feedback_response entity WHERE entity.form_id IS NULL")
    Flux<FeedbackResponse> findAllWhereFormIsNull();

    @Query("SELECT * FROM feedback_response entity WHERE entity.user_id = :id")
    Flux<FeedbackResponse> findByUser(Long id);

    @Query("SELECT * FROM feedback_response entity WHERE entity.user_id IS NULL")
    Flux<FeedbackResponse> findAllWhereUserIsNull();

    @Override
    <S extends FeedbackResponse> Mono<S> save(S entity);

    @Override
    Flux<FeedbackResponse> findAll();

    @Override
    Mono<FeedbackResponse> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FeedbackResponseRepositoryInternal {
    <S extends FeedbackResponse> Mono<S> save(S entity);

    Flux<FeedbackResponse> findAllBy(Pageable pageable);

    Flux<FeedbackResponse> findAll();

    Mono<FeedbackResponse> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FeedbackResponse> findAllBy(Pageable pageable, Criteria criteria);

    Mono<FeedbackResponse> findOneWithEagerRelationships(Long id);

    Flux<FeedbackResponse> findAllWithEagerRelationships();

    Flux<FeedbackResponse> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
