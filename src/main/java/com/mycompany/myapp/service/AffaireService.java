package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Affaire;
import com.mycompany.myapp.repository.AffaireRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Affaire}.
 */
@Service
@Transactional
public class AffaireService {

    private final Logger log = LoggerFactory.getLogger(AffaireService.class);

    private final AffaireRepository affaireRepository;

    public AffaireService(AffaireRepository affaireRepository) {
        this.affaireRepository = affaireRepository;
    }

    /**
     * Save a affaire.
     *
     * @param affaire the entity to save.
     * @return the persisted entity.
     */
    public Affaire save(Affaire affaire) {
        log.debug("Request to save Affaire : {}", affaire);
        return affaireRepository.save(affaire);
    }

    /**
     * Partially update a affaire.
     *
     * @param affaire the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Affaire> partialUpdate(Affaire affaire) {
        log.debug("Request to partially update Affaire : {}", affaire);

        return affaireRepository
            .findById(affaire.getId())
            .map(existingAffaire -> {
                if (affaire.getCode() != null) {
                    existingAffaire.setCode(affaire.getCode());
                }
                if (affaire.getLibelle() != null) {
                    existingAffaire.setLibelle(affaire.getLibelle());
                }
                if (affaire.getContact() != null) {
                    existingAffaire.setContact(affaire.getContact());
                }
                if (affaire.getEstAffiche() != null) {
                    existingAffaire.setEstAffiche(affaire.getEstAffiche());
                }
                if (affaire.getComId() != null) {
                    existingAffaire.setComId(affaire.getComId());
                }
                if (affaire.getActif() != null) {
                    existingAffaire.setActif(affaire.getActif());
                }

                return existingAffaire;
            })
            .map(affaireRepository::save);
    }

    /**
     * Get all the affaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Affaire> findAll(Pageable pageable) {
        log.debug("Request to get all Affaires");
        return affaireRepository.findAll(pageable);
    }

    /**
     * Get one affaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Affaire> findOne(Long id) {
        log.debug("Request to get Affaire : {}", id);
        return affaireRepository.findById(id);
    }

    /**
     * Delete the affaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Affaire : {}", id);
        affaireRepository.deleteById(id);
    }
}
