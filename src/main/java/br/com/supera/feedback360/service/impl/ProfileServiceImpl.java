package br.com.supera.feedback360.service.impl;

import br.com.supera.feedback360.repository.ProfileRepository;
import br.com.supera.feedback360.service.ProfileService;
import br.com.supera.feedback360.service.dto.ProfileDTO;
import br.com.supera.feedback360.service.mapper.ProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link br.com.supera.feedback360.domain.Profile}.
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    @Override
    public Mono<ProfileDTO> save(ProfileDTO profileDTO) {
        log.debug("Request to save Profile : {}", profileDTO);
        return profileRepository.save(profileMapper.toEntity(profileDTO)).map(profileMapper::toDto);
    }

    @Override
    public Mono<ProfileDTO> update(ProfileDTO profileDTO) {
        log.debug("Request to update Profile : {}", profileDTO);
        return profileRepository.save(profileMapper.toEntity(profileDTO)).map(profileMapper::toDto);
    }

    @Override
    public Mono<ProfileDTO> partialUpdate(ProfileDTO profileDTO) {
        log.debug("Request to partially update Profile : {}", profileDTO);

        return profileRepository
            .findById(profileDTO.getId())
            .map(existingProfile -> {
                profileMapper.partialUpdate(existingProfile, profileDTO);

                return existingProfile;
            })
            .flatMap(profileRepository::save)
            .map(profileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProfileDTO> findAll() {
        log.debug("Request to get all Profiles");
        return profileRepository.findAll().map(profileMapper::toDto);
    }

    public Flux<ProfileDTO> findAllWithEagerRelationships(Pageable pageable) {
        return profileRepository.findAllWithEagerRelationships(pageable).map(profileMapper::toDto);
    }

    public Mono<Long> countAll() {
        return profileRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProfileDTO> findOne(Long id) {
        log.debug("Request to get Profile : {}", id);
        return profileRepository.findOneWithEagerRelationships(id).map(profileMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Profile : {}", id);
        return profileRepository.deleteById(id);
    }
}
