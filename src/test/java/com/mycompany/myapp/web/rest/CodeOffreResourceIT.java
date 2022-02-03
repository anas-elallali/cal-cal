package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CodeOffre;
import com.mycompany.myapp.repository.CodeOffreRepository;
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
 * Integration tests for the {@link CodeOffreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodeOffreResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/code-offres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CodeOffreRepository codeOffreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodeOffreMockMvc;

    private CodeOffre codeOffre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeOffre createEntity(EntityManager em) {
        CodeOffre codeOffre = new CodeOffre().libelle(DEFAULT_LIBELLE);
        return codeOffre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeOffre createUpdatedEntity(EntityManager em) {
        CodeOffre codeOffre = new CodeOffre().libelle(UPDATED_LIBELLE);
        return codeOffre;
    }

    @BeforeEach
    public void initTest() {
        codeOffre = createEntity(em);
    }

    @Test
    @Transactional
    void createCodeOffre() throws Exception {
        int databaseSizeBeforeCreate = codeOffreRepository.findAll().size();
        // Create the CodeOffre
        restCodeOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeOffre)))
            .andExpect(status().isCreated());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeCreate + 1);
        CodeOffre testCodeOffre = codeOffreList.get(codeOffreList.size() - 1);
        assertThat(testCodeOffre.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createCodeOffreWithExistingId() throws Exception {
        // Create the CodeOffre with an existing ID
        codeOffre.setId(1L);

        int databaseSizeBeforeCreate = codeOffreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeOffre)))
            .andExpect(status().isBadRequest());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeOffreRepository.findAll().size();
        // set the field null
        codeOffre.setLibelle(null);

        // Create the CodeOffre, which fails.

        restCodeOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeOffre)))
            .andExpect(status().isBadRequest());

        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCodeOffres() throws Exception {
        // Initialize the database
        codeOffreRepository.saveAndFlush(codeOffre);

        // Get all the codeOffreList
        restCodeOffreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeOffre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getCodeOffre() throws Exception {
        // Initialize the database
        codeOffreRepository.saveAndFlush(codeOffre);

        // Get the codeOffre
        restCodeOffreMockMvc
            .perform(get(ENTITY_API_URL_ID, codeOffre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codeOffre.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingCodeOffre() throws Exception {
        // Get the codeOffre
        restCodeOffreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCodeOffre() throws Exception {
        // Initialize the database
        codeOffreRepository.saveAndFlush(codeOffre);

        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();

        // Update the codeOffre
        CodeOffre updatedCodeOffre = codeOffreRepository.findById(codeOffre.getId()).get();
        // Disconnect from session so that the updates on updatedCodeOffre are not directly saved in db
        em.detach(updatedCodeOffre);
        updatedCodeOffre.libelle(UPDATED_LIBELLE);

        restCodeOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCodeOffre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCodeOffre))
            )
            .andExpect(status().isOk());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
        CodeOffre testCodeOffre = codeOffreList.get(codeOffreList.size() - 1);
        assertThat(testCodeOffre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingCodeOffre() throws Exception {
        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();
        codeOffre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeOffre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeOffre))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodeOffre() throws Exception {
        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();
        codeOffre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeOffre))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodeOffre() throws Exception {
        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();
        codeOffre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeOffreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeOffre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodeOffreWithPatch() throws Exception {
        // Initialize the database
        codeOffreRepository.saveAndFlush(codeOffre);

        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();

        // Update the codeOffre using partial update
        CodeOffre partialUpdatedCodeOffre = new CodeOffre();
        partialUpdatedCodeOffre.setId(codeOffre.getId());

        partialUpdatedCodeOffre.libelle(UPDATED_LIBELLE);

        restCodeOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeOffre))
            )
            .andExpect(status().isOk());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
        CodeOffre testCodeOffre = codeOffreList.get(codeOffreList.size() - 1);
        assertThat(testCodeOffre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateCodeOffreWithPatch() throws Exception {
        // Initialize the database
        codeOffreRepository.saveAndFlush(codeOffre);

        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();

        // Update the codeOffre using partial update
        CodeOffre partialUpdatedCodeOffre = new CodeOffre();
        partialUpdatedCodeOffre.setId(codeOffre.getId());

        partialUpdatedCodeOffre.libelle(UPDATED_LIBELLE);

        restCodeOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeOffre))
            )
            .andExpect(status().isOk());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
        CodeOffre testCodeOffre = codeOffreList.get(codeOffreList.size() - 1);
        assertThat(testCodeOffre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingCodeOffre() throws Exception {
        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();
        codeOffre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codeOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeOffre))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodeOffre() throws Exception {
        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();
        codeOffre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeOffre))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodeOffre() throws Exception {
        int databaseSizeBeforeUpdate = codeOffreRepository.findAll().size();
        codeOffre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeOffreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(codeOffre))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeOffre in the database
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodeOffre() throws Exception {
        // Initialize the database
        codeOffreRepository.saveAndFlush(codeOffre);

        int databaseSizeBeforeDelete = codeOffreRepository.findAll().size();

        // Delete the codeOffre
        restCodeOffreMockMvc
            .perform(delete(ENTITY_API_URL_ID, codeOffre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CodeOffre> codeOffreList = codeOffreRepository.findAll();
        assertThat(codeOffreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
