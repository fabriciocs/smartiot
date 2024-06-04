package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.SysUser;
import br.com.supera.smartiot.repository.SysUserRepository;
import br.com.supera.smartiot.service.dto.SysUserDTO;
import br.com.supera.smartiot.service.mapper.SysUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.SysUser}.
 */
@Service
@Transactional
public class SysUserService {

    private final Logger log = LoggerFactory.getLogger(SysUserService.class);

    private final SysUserRepository sysUserRepository;

    private final SysUserMapper sysUserMapper;

    public SysUserService(SysUserRepository sysUserRepository, SysUserMapper sysUserMapper) {
        this.sysUserRepository = sysUserRepository;
        this.sysUserMapper = sysUserMapper;
    }

    /**
     * Save a sysUser.
     *
     * @param sysUserDTO the entity to save.
     * @return the persisted entity.
     */
    public SysUserDTO save(SysUserDTO sysUserDTO) {
        log.debug("Request to save SysUser : {}", sysUserDTO);
        SysUser sysUser = sysUserMapper.toEntity(sysUserDTO);
        sysUser = sysUserRepository.save(sysUser);
        return sysUserMapper.toDto(sysUser);
    }

    /**
     * Update a sysUser.
     *
     * @param sysUserDTO the entity to save.
     * @return the persisted entity.
     */
    public SysUserDTO update(SysUserDTO sysUserDTO) {
        log.debug("Request to update SysUser : {}", sysUserDTO);
        SysUser sysUser = sysUserMapper.toEntity(sysUserDTO);
        sysUser = sysUserRepository.save(sysUser);
        return sysUserMapper.toDto(sysUser);
    }

    /**
     * Partially update a sysUser.
     *
     * @param sysUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SysUserDTO> partialUpdate(SysUserDTO sysUserDTO) {
        log.debug("Request to partially update SysUser : {}", sysUserDTO);

        return sysUserRepository
            .findById(sysUserDTO.getId())
            .map(existingSysUser -> {
                sysUserMapper.partialUpdate(existingSysUser, sysUserDTO);

                return existingSysUser;
            })
            .map(sysUserRepository::save)
            .map(sysUserMapper::toDto);
    }

    /**
     * Get all the sysUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SysUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SysUsers");
        return sysUserRepository.findAll(pageable).map(sysUserMapper::toDto);
    }

    /**
     * Get one sysUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SysUserDTO> findOne(Long id) {
        log.debug("Request to get SysUser : {}", id);
        return sysUserRepository.findById(id).map(sysUserMapper::toDto);
    }

    /**
     * Delete the sysUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SysUser : {}", id);
        sysUserRepository.deleteById(id);
    }
}
