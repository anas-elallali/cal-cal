package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Typologie;
import com.mycompany.myapp.repository.TypologieRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Typologie}.
 */
@Service
@Transactional
public class TypologieService {

    private final Logger log = LoggerFactory.getLogger(TypologieService.class);

    private final TypologieRepository typologieRepository;

    public TypologieService(TypologieRepository typologieRepository) {
        this.typologieRepository = typologieRepository;
    }

    /**
     * Save a typologie.
     *
     * @param typologie the entity to save.
     * @return the persisted entity.
     */
    public Typologie save(Typologie typologie) {
        log.debug("Request to save Typologie : {}", typologie);
        return typologieRepository.save(typologie);
    }

    /**
     * Partially update a typologie.
     *
     * @param typologie the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Typologie> partialUpdate(Typologie typologie) {
        log.debug("Request to partially update Typologie : {}", typologie);

        return typologieRepository
            .findById(typologie.getId())
            .map(existingTypologie -> {
                if (typologie.getCode() != null) {
                    existingTypologie.setCode(typologie.getCode());
                }
                if (typologie.getLibelle() != null) {
                    existingTypologie.setLibelle(typologie.getLibelle());
                }
                if (typologie.getOrdre() != null) {
                    existingTypologie.setOrdre(typologie.getOrdre());
                }

                return existingTypologie;
            })
            .map(typologieRepository::save);
    }

    /**
     * Get all the typologies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Typologie> findAll(Pageable pageable) {
        log.debug("Request to get all Typologies");
        return typologieRepository.findAll(pageable);
    }

    /**
     * Get one typologie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Typologie> findOne(Long id) {
        log.debug("Request to get Typologie : {}", id);
        return typologieRepository.findById(id);
    }

    /**
     * Delete the typologie by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Typologie : {}", id);
        typologieRepository.deleteById(id);
    }
}
