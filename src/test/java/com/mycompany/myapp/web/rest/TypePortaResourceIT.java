package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TypePorta;
import com.mycompany.myapp.repository.TypePortaRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypePortaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypePortaResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-portas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypePortaRepository typePortaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypePortaMockMvc;

    private TypePorta typePorta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePorta createEntity(EntityManager em) {
        TypePorta typePorta = new TypePorta().libelle(DEFAULT_LIBELLE);
        return typePorta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePorta createUpdatedEntity(EntityManager em) {
        TypePorta typePorta = new TypePorta().libelle(UPDATED_LIBELLE);
        return typePorta;
    }

    @BeforeEach
    public void initTest() {
        typePorta = createEntity(em);
    }

    @Test
    @Transactional
    void createTypePorta() throws Exception {
        int databaseSizeBeforeCreate = typePortaRepository.findAll().size();
        // Create the TypePorta
        restTypePortaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePorta)))
            .andExpect(status().isCreated());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeCreate + 1);
        TypePorta testTypePorta = typePortaList.get(typePortaList.size() - 1);
        assertThat(testTypePorta.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createTypePortaWithExistingId() throws Exception {
        // Create the TypePorta with an existing ID
        typePorta.setId(1L);

        int databaseSizeBeforeCreate = typePortaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypePortaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePorta)))
            .andExpect(status().isBadRequest());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePortaRepository.findAll().size();
        // set the field null
        typePorta.setLibelle(null);

        // Create the TypePorta, which fails.

        restTypePortaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePorta)))
            .andExpect(status().isBadRequest());

        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypePortas() throws Exception {
        // Initialize the database
        typePortaRepository.saveAndFlush(typePorta);

        // Get all the typePortaList
        restTypePortaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePorta.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypePorta() throws Exception {
        // Initialize the database
        typePortaRepository.saveAndFlush(typePorta);

        // Get the typePorta
        restTypePortaMockMvc
            .perform(get(ENTITY_API_URL_ID, typePorta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typePorta.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypePorta() throws Exception {
        // Get the typePorta
        restTypePortaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypePorta() throws Exception {
        // Initialize the database
        typePortaRepository.saveAndFlush(typePorta);

        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();

        // Update the typePorta
        TypePorta updatedTypePorta = typePortaRepository.findById(typePorta.getId()).get();
        // Disconnect from session so that the updates on updatedTypePorta are not directly saved in db
        em.detach(updatedTypePorta);
        updatedTypePorta.libelle(UPDATED_LIBELLE);

        restTypePortaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypePorta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypePorta))
            )
            .andExpect(status().isOk());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
        TypePorta testTypePorta = typePortaList.get(typePortaList.size() - 1);
        assertThat(testTypePorta.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingTypePorta() throws Exception {
        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();
        typePorta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePortaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typePorta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePorta))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypePorta() throws Exception {
        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();
        typePorta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePortaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePorta))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypePorta() throws Exception {
        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();
        typePorta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePortaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePorta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypePortaWithPatch() throws Exception {
        // Initialize the database
        typePortaRepository.saveAndFlush(typePorta);

        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();

        // Update the typePorta using partial update
        TypePorta partialUpdatedTypePorta = new TypePorta();
        partialUpdatedTypePorta.setId(typePorta.getId());

        partialUpdatedTypePorta.libelle(UPDATED_LIBELLE);

        restTypePortaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePorta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePorta))
            )
            .andExpect(status().isOk());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
        TypePorta testTypePorta = typePortaList.get(typePortaList.size() - 1);
        assertThat(testTypePorta.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateTypePortaWithPatch() throws Exception {
        // Initialize the database
        typePortaRepository.saveAndFlush(typePorta);

        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();

        // Update the typePorta using partial update
        TypePorta partialUpdatedTypePorta = new TypePorta();
        partialUpdatedTypePorta.setId(typePorta.getId());

        partialUpdatedTypePorta.libelle(UPDATED_LIBELLE);

        restTypePortaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePorta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePorta))
            )
            .andExpect(status().isOk());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
        TypePorta testTypePorta = typePortaList.get(typePortaList.size() - 1);
        assertThat(testTypePorta.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingTypePorta() throws Exception {
        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();
        typePorta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePortaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typePorta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePorta))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypePorta() throws Exception {
        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();
        typePorta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePortaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePorta))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypePorta() throws Exception {
        int databaseSizeBeforeUpdate = typePortaRepository.findAll().size();
        typePorta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePortaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typePorta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePorta in the database
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypePorta() throws Exception {
        // Initialize the database
        typePortaRepository.saveAndFlush(typePorta);

        int databaseSizeBeforeDelete = typePortaRepository.findAll().size();

        // Delete the typePorta
        restTypePortaMockMvc
            .perform(delete(ENTITY_API_URL_ID, typePorta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypePorta> typePortaList = typePortaRepository.findAll();
        assertThat(typePortaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
