package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.SysRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SysRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysRoleRepository extends ReactiveCrudRepository<SysRole, Long>, SysRoleRepositoryInternal {
    @Override
    <S extends SysRole> Mono<S> save(S entity);

    @Override
    Flux<SysRole> findAll();

    @Override
    Mono<SysRole> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SysRoleRepositoryInternal {
    <S extends SysRole> Mono<S> save(S entity);

    Flux<SysRole> findAllBy(Pageable pageable);

    Flux<SysRole> findAll();

    Mono<SysRole> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SysRole> findAllBy(Pageable pageable, Criteria criteria);
}
