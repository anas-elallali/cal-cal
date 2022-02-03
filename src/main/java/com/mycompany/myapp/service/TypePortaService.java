package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TypePorta;
import com.mycompany.myapp.repository.TypePortaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypePorta}.
 */
@Service
@Transactional
public class TypePortaService {

    private final Logger log = LoggerFactory.getLogger(TypePortaService.class);

    private final TypePortaRepository typePortaRepository;

    public TypePortaService(TypePortaRepository typePortaRepository) {
        this.typePortaRepository = typePortaRepository;
    }

    /**
     * Save a typePorta.
     *
     * @param typePorta the entity to save.
     * @return the persisted entity.
     */
    public TypePorta save(TypePorta typePorta) {
        log.debug("Request to save TypePorta : {}", typePorta);
        return typePortaRepository.save(typePorta);
    }

    /**
     * Partially update a typePorta.
     *
     * @param typePorta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypePorta> partialUpdate(TypePorta typePorta) {
        log.debug("Request to partially update TypePorta : {}", typePorta);

        return typePortaRepository
            .findById(typePorta.getId())
            .map(existingTypePorta -> {
                if (typePorta.getLibelle() != null) {
                    existingTypePorta.setLibelle(typePorta.getLibelle());
                }

                return existingTypePorta;
            })
            .map(typePortaRepository::save);
    }

    /**
     * Get all the typePortas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypePorta> findAll(Pageable pageable) {
        log.debug("Request to get all TypePortas");
        return typePortaRepository.findAll(pageable);
    }

    /**
     * Get one typePorta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypePorta> findOne(Long id) {
        log.debug("Request to get TypePorta : {}", id);
        return typePortaRepository.findById(id);
    }

    /**
     * Delete the typePorta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypePorta : {}", id);
        typePortaRepository.deleteById(id);
    }
}
