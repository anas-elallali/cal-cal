package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CodeOffre;
import com.mycompany.myapp.repository.CodeOffreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CodeOffre}.
 */
@Service
@Transactional
public class CodeOffreService {

    private final Logger log = LoggerFactory.getLogger(CodeOffreService.class);

    private final CodeOffreRepository codeOffreRepository;

    public CodeOffreService(CodeOffreRepository codeOffreRepository) {
        this.codeOffreRepository = codeOffreRepository;
    }

    /**
     * Save a codeOffre.
     *
     * @param codeOffre the entity to save.
     * @return the persisted entity.
     */
    public CodeOffre save(CodeOffre codeOffre) {
        log.debug("Request to save CodeOffre : {}", codeOffre);
        return codeOffreRepository.save(codeOffre);
    }

    /**
     * Partially update a codeOffre.
     *
     * @param codeOffre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CodeOffre> partialUpdate(CodeOffre codeOffre) {
        log.debug("Request to partially update CodeOffre : {}", codeOffre);

        return codeOffreRepository
            .findById(codeOffre.getId())
            .map(existingCodeOffre -> {
                if (codeOffre.getLibelle() != null) {
                    existingCodeOffre.setLibelle(codeOffre.getLibelle());
                }

                return existingCodeOffre;
            })
            .map(codeOffreRepository::save);
    }

    /**
     * Get all the codeOffres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CodeOffre> findAll(Pageable pageable) {
        log.debug("Request to get all CodeOffres");
        return codeOffreRepository.findAll(pageable);
    }

    /**
     * Get one codeOffre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CodeOffre> findOne(Long id) {
        log.debug("Request to get CodeOffre : {}", id);
        return codeOffreRepository.findById(id);
    }

    /**
     * Delete the codeOffre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CodeOffre : {}", id);
        codeOffreRepository.deleteById(id);
    }
}
