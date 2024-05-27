package br.com.supera.feedback360.service;

import br.com.supera.feedback360.repository.ClienteRepository;
import br.com.supera.feedback360.service.dto.ClienteDTO;
import br.com.supera.feedback360.service.mapper.ClienteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.Cliente}.
 */
@Service
@Transactional
public class ClienteService {

    private final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Save a cliente.
     *
     * @param clienteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClienteDTO> save(ClienteDTO clienteDTO) {
        log.debug("Request to save Cliente : {}", clienteDTO);
        return clienteRepository.save(clienteMapper.toEntity(clienteDTO)).map(clienteMapper::toDto);
    }

    /**
     * Update a cliente.
     *
     * @param clienteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClienteDTO> update(ClienteDTO clienteDTO) {
        log.debug("Request to update Cliente : {}", clienteDTO);
        return clienteRepository.save(clienteMapper.toEntity(clienteDTO)).map(clienteMapper::toDto);
    }

    /**
     * Partially update a cliente.
     *
     * @param clienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ClienteDTO> partialUpdate(ClienteDTO clienteDTO) {
        log.debug("Request to partially update Cliente : {}", clienteDTO);

        return clienteRepository
            .findById(clienteDTO.getId())
            .map(existingCliente -> {
                clienteMapper.partialUpdate(existingCliente, clienteDTO);

                return existingCliente;
            })
            .flatMap(clienteRepository::save)
            .map(clienteMapper::toDto);
    }

    /**
     * Get all the clientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ClienteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clientes");
        return clienteRepository.findAllBy(pageable).map(clienteMapper::toDto);
    }

    /**
     * Returns the number of clientes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return clienteRepository.count();
    }

    /**
     * Get one cliente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ClienteDTO> findOne(Long id) {
        log.debug("Request to get Cliente : {}", id);
        return clienteRepository.findById(id).map(clienteMapper::toDto);
    }

    /**
     * Delete the cliente by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Cliente : {}", id);
        return clienteRepository.deleteById(id);
    }
}
