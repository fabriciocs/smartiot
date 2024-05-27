package br.com.supera.feedback360.service;

import br.com.supera.feedback360.repository.ConfiguracaoAlertaRepository;
import br.com.supera.feedback360.service.dto.ConfiguracaoAlertaDTO;
import br.com.supera.feedback360.service.mapper.ConfiguracaoAlertaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.ConfiguracaoAlerta}.
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
    public Mono<ConfiguracaoAlertaDTO> save(ConfiguracaoAlertaDTO configuracaoAlertaDTO) {
        log.debug("Request to save ConfiguracaoAlerta : {}", configuracaoAlertaDTO);
        return configuracaoAlertaRepository
            .save(configuracaoAlertaMapper.toEntity(configuracaoAlertaDTO))
            .map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Update a configuracaoAlerta.
     *
     * @param configuracaoAlertaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConfiguracaoAlertaDTO> update(ConfiguracaoAlertaDTO configuracaoAlertaDTO) {
        log.debug("Request to update ConfiguracaoAlerta : {}", configuracaoAlertaDTO);
        return configuracaoAlertaRepository
            .save(configuracaoAlertaMapper.toEntity(configuracaoAlertaDTO))
            .map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Partially update a configuracaoAlerta.
     *
     * @param configuracaoAlertaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ConfiguracaoAlertaDTO> partialUpdate(ConfiguracaoAlertaDTO configuracaoAlertaDTO) {
        log.debug("Request to partially update ConfiguracaoAlerta : {}", configuracaoAlertaDTO);

        return configuracaoAlertaRepository
            .findById(configuracaoAlertaDTO.getId())
            .map(existingConfiguracaoAlerta -> {
                configuracaoAlertaMapper.partialUpdate(existingConfiguracaoAlerta, configuracaoAlertaDTO);

                return existingConfiguracaoAlerta;
            })
            .flatMap(configuracaoAlertaRepository::save)
            .map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Get all the configuracaoAlertas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ConfiguracaoAlertaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfiguracaoAlertas");
        return configuracaoAlertaRepository.findAllBy(pageable).map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Get all the configuracaoAlertas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ConfiguracaoAlertaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return configuracaoAlertaRepository.findAllWithEagerRelationships(pageable).map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Returns the number of configuracaoAlertas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return configuracaoAlertaRepository.count();
    }

    /**
     * Get one configuracaoAlerta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ConfiguracaoAlertaDTO> findOne(Long id) {
        log.debug("Request to get ConfiguracaoAlerta : {}", id);
        return configuracaoAlertaRepository.findOneWithEagerRelationships(id).map(configuracaoAlertaMapper::toDto);
    }

    /**
     * Delete the configuracaoAlerta by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ConfiguracaoAlerta : {}", id);
        return configuracaoAlertaRepository.deleteById(id);
    }
}
