package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.SysRoleRepository;
import br.com.supera.feedback360.service.SysRoleService;
import br.com.supera.feedback360.service.dto.SysRoleDTO;
import br.com.supera.feedback360.service.mapper.SysRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.SysRole}.
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    private final Logger log = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    private final SysRoleRepository sysRoleRepository;

    private final SysRoleMapper sysRoleMapper;

    public SysRoleServiceImpl(SysRoleRepository sysRoleRepository, SysRoleMapper sysRoleMapper) {
        this.sysRoleRepository = sysRoleRepository;
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public Mono<SysRoleDTO> save(SysRoleDTO sysRoleDTO) {
        log.debug("Request to save SysRole : {}", sysRoleDTO);
        return sysRoleRepository.save(sysRoleMapper.toEntity(sysRoleDTO)).map(sysRoleMapper::toDto);
    }

    @Override
    public Mono<SysRoleDTO> update(SysRoleDTO sysRoleDTO) {
        log.debug("Request to update SysRole : {}", sysRoleDTO);
        return sysRoleRepository.save(sysRoleMapper.toEntity(sysRoleDTO)).map(sysRoleMapper::toDto);
    }

    @Override
    public Mono<SysRoleDTO> partialUpdate(SysRoleDTO sysRoleDTO) {
        log.debug("Request to partially update SysRole : {}", sysRoleDTO);

        return sysRoleRepository
            .findById(sysRoleDTO.getId())
            .map(existingSysRole -> {
                sysRoleMapper.partialUpdate(existingSysRole, sysRoleDTO);

                return existingSysRole;
            })
            .flatMap(sysRoleRepository::save)
            .map(sysRoleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SysRoleDTO> findAll() {
        log.debug("Request to get all SysRoles");
        return sysRoleRepository.findAll().map(sysRoleMapper::toDto);
    }

    public Mono<Long> countAll() {
        return sysRoleRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SysRoleDTO> findOne(Long id) {
        log.debug("Request to get SysRole : {}", id);
        return sysRoleRepository.findById(id).map(sysRoleMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SysRole : {}", id);
        return sysRoleRepository.deleteById(id);
    }
}
