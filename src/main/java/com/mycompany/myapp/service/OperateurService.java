package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Operateur;
import com.mycompany.myapp.repository.OperateurRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Operateur}.
 */
@Service
@Transactional
public class OperateurService {

    private final Logger log = LoggerFactory.getLogger(OperateurService.class);

    private final OperateurRepository operateurRepository;

    public OperateurService(OperateurRepository operateurRepository) {
        this.operateurRepository = operateurRepository;
    }

    /**
     * Save a operateur.
     *
     * @param operateur the entity to save.
     * @return the persisted entity.
     */
    public Operateur save(Operateur operateur) {
        log.debug("Request to save Operateur : {}", operateur);
        return operateurRepository.save(operateur);
    }

    /**
     * Partially update a operateur.
     *
     * @param operateur the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Operateur> partialUpdate(Operateur operateur) {
        log.debug("Request to partially update Operateur : {}", operateur);

        return operateurRepository
            .findById(operateur.getId())
            .map(existingOperateur -> {
                if (operateur.getCode() != null) {
                    existingOperateur.setCode(operateur.getCode());
                }
                if (operateur.getLibelle() != null) {
                    existingOperateur.setLibelle(operateur.getLibelle());
                }
                if (operateur.getSeq() != null) {
                    existingOperateur.setSeq(operateur.getSeq());
                }
                if (operateur.getDateSeq() != null) {
                    existingOperateur.setDateSeq(operateur.getDateSeq());
                }
                if (operateur.getContrat() != null) {
                    existingOperateur.setContrat(operateur.getContrat());
                }
                if (operateur.getSeqPort() != null) {
                    existingOperateur.setSeqPort(operateur.getSeqPort());
                }
                if (operateur.getSeqPortAr() != null) {
                    existingOperateur.setSeqPortAr(operateur.getSeqPortAr());
                }
                if (operateur.getSeqPortCri() != null) {
                    existingOperateur.setSeqPortCri(operateur.getSeqPortCri());
                }
                if (operateur.getSeqPortCr() != null) {
                    existingOperateur.setSeqPortCr(operateur.getSeqPortCr());
                }
                if (operateur.getLibelleAs() != null) {
                    existingOperateur.setLibelleAs(operateur.getLibelleAs());
                }

                return existingOperateur;
            })
            .map(operateurRepository::save);
    }

    /**
     * Get all the operateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Operateur> findAll(Pageable pageable) {
        log.debug("Request to get all Operateurs");
        return operateurRepository.findAll(pageable);
    }

    /**
     * Get one operateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Operateur> findOne(Long id) {
        log.debug("Request to get Operateur : {}", id);
        return operateurRepository.findById(id);
    }

    /**
     * Delete the operateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Operateur : {}", id);
        operateurRepository.deleteById(id);
    }
}
