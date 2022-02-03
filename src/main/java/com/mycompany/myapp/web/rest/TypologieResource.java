package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Typologie;
import com.mycompany.myapp.repository.TypologieRepository;
import com.mycompany.myapp.service.TypologieService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Typologie}.
 */
@RestController
@RequestMapping("/api")
public class TypologieResource {

    private final Logger log = LoggerFactory.getLogger(TypologieResource.class);

    private static final String ENTITY_NAME = "typologie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypologieService typologieService;

    private final TypologieRepository typologieRepository;

    public TypologieResource(TypologieService typologieService, TypologieRepository typologieRepository) {
        this.typologieService = typologieService;
        this.typologieRepository = typologieRepository;
    }

    /**
     * {@code POST  /typologies} : Create a new typologie.
     *
     * @param typologie the typologie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typologie, or with status {@code 400 (Bad Request)} if the typologie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/typologies")
    public ResponseEntity<Typologie> createTypologie(@Valid @RequestBody Typologie typologie) throws URISyntaxException {
        log.debug("REST request to save Typologie : {}", typologie);
        if (typologie.getId() != null) {
            throw new BadRequestAlertException("A new typologie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Typologie result = typologieService.save(typologie);
        return ResponseEntity
            .created(new URI("/api/typologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /typologies/:id} : Updates an existing typologie.
     *
     * @param id the id of the typologie to save.
     * @param typologie the typologie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typologie,
     * or with status {@code 400 (Bad Request)} if the typologie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typologie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/typologies/{id}")
    public ResponseEntity<Typologie> updateTypologie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Typologie typologie
    ) throws URISyntaxException {
        log.debug("REST request to update Typologie : {}, {}", id, typologie);
        if (typologie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typologie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typologieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Typologie result = typologieService.save(typologie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typologie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /typologies/:id} : Partial updates given fields of an existing typologie, field will ignore if it is null
     *
     * @param id the id of the typologie to save.
     * @param typologie the typologie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typologie,
     * or with status {@code 400 (Bad Request)} if the typologie is not valid,
     * or with status {@code 404 (Not Found)} if the typologie is not found,
     * or with status {@code 500 (Internal Server Error)} if the typologie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/typologies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Typologie> partialUpdateTypologie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Typologie typologie
    ) throws URISyntaxException {
        log.debug("REST request to partial update Typologie partially : {}, {}", id, typologie);
        if (typologie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typologie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typologieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Typologie> result = typologieService.partialUpdate(typologie);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typologie.getId().toString())
        );
    }

    /**
     * {@code GET  /typologies} : get all the typologies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typologies in body.
     */
    @GetMapping("/typologies")
    public ResponseEntity<List<Typologie>> getAllTypologies(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Typologies");
        Page<Typologie> page = typologieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /typologies/:id} : get the "id" typologie.
     *
     * @param id the id of the typologie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typologie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/typologies/{id}")
    public ResponseEntity<Typologie> getTypologie(@PathVariable Long id) {
        log.debug("REST request to get Typologie : {}", id);
        Optional<Typologie> typologie = typologieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typologie);
    }

    /**
     * {@code DELETE  /typologies/:id} : delete the "id" typologie.
     *
     * @param id the id of the typologie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/typologies/{id}")
    public ResponseEntity<Void> deleteTypologie(@PathVariable Long id) {
        log.debug("REST request to delete Typologie : {}", id);
        typologieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
