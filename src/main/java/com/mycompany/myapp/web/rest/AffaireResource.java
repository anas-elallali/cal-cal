package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Affaire;
import com.mycompany.myapp.repository.AffaireRepository;
import com.mycompany.myapp.service.AffaireService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Affaire}.
 */
@RestController
@RequestMapping("/api")
public class AffaireResource {

    private final Logger log = LoggerFactory.getLogger(AffaireResource.class);

    private static final String ENTITY_NAME = "affaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffaireService affaireService;

    private final AffaireRepository affaireRepository;

    public AffaireResource(AffaireService affaireService, AffaireRepository affaireRepository) {
        this.affaireService = affaireService;
        this.affaireRepository = affaireRepository;
    }

    /**
     * {@code POST  /affaires} : Create a new affaire.
     *
     * @param affaire the affaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affaire, or with status {@code 400 (Bad Request)} if the affaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/affaires")
    public ResponseEntity<Affaire> createAffaire(@Valid @RequestBody Affaire affaire) throws URISyntaxException {
        log.debug("REST request to save Affaire : {}", affaire);
        if (affaire.getId() != null) {
            throw new BadRequestAlertException("A new affaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Affaire result = affaireService.save(affaire);
        return ResponseEntity
            .created(new URI("/api/affaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /affaires/:id} : Updates an existing affaire.
     *
     * @param id the id of the affaire to save.
     * @param affaire the affaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affaire,
     * or with status {@code 400 (Bad Request)} if the affaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affaires/{id}")
    public ResponseEntity<Affaire> updateAffaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Affaire affaire
    ) throws URISyntaxException {
        log.debug("REST request to update Affaire : {}, {}", id, affaire);
        if (affaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Affaire result = affaireService.save(affaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /affaires/:id} : Partial updates given fields of an existing affaire, field will ignore if it is null
     *
     * @param id the id of the affaire to save.
     * @param affaire the affaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affaire,
     * or with status {@code 400 (Bad Request)} if the affaire is not valid,
     * or with status {@code 404 (Not Found)} if the affaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the affaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/affaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Affaire> partialUpdateAffaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Affaire affaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update Affaire partially : {}, {}", id, affaire);
        if (affaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Affaire> result = affaireService.partialUpdate(affaire);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affaire.getId().toString())
        );
    }

    /**
     * {@code GET  /affaires} : get all the affaires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affaires in body.
     */
    @GetMapping("/affaires")
    public ResponseEntity<List<Affaire>> getAllAffaires(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Affaires");
        Page<Affaire> page = affaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /affaires/:id} : get the "id" affaire.
     *
     * @param id the id of the affaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affaires/{id}")
    public ResponseEntity<Affaire> getAffaire(@PathVariable Long id) {
        log.debug("REST request to get Affaire : {}", id);
        Optional<Affaire> affaire = affaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affaire);
    }

    /**
     * {@code DELETE  /affaires/:id} : delete the "id" affaire.
     *
     * @param id the id of the affaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affaires/{id}")
    public ResponseEntity<Void> deleteAffaire(@PathVariable Long id) {
        log.debug("REST request to delete Affaire : {}", id);
        affaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
