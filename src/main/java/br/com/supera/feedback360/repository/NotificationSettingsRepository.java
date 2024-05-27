package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.NotificationSettings;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the NotificationSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationSettingsRepository
    extends ReactiveCrudRepository<NotificationSettings, Long>, NotificationSettingsRepositoryInternal {
    @Override
    Mono<NotificationSettings> findOneWithEagerRelationships(Long id);

    @Override
    Flux<NotificationSettings> findAllWithEagerRelationships();

    @Override
    Flux<NotificationSettings> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM notification_settings entity WHERE entity.user_id = :id")
    Flux<NotificationSettings> findByUser(Long id);

    @Query("SELECT * FROM notification_settings entity WHERE entity.user_id IS NULL")
    Flux<NotificationSettings> findAllWhereUserIsNull();

    @Override
    <S extends NotificationSettings> Mono<S> save(S entity);

    @Override
    Flux<NotificationSettings> findAll();

    @Override
    Mono<NotificationSettings> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface NotificationSettingsRepositoryInternal {
    <S extends NotificationSettings> Mono<S> save(S entity);

    Flux<NotificationSettings> findAllBy(Pageable pageable);

    Flux<NotificationSettings> findAll();

    Mono<NotificationSettings> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<NotificationSettings> findAllBy(Pageable pageable, Criteria criteria);

    Mono<NotificationSettings> findOneWithEagerRelationships(Long id);

    Flux<NotificationSettings> findAllWithEagerRelationships();

    Flux<NotificationSettings> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
