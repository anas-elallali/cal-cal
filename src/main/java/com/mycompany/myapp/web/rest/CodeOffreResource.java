package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CodeOffre;
import com.mycompany.myapp.repository.CodeOffreRepository;
import com.mycompany.myapp.service.CodeOffreService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CodeOffre}.
 */
@RestController
@RequestMapping("/api")
public class CodeOffreResource {

    private final Logger log = LoggerFactory.getLogger(CodeOffreResource.class);

    private static final String ENTITY_NAME = "codeOffre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodeOffreService codeOffreService;

    private final CodeOffreRepository codeOffreRepository;

    public CodeOffreResource(CodeOffreService codeOffreService, CodeOffreRepository codeOffreRepository) {
        this.codeOffreService = codeOffreService;
        this.codeOffreRepository = codeOffreRepository;
    }

    /**
     * {@code POST  /code-offres} : Create a new codeOffre.
     *
     * @param codeOffre the codeOffre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codeOffre, or with status {@code 400 (Bad Request)} if the codeOffre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/code-offres")
    public ResponseEntity<CodeOffre> createCodeOffre(@Valid @RequestBody CodeOffre codeOffre) throws URISyntaxException {
        log.debug("REST request to save CodeOffre : {}", codeOffre);
        if (codeOffre.getId() != null) {
            throw new BadRequestAlertException("A new codeOffre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CodeOffre result = codeOffreService.save(codeOffre);
        return ResponseEntity
            .created(new URI("/api/code-offres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /code-offres/:id} : Updates an existing codeOffre.
     *
     * @param id the id of the codeOffre to save.
     * @param codeOffre the codeOffre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeOffre,
     * or with status {@code 400 (Bad Request)} if the codeOffre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codeOffre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/code-offres/{id}")
    public ResponseEntity<CodeOffre> updateCodeOffre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CodeOffre codeOffre
    ) throws URISyntaxException {
        log.debug("REST request to update CodeOffre : {}, {}", id, codeOffre);
        if (codeOffre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeOffre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeOffreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CodeOffre result = codeOffreService.save(codeOffre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeOffre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /code-offres/:id} : Partial updates given fields of an existing codeOffre, field will ignore if it is null
     *
     * @param id the id of the codeOffre to save.
     * @param codeOffre the codeOffre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeOffre,
     * or with status {@code 400 (Bad Request)} if the codeOffre is not valid,
     * or with status {@code 404 (Not Found)} if the codeOffre is not found,
     * or with status {@code 500 (Internal Server Error)} if the codeOffre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/code-offres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodeOffre> partialUpdateCodeOffre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CodeOffre codeOffre
    ) throws URISyntaxException {
        log.debug("REST request to partial update CodeOffre partially : {}, {}", id, codeOffre);
        if (codeOffre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeOffre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeOffreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodeOffre> result = codeOffreService.partialUpdate(codeOffre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeOffre.getId().toString())
        );
    }

    /**
     * {@code GET  /code-offres} : get all the codeOffres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codeOffres in body.
     */
    @GetMapping("/code-offres")
    public ResponseEntity<List<CodeOffre>> getAllCodeOffres(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CodeOffres");
        Page<CodeOffre> page = codeOffreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /code-offres/:id} : get the "id" codeOffre.
     *
     * @param id the id of the codeOffre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codeOffre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/code-offres/{id}")
    public ResponseEntity<CodeOffre> getCodeOffre(@PathVariable Long id) {
        log.debug("REST request to get CodeOffre : {}", id);
        Optional<CodeOffre> codeOffre = codeOffreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codeOffre);
    }

    /**
     * {@code DELETE  /code-offres/:id} : delete the "id" codeOffre.
     *
     * @param id the id of the codeOffre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/code-offres/{id}")
    public ResponseEntity<Void> deleteCodeOffre(@PathVariable Long id) {
        log.debug("REST request to delete CodeOffre : {}", id);
        codeOffreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
