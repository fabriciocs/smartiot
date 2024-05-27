package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.SysUserRepository;
import br.com.supera.feedback360.service.SysUserService;
import br.com.supera.feedback360.service.dto.SysUserDTO;
import br.com.supera.feedback360.service.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.SysUser}.
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    private final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    private final SysUserRepository sysUserRepository;

    private final SysUserMapper sysUserMapper;

    public SysUserServiceImpl(SysUserRepository sysUserRepository, SysUserMapper sysUserMapper) {
        this.sysUserRepository = sysUserRepository;
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public Mono<SysUserDTO> save(SysUserDTO sysUserDTO) {
        log.debug("Request to save SysUser : {}", sysUserDTO);
        return sysUserRepository.save(sysUserMapper.toEntity(sysUserDTO)).map(sysUserMapper::toDto);
    }

    @Override
    public Mono<SysUserDTO> update(SysUserDTO sysUserDTO) {
        log.debug("Request to update SysUser : {}", sysUserDTO);
        return sysUserRepository.save(sysUserMapper.toEntity(sysUserDTO)).map(sysUserMapper::toDto);
    }

    @Override
    public Mono<SysUserDTO> partialUpdate(SysUserDTO sysUserDTO) {
        log.debug("Request to partially update SysUser : {}", sysUserDTO);

        return sysUserRepository
            .findById(sysUserDTO.getId())
            .map(existingSysUser -> {
                sysUserMapper.partialUpdate(existingSysUser, sysUserDTO);

                return existingSysUser;
            })
            .flatMap(sysUserRepository::save)
            .map(sysUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SysUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SysUsers");
        return sysUserRepository.findAllBy(pageable).map(sysUserMapper::toDto);
    }

    public Flux<SysUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sysUserRepository.findAllWithEagerRelationships(pageable).map(sysUserMapper::toDto);
    }

    public Mono<Long> countAll() {
        return sysUserRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SysUserDTO> findOne(Long id) {
        log.debug("Request to get SysUser : {}", id);
        return sysUserRepository.findOneWithEagerRelationships(id).map(sysUserMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SysUser : {}", id);
        return sysUserRepository.deleteById(id);
    }
}
