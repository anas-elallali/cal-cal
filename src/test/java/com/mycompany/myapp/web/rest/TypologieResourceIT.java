package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Typologie;
import com.mycompany.myapp.repository.TypologieRepository;
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
 * Integration tests for the {@link TypologieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypologieResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;

    private static final String ENTITY_API_URL = "/api/typologies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypologieRepository typologieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypologieMockMvc;

    private Typologie typologie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Typologie createEntity(EntityManager em) {
        Typologie typologie = new Typologie().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE).ordre(DEFAULT_ORDRE);
        return typologie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Typologie createUpdatedEntity(EntityManager em) {
        Typologie typologie = new Typologie().code(UPDATED_CODE).libelle(UPDATED_LIBELLE).ordre(UPDATED_ORDRE);
        return typologie;
    }

    @BeforeEach
    public void initTest() {
        typologie = createEntity(em);
    }

    @Test
    @Transactional
    void createTypologie() throws Exception {
        int databaseSizeBeforeCreate = typologieRepository.findAll().size();
        // Create the Typologie
        restTypologieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typologie)))
            .andExpect(status().isCreated());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeCreate + 1);
        Typologie testTypologie = typologieList.get(typologieList.size() - 1);
        assertThat(testTypologie.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypologie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypologie.getOrdre()).isEqualTo(DEFAULT_ORDRE);
    }

    @Test
    @Transactional
    void createTypologieWithExistingId() throws Exception {
        // Create the Typologie with an existing ID
        typologie.setId(1L);

        int databaseSizeBeforeCreate = typologieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypologieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typologie)))
            .andExpect(status().isBadRequest());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = typologieRepository.findAll().size();
        // set the field null
        typologie.setCode(null);

        // Create the Typologie, which fails.

        restTypologieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typologie)))
            .andExpect(status().isBadRequest());

        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typologieRepository.findAll().size();
        // set the field null
        typologie.setLibelle(null);

        // Create the Typologie, which fails.

        restTypologieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typologie)))
            .andExpect(status().isBadRequest());

        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = typologieRepository.findAll().size();
        // set the field null
        typologie.setOrdre(null);

        // Create the Typologie, which fails.

        restTypologieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typologie)))
            .andExpect(status().isBadRequest());

        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypologies() throws Exception {
        // Initialize the database
        typologieRepository.saveAndFlush(typologie);

        // Get all the typologieList
        restTypologieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typologie.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)));
    }

    @Test
    @Transactional
    void getTypologie() throws Exception {
        // Initialize the database
        typologieRepository.saveAndFlush(typologie);

        // Get the typologie
        restTypologieMockMvc
            .perform(get(ENTITY_API_URL_ID, typologie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typologie.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE));
    }

    @Test
    @Transactional
    void getNonExistingTypologie() throws Exception {
        // Get the typologie
        restTypologieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypologie() throws Exception {
        // Initialize the database
        typologieRepository.saveAndFlush(typologie);

        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();

        // Update the typologie
        Typologie updatedTypologie = typologieRepository.findById(typologie.getId()).get();
        // Disconnect from session so that the updates on updatedTypologie are not directly saved in db
        em.detach(updatedTypologie);
        updatedTypologie.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).ordre(UPDATED_ORDRE);

        restTypologieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypologie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypologie))
            )
            .andExpect(status().isOk());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
        Typologie testTypologie = typologieList.get(typologieList.size() - 1);
        assertThat(testTypologie.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypologie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypologie.getOrdre()).isEqualTo(UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void putNonExistingTypologie() throws Exception {
        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();
        typologie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypologieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typologie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typologie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypologie() throws Exception {
        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();
        typologie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypologieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typologie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypologie() throws Exception {
        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();
        typologie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypologieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typologie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypologieWithPatch() throws Exception {
        // Initialize the database
        typologieRepository.saveAndFlush(typologie);

        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();

        // Update the typologie using partial update
        Typologie partialUpdatedTypologie = new Typologie();
        partialUpdatedTypologie.setId(typologie.getId());

        partialUpdatedTypologie.ordre(UPDATED_ORDRE);

        restTypologieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypologie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypologie))
            )
            .andExpect(status().isOk());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
        Typologie testTypologie = typologieList.get(typologieList.size() - 1);
        assertThat(testTypologie.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypologie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypologie.getOrdre()).isEqualTo(UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void fullUpdateTypologieWithPatch() throws Exception {
        // Initialize the database
        typologieRepository.saveAndFlush(typologie);

        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();

        // Update the typologie using partial update
        Typologie partialUpdatedTypologie = new Typologie();
        partialUpdatedTypologie.setId(typologie.getId());

        partialUpdatedTypologie.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).ordre(UPDATED_ORDRE);

        restTypologieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypologie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypologie))
            )
            .andExpect(status().isOk());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
        Typologie testTypologie = typologieList.get(typologieList.size() - 1);
        assertThat(testTypologie.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypologie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypologie.getOrdre()).isEqualTo(UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void patchNonExistingTypologie() throws Exception {
        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();
        typologie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypologieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typologie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typologie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypologie() throws Exception {
        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();
        typologie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypologieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typologie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypologie() throws Exception {
        int databaseSizeBeforeUpdate = typologieRepository.findAll().size();
        typologie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypologieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typologie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Typologie in the database
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypologie() throws Exception {
        // Initialize the database
        typologieRepository.saveAndFlush(typologie);

        int databaseSizeBeforeDelete = typologieRepository.findAll().size();

        // Delete the typologie
        restTypologieMockMvc
            .perform(delete(ENTITY_API_URL_ID, typologie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Typologie> typologieList = typologieRepository.findAll();
        assertThat(typologieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
