package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Ordre;
import com.mycompany.myapp.repository.OrdreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ordre}.
 */
@Service
@Transactional
public class OrdreService {

    private final Logger log = LoggerFactory.getLogger(OrdreService.class);

    private final OrdreRepository ordreRepository;

    public OrdreService(OrdreRepository ordreRepository) {
        this.ordreRepository = ordreRepository;
    }

    /**
     * Save a ordre.
     *
     * @param ordre the entity to save.
     * @return the persisted entity.
     */
    public Ordre save(Ordre ordre) {
        log.debug("Request to save Ordre : {}", ordre);
        return ordreRepository.save(ordre);
    }

    /**
     * Partially update a ordre.
     *
     * @param ordre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Ordre> partialUpdate(Ordre ordre) {
        log.debug("Request to partially update Ordre : {}", ordre);

        return ordreRepository
            .findById(ordre.getId())
            .map(existingOrdre -> {
                if (ordre.getLibelle() != null) {
                    existingOrdre.setLibelle(ordre.getLibelle());
                }
                if (ordre.getIsInitMvm() != null) {
                    existingOrdre.setIsInitMvm(ordre.getIsInitMvm());
                }

                return existingOrdre;
            })
            .map(ordreRepository::save);
    }

    /**
     * Get all the ordres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Ordre> findAll(Pageable pageable) {
        log.debug("Request to get all Ordres");
        return ordreRepository.findAll(pageable);
    }

    /**
     * Get one ordre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Ordre> findOne(Long id) {
        log.debug("Request to get Ordre : {}", id);
        return ordreRepository.findById(id);
    }

    /**
     * Delete the ordre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ordre : {}", id);
        ordreRepository.deleteById(id);
    }
}
