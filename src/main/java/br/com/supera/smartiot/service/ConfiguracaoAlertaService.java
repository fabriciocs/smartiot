package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.ConfiguracaoAlerta;
import br.com.supera.smartiot.repository.ConfiguracaoAlertaRepository;
import br.com.supera.smartiot.service.dto.ConfiguracaoAlertaDTO;
import br.com.supera.smartiot.service.mapper.ConfiguracaoAlertaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.ConfiguracaoAlerta}.
 */
@Service
@Transactional
public class ConfiguracaoAlertaService {

    private final Logger log = LoggerFactory.getLogger(ConfiguracaoAlertaService.class);

    private final ConfiguracaoAlertaRepository configuracaoAlertaRepository;

    private final ConfiguracaoAlertaMapper configuracaoAlertaMapper;

    public ConfiguracaoAlertaService(
        ConfiguracaoAlertaRepository configuracaoAlertaRepository,
        ConfiguracaoAlertaMapper configuracaoAlertaMapper
    ) {
        this.configuracaoAlertaRepository = configuracaoAlertaRepository;
        this.configuracaoAlertaMapper = configuracaoAlertaMapper;
    }

    /**
     * Save a configuracaoAlerta.
     *
     * @param configuracaoAlertaDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfiguracaoAlertaDTO save(ConfiguracaoAlertaDTO configuracaoAlertaDTO) {
        log.debug("Request to save ConfiguracaoAlerta : {}", configuracaoAlertaDTO);
        ConfiguracaoAlerta configuracaoAlerta = configuracaoAlertaMapper.toEntity(configuracaoAlertaDTO);
        configuracaoAlerta = configuracaoAlertaRepository.save(configuracaoAlerta);
        return configuracaoAlertaMapper.toDto(configuracaoAlerta);
    }

    /**
     * Update a configuracaoAlerta.
     *
     * @param configuracaoAlertaDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfiguracaoAlertaDTO update(ConfiguracaoAlertaDTO configuracaoAlertaDTO) {
        log.debug("Request to update ConfiguracaoAlerta : {}", configuracaoAlertaDTO);
        ConfiguracaoAlerta configuracaoAlerta = configuracaoAlertaMapper.toEntity(configuracaoAlertaDTO);
        configuracaoAlerta = configuracaoAlertaRepository.save(configuracaoAlerta);
        return configuracaoAlertaMapper.toDto(configuracaoAlerta);
    }

    /**
     * Partially update a configuracaoAlerta.
     *
     * @param configuracaoAlertaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConfiguracaoAlertaDTO> partialUpdate(ConfiguracaoAlertaDTO configuracaoAlertaDTO) {
        log.debug("Request to partially update ConfiguracaoAlerta : {}", configuracaoAlertaDTO);

        return configuracaoAlertaRepository
            .findById(configuracaoAlertaDTO.getId())
            .map(existingConfiguracaoAlerta -> {
                configuracaoAlertaMapper.partialUpdate(existingConfiguracaoAlerta, configuracaoAlertaDTO);

                return existingConfiguracaoAlerta;
            })
            .map(configuracaoAlertaRepository::save)
            .map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Get all the configuracaoAlertas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfiguracaoAlertaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfiguracaoAlertas");
        return configuracaoAlertaRepository.findAll(pageable).map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Get all the configuracaoAlertas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ConfiguracaoAlertaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return configuracaoAlertaRepository.findAllWithEagerRelationships(pageable).map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Get one configuracaoAlerta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfiguracaoAlertaDTO> findOne(Long id) {
        log.debug("Request to get ConfiguracaoAlerta : {}", id);
        return configuracaoAlertaRepository.findOneWithEagerRelationships(id).map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Delete the configuracaoAlerta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfiguracaoAlerta : {}", id);
        configuracaoAlertaRepository.deleteById(id);
    }
}
