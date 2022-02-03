package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Affaire;
import com.mycompany.myapp.repository.AffaireRepository;
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
 * Integration tests for the {@link AffaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AffaireResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final Integer DEFAULT_EST_AFFICHE = 1;
    private static final Integer UPDATED_EST_AFFICHE = 2;

    private static final Long DEFAULT_COM_ID = 1L;
    private static final Long UPDATED_COM_ID = 2L;

    private static final Integer DEFAULT_ACTIF = 1;
    private static final Integer UPDATED_ACTIF = 2;

    private static final String ENTITY_API_URL = "/api/affaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffaireRepository affaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffaireMockMvc;

    private Affaire affaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affaire createEntity(EntityManager em) {
        Affaire affaire = new Affaire()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .contact(DEFAULT_CONTACT)
            .estAffiche(DEFAULT_EST_AFFICHE)
            .comId(DEFAULT_COM_ID)
            .actif(DEFAULT_ACTIF);
        return affaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affaire createUpdatedEntity(EntityManager em) {
        Affaire affaire = new Affaire()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .contact(UPDATED_CONTACT)
            .estAffiche(UPDATED_EST_AFFICHE)
            .comId(UPDATED_COM_ID)
            .actif(UPDATED_ACTIF);
        return affaire;
    }

    @BeforeEach
    public void initTest() {
        affaire = createEntity(em);
    }

    @Test
    @Transactional
    void createAffaire() throws Exception {
        int databaseSizeBeforeCreate = affaireRepository.findAll().size();
        // Create the Affaire
        restAffaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affaire)))
            .andExpect(status().isCreated());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeCreate + 1);
        Affaire testAffaire = affaireList.get(affaireList.size() - 1);
        assertThat(testAffaire.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAffaire.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testAffaire.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testAffaire.getEstAffiche()).isEqualTo(DEFAULT_EST_AFFICHE);
        assertThat(testAffaire.getComId()).isEqualTo(DEFAULT_COM_ID);
        assertThat(testAffaire.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void createAffaireWithExistingId() throws Exception {
        // Create the Affaire with an existing ID
        affaire.setId(1L);

        int databaseSizeBeforeCreate = affaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affaire)))
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = affaireRepository.findAll().size();
        // set the field null
        affaire.setCode(null);

        // Create the Affaire, which fails.

        restAffaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affaire)))
            .andExpect(status().isBadRequest());

        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = affaireRepository.findAll().size();
        // set the field null
        affaire.setLibelle(null);

        // Create the Affaire, which fails.

        restAffaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affaire)))
            .andExpect(status().isBadRequest());

        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        int databaseSizeBeforeTest = affaireRepository.findAll().size();
        // set the field null
        affaire.setActif(null);

        // Create the Affaire, which fails.

        restAffaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affaire)))
            .andExpect(status().isBadRequest());

        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAffaires() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        // Get all the affaireList
        restAffaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].estAffiche").value(hasItem(DEFAULT_EST_AFFICHE)))
            .andExpect(jsonPath("$.[*].comId").value(hasItem(DEFAULT_COM_ID.intValue())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getAffaire() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        // Get the affaire
        restAffaireMockMvc
            .perform(get(ENTITY_API_URL_ID, affaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affaire.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.estAffiche").value(DEFAULT_EST_AFFICHE))
            .andExpect(jsonPath("$.comId").value(DEFAULT_COM_ID.intValue()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getNonExistingAffaire() throws Exception {
        // Get the affaire
        restAffaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAffaire() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();

        // Update the affaire
        Affaire updatedAffaire = affaireRepository.findById(affaire.getId()).get();
        // Disconnect from session so that the updates on updatedAffaire are not directly saved in db
        em.detach(updatedAffaire);
        updatedAffaire
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .contact(UPDATED_CONTACT)
            .estAffiche(UPDATED_EST_AFFICHE)
            .comId(UPDATED_COM_ID)
            .actif(UPDATED_ACTIF);

        restAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAffaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAffaire))
            )
            .andExpect(status().isOk());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
        Affaire testAffaire = affaireList.get(affaireList.size() - 1);
        assertThat(testAffaire.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAffaire.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAffaire.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testAffaire.getEstAffiche()).isEqualTo(UPDATED_EST_AFFICHE);
        assertThat(testAffaire.getComId()).isEqualTo(UPDATED_COM_ID);
        assertThat(testAffaire.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void putNonExistingAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffaireWithPatch() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();

        // Update the affaire using partial update
        Affaire partialUpdatedAffaire = new Affaire();
        partialUpdatedAffaire.setId(affaire.getId());

        partialUpdatedAffaire.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).estAffiche(UPDATED_EST_AFFICHE).comId(UPDATED_COM_ID);

        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffaire))
            )
            .andExpect(status().isOk());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
        Affaire testAffaire = affaireList.get(affaireList.size() - 1);
        assertThat(testAffaire.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAffaire.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAffaire.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testAffaire.getEstAffiche()).isEqualTo(UPDATED_EST_AFFICHE);
        assertThat(testAffaire.getComId()).isEqualTo(UPDATED_COM_ID);
        assertThat(testAffaire.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void fullUpdateAffaireWithPatch() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();

        // Update the affaire using partial update
        Affaire partialUpdatedAffaire = new Affaire();
        partialUpdatedAffaire.setId(affaire.getId());

        partialUpdatedAffaire
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .contact(UPDATED_CONTACT)
            .estAffiche(UPDATED_EST_AFFICHE)
            .comId(UPDATED_COM_ID)
            .actif(UPDATED_ACTIF);

        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffaire))
            )
            .andExpect(status().isOk());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
        Affaire testAffaire = affaireList.get(affaireList.size() - 1);
        assertThat(testAffaire.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAffaire.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAffaire.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testAffaire.getEstAffiche()).isEqualTo(UPDATED_EST_AFFICHE);
        assertThat(testAffaire.getComId()).isEqualTo(UPDATED_COM_ID);
        assertThat(testAffaire.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void patchNonExistingAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(affaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffaire() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        int databaseSizeBeforeDelete = affaireRepository.findAll().size();

        // Delete the affaire
        restAffaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, affaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
