package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ordre;
import com.mycompany.myapp.repository.OrdreRepository;
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
 * Integration tests for the {@link OrdreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdreResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_INIT_MVM = false;
    private static final Boolean UPDATED_IS_INIT_MVM = true;

    private static final String ENTITY_API_URL = "/api/ordres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdreRepository ordreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdreMockMvc;

    private Ordre ordre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordre createEntity(EntityManager em) {
        Ordre ordre = new Ordre().libelle(DEFAULT_LIBELLE).isInitMvm(DEFAULT_IS_INIT_MVM);
        return ordre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordre createUpdatedEntity(EntityManager em) {
        Ordre ordre = new Ordre().libelle(UPDATED_LIBELLE).isInitMvm(UPDATED_IS_INIT_MVM);
        return ordre;
    }

    @BeforeEach
    public void initTest() {
        ordre = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdre() throws Exception {
        int databaseSizeBeforeCreate = ordreRepository.findAll().size();
        // Create the Ordre
        restOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isCreated());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeCreate + 1);
        Ordre testOrdre = ordreList.get(ordreList.size() - 1);
        assertThat(testOrdre.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testOrdre.getIsInitMvm()).isEqualTo(DEFAULT_IS_INIT_MVM);
    }

    @Test
    @Transactional
    void createOrdreWithExistingId() throws Exception {
        // Create the Ordre with an existing ID
        ordre.setId(1L);

        int databaseSizeBeforeCreate = ordreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordreRepository.findAll().size();
        // set the field null
        ordre.setLibelle(null);

        // Create the Ordre, which fails.

        restOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isBadRequest());

        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrdres() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList
        restOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].isInitMvm").value(hasItem(DEFAULT_IS_INIT_MVM.booleanValue())));
    }

    @Test
    @Transactional
    void getOrdre() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get the ordre
        restOrdreMockMvc
            .perform(get(ENTITY_API_URL_ID, ordre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordre.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.isInitMvm").value(DEFAULT_IS_INIT_MVM.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrdre() throws Exception {
        // Get the ordre
        restOrdreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdre() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();

        // Update the ordre
        Ordre updatedOrdre = ordreRepository.findById(ordre.getId()).get();
        // Disconnect from session so that the updates on updatedOrdre are not directly saved in db
        em.detach(updatedOrdre);
        updatedOrdre.libelle(UPDATED_LIBELLE).isInitMvm(UPDATED_IS_INIT_MVM);

        restOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdre))
            )
            .andExpect(status().isOk());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
        Ordre testOrdre = ordreList.get(ordreList.size() - 1);
        assertThat(testOrdre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOrdre.getIsInitMvm()).isEqualTo(UPDATED_IS_INIT_MVM);
    }

    @Test
    @Transactional
    void putNonExistingOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdreWithPatch() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();

        // Update the ordre using partial update
        Ordre partialUpdatedOrdre = new Ordre();
        partialUpdatedOrdre.setId(ordre.getId());

        partialUpdatedOrdre.libelle(UPDATED_LIBELLE);

        restOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdre))
            )
            .andExpect(status().isOk());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
        Ordre testOrdre = ordreList.get(ordreList.size() - 1);
        assertThat(testOrdre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOrdre.getIsInitMvm()).isEqualTo(DEFAULT_IS_INIT_MVM);
    }

    @Test
    @Transactional
    void fullUpdateOrdreWithPatch() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();

        // Update the ordre using partial update
        Ordre partialUpdatedOrdre = new Ordre();
        partialUpdatedOrdre.setId(ordre.getId());

        partialUpdatedOrdre.libelle(UPDATED_LIBELLE).isInitMvm(UPDATED_IS_INIT_MVM);

        restOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdre))
            )
            .andExpect(status().isOk());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
        Ordre testOrdre = ordreList.get(ordreList.size() - 1);
        assertThat(testOrdre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOrdre.getIsInitMvm()).isEqualTo(UPDATED_IS_INIT_MVM);
    }

    @Test
    @Transactional
    void patchNonExistingOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdre() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        int databaseSizeBeforeDelete = ordreRepository.findAll().size();

        // Delete the ordre
        restOrdreMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
