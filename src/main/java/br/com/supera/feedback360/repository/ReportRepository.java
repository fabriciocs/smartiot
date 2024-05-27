package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Report entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRepository extends ReactiveCrudRepository<Report, Long>, ReportRepositoryInternal {
    Flux<Report> findAllBy(Pageable pageable);

    @Override
    Mono<Report> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Report> findAllWithEagerRelationships();

    @Override
    Flux<Report> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM report entity WHERE entity.generated_by_id = :id")
    Flux<Report> findByGeneratedBy(Long id);

    @Query("SELECT * FROM report entity WHERE entity.generated_by_id IS NULL")
    Flux<Report> findAllWhereGeneratedByIsNull();

    @Override
    <S extends Report> Mono<S> save(S entity);

    @Override
    Flux<Report> findAll();

    @Override
    Mono<Report> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ReportRepositoryInternal {
    <S extends Report> Mono<S> save(S entity);

    Flux<Report> findAllBy(Pageable pageable);

    Flux<Report> findAll();

    Mono<Report> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Report> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Report> findOneWithEagerRelationships(Long id);

    Flux<Report> findAllWithEagerRelationships();

    Flux<Report> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
