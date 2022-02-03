package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TypePorta;
import com.mycompany.myapp.repository.TypePortaRepository;
import com.mycompany.myapp.service.TypePortaService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TypePorta}.
 */
@RestController
@RequestMapping("/api")
public class TypePortaResource {

    private final Logger log = LoggerFactory.getLogger(TypePortaResource.class);

    private static final String ENTITY_NAME = "typePorta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePortaService typePortaService;

    private final TypePortaRepository typePortaRepository;

    public TypePortaResource(TypePortaService typePortaService, TypePortaRepository typePortaRepository) {
        this.typePortaService = typePortaService;
        this.typePortaRepository = typePortaRepository;
    }

    /**
     * {@code POST  /type-portas} : Create a new typePorta.
     *
     * @param typePorta the typePorta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePorta, or with status {@code 400 (Bad Request)} if the typePorta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-portas")
    public ResponseEntity<TypePorta> createTypePorta(@Valid @RequestBody TypePorta typePorta) throws URISyntaxException {
        log.debug("REST request to save TypePorta : {}", typePorta);
        if (typePorta.getId() != null) {
            throw new BadRequestAlertException("A new typePorta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypePorta result = typePortaService.save(typePorta);
        return ResponseEntity
            .created(new URI("/api/type-portas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-portas/:id} : Updates an existing typePorta.
     *
     * @param id the id of the typePorta to save.
     * @param typePorta the typePorta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePorta,
     * or with status {@code 400 (Bad Request)} if the typePorta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePorta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-portas/{id}")
    public ResponseEntity<TypePorta> updateTypePorta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypePorta typePorta
    ) throws URISyntaxException {
        log.debug("REST request to update TypePorta : {}, {}", id, typePorta);
        if (typePorta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePorta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePortaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypePorta result = typePortaService.save(typePorta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePorta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-portas/:id} : Partial updates given fields of an existing typePorta, field will ignore if it is null
     *
     * @param id the id of the typePorta to save.
     * @param typePorta the typePorta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePorta,
     * or with status {@code 400 (Bad Request)} if the typePorta is not valid,
     * or with status {@code 404 (Not Found)} if the typePorta is not found,
     * or with status {@code 500 (Internal Server Error)} if the typePorta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-portas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypePorta> partialUpdateTypePorta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypePorta typePorta
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypePorta partially : {}, {}", id, typePorta);
        if (typePorta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePorta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePortaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypePorta> result = typePortaService.partialUpdate(typePorta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePorta.getId().toString())
        );
    }

    /**
     * {@code GET  /type-portas} : get all the typePortas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePortas in body.
     */
    @GetMapping("/type-portas")
    public ResponseEntity<List<TypePorta>> getAllTypePortas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypePortas");
        Page<TypePorta> page = typePortaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-portas/:id} : get the "id" typePorta.
     *
     * @param id the id of the typePorta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePorta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-portas/{id}")
    public ResponseEntity<TypePorta> getTypePorta(@PathVariable Long id) {
        log.debug("REST request to get TypePorta : {}", id);
        Optional<TypePorta> typePorta = typePortaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typePorta);
    }

    /**
     * {@code DELETE  /type-portas/:id} : delete the "id" typePorta.
     *
     * @param id the id of the typePorta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-portas/{id}")
    public ResponseEntity<Void> deleteTypePorta(@PathVariable Long id) {
        log.debug("REST request to delete TypePorta : {}", id);
        typePortaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
