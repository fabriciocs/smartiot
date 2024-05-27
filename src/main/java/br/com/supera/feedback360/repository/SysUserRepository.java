package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.SysUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SysUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysUserRepository extends ReactiveCrudRepository<SysUser, Long>, SysUserRepositoryInternal {
    Flux<SysUser> findAllBy(Pageable pageable);

    @Override
    Mono<SysUser> findOneWithEagerRelationships(Long id);

    @Override
    Flux<SysUser> findAllWithEagerRelationships();

    @Override
    Flux<SysUser> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM sys_user entity WHERE entity.role_id = :id")
    Flux<SysUser> findByRole(Long id);

    @Query("SELECT * FROM sys_user entity WHERE entity.role_id IS NULL")
    Flux<SysUser> findAllWhereRoleIsNull();

    @Override
    <S extends SysUser> Mono<S> save(S entity);

    @Override
    Flux<SysUser> findAll();

    @Override
    Mono<SysUser> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SysUserRepositoryInternal {
    <S extends SysUser> Mono<S> save(S entity);

    Flux<SysUser> findAllBy(Pageable pageable);

    Flux<SysUser> findAll();

    Mono<SysUser> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SysUser> findAllBy(Pageable pageable, Criteria criteria);

    Mono<SysUser> findOneWithEagerRelationships(Long id);

    Flux<SysUser> findAllWithEagerRelationships();

    Flux<SysUser> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
