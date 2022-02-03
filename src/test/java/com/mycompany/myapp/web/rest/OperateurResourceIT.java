package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Operateur;
import com.mycompany.myapp.repository.OperateurRepository;
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
 * Integration tests for the {@link OperateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperateurResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Long DEFAULT_SEQ = 1L;
    private static final Long UPDATED_SEQ = 2L;

    private static final String DEFAULT_DATE_SEQ = "AAAAAAAAAA";
    private static final String UPDATED_DATE_SEQ = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRAT = "AAAAAAAAAA";
    private static final String UPDATED_CONTRAT = "BBBBBBBBBB";

    private static final Long DEFAULT_SEQ_PORT = 1L;
    private static final Long UPDATED_SEQ_PORT = 2L;

    private static final Long DEFAULT_SEQ_PORT_AR = 1L;
    private static final Long UPDATED_SEQ_PORT_AR = 2L;

    private static final Long DEFAULT_SEQ_PORT_CRI = 1L;
    private static final Long UPDATED_SEQ_PORT_CRI = 2L;

    private static final Long DEFAULT_SEQ_PORT_CR = 1L;
    private static final Long UPDATED_SEQ_PORT_CR = 2L;

    private static final String DEFAULT_LIBELLE_AS = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_AS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperateurRepository operateurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperateurMockMvc;

    private Operateur operateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operateur createEntity(EntityManager em) {
        Operateur operateur = new Operateur()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .seq(DEFAULT_SEQ)
            .dateSeq(DEFAULT_DATE_SEQ)
            .contrat(DEFAULT_CONTRAT)
            .seqPort(DEFAULT_SEQ_PORT)
            .seqPortAr(DEFAULT_SEQ_PORT_AR)
            .seqPortCri(DEFAULT_SEQ_PORT_CRI)
            .seqPortCr(DEFAULT_SEQ_PORT_CR)
            .libelleAs(DEFAULT_LIBELLE_AS);
        return operateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operateur createUpdatedEntity(EntityManager em) {
        Operateur operateur = new Operateur()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .seq(UPDATED_SEQ)
            .dateSeq(UPDATED_DATE_SEQ)
            .contrat(UPDATED_CONTRAT)
            .seqPort(UPDATED_SEQ_PORT)
            .seqPortAr(UPDATED_SEQ_PORT_AR)
            .seqPortCri(UPDATED_SEQ_PORT_CRI)
            .seqPortCr(UPDATED_SEQ_PORT_CR)
            .libelleAs(UPDATED_LIBELLE_AS);
        return operateur;
    }

    @BeforeEach
    public void initTest() {
        operateur = createEntity(em);
    }

    @Test
    @Transactional
    void createOperateur() throws Exception {
        int databaseSizeBeforeCreate = operateurRepository.findAll().size();
        // Create the Operateur
        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operateur)))
            .andExpect(status().isCreated());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeCreate + 1);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOperateur.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testOperateur.getSeq()).isEqualTo(DEFAULT_SEQ);
        assertThat(testOperateur.getDateSeq()).isEqualTo(DEFAULT_DATE_SEQ);
        assertThat(testOperateur.getContrat()).isEqualTo(DEFAULT_CONTRAT);
        assertThat(testOperateur.getSeqPort()).isEqualTo(DEFAULT_SEQ_PORT);
        assertThat(testOperateur.getSeqPortAr()).isEqualTo(DEFAULT_SEQ_PORT_AR);
        assertThat(testOperateur.getSeqPortCri()).isEqualTo(DEFAULT_SEQ_PORT_CRI);
        assertThat(testOperateur.getSeqPortCr()).isEqualTo(DEFAULT_SEQ_PORT_CR);
        assertThat(testOperateur.getLibelleAs()).isEqualTo(DEFAULT_LIBELLE_AS);
    }

    @Test
    @Transactional
    void createOperateurWithExistingId() throws Exception {
        // Create the Operateur with an existing ID
        operateur.setId(1L);

        int databaseSizeBeforeCreate = operateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operateur)))
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = operateurRepository.findAll().size();
        // set the field null
        operateur.setCode(null);

        // Create the Operateur, which fails.

        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operateur)))
            .andExpect(status().isBadRequest());

        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = operateurRepository.findAll().size();
        // set the field null
        operateur.setLibelle(null);

        // Create the Operateur, which fails.

        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operateur)))
            .andExpect(status().isBadRequest());

        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperateurs() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].seq").value(hasItem(DEFAULT_SEQ.intValue())))
            .andExpect(jsonPath("$.[*].dateSeq").value(hasItem(DEFAULT_DATE_SEQ)))
            .andExpect(jsonPath("$.[*].contrat").value(hasItem(DEFAULT_CONTRAT)))
            .andExpect(jsonPath("$.[*].seqPort").value(hasItem(DEFAULT_SEQ_PORT.intValue())))
            .andExpect(jsonPath("$.[*].seqPortAr").value(hasItem(DEFAULT_SEQ_PORT_AR.intValue())))
            .andExpect(jsonPath("$.[*].seqPortCri").value(hasItem(DEFAULT_SEQ_PORT_CRI.intValue())))
            .andExpect(jsonPath("$.[*].seqPortCr").value(hasItem(DEFAULT_SEQ_PORT_CR.intValue())))
            .andExpect(jsonPath("$.[*].libelleAs").value(hasItem(DEFAULT_LIBELLE_AS)));
    }

    @Test
    @Transactional
    void getOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        // Get the operateur
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL_ID, operateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operateur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.seq").value(DEFAULT_SEQ.intValue()))
            .andExpect(jsonPath("$.dateSeq").value(DEFAULT_DATE_SEQ))
            .andExpect(jsonPath("$.contrat").value(DEFAULT_CONTRAT))
            .andExpect(jsonPath("$.seqPort").value(DEFAULT_SEQ_PORT.intValue()))
            .andExpect(jsonPath("$.seqPortAr").value(DEFAULT_SEQ_PORT_AR.intValue()))
            .andExpect(jsonPath("$.seqPortCri").value(DEFAULT_SEQ_PORT_CRI.intValue()))
            .andExpect(jsonPath("$.seqPortCr").value(DEFAULT_SEQ_PORT_CR.intValue()))
            .andExpect(jsonPath("$.libelleAs").value(DEFAULT_LIBELLE_AS));
    }

    @Test
    @Transactional
    void getNonExistingOperateur() throws Exception {
        // Get the operateur
        restOperateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur
        Operateur updatedOperateur = operateurRepository.findById(operateur.getId()).get();
        // Disconnect from session so that the updates on updatedOperateur are not directly saved in db
        em.detach(updatedOperateur);
        updatedOperateur
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .seq(UPDATED_SEQ)
            .dateSeq(UPDATED_DATE_SEQ)
            .contrat(UPDATED_CONTRAT)
            .seqPort(UPDATED_SEQ_PORT)
            .seqPortAr(UPDATED_SEQ_PORT_AR)
            .seqPortCri(UPDATED_SEQ_PORT_CRI)
            .seqPortCr(UPDATED_SEQ_PORT_CR)
            .libelleAs(UPDATED_LIBELLE_AS);

        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOperateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOperateur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOperateur.getSeq()).isEqualTo(UPDATED_SEQ);
        assertThat(testOperateur.getDateSeq()).isEqualTo(UPDATED_DATE_SEQ);
        assertThat(testOperateur.getContrat()).isEqualTo(UPDATED_CONTRAT);
        assertThat(testOperateur.getSeqPort()).isEqualTo(UPDATED_SEQ_PORT);
        assertThat(testOperateur.getSeqPortAr()).isEqualTo(UPDATED_SEQ_PORT_AR);
        assertThat(testOperateur.getSeqPortCri()).isEqualTo(UPDATED_SEQ_PORT_CRI);
        assertThat(testOperateur.getSeqPortCr()).isEqualTo(UPDATED_SEQ_PORT_CR);
        assertThat(testOperateur.getLibelleAs()).isEqualTo(UPDATED_LIBELLE_AS);
    }

    @Test
    @Transactional
    void putNonExistingOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operateur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperateurWithPatch() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur using partial update
        Operateur partialUpdatedOperateur = new Operateur();
        partialUpdatedOperateur.setId(operateur.getId());

        partialUpdatedOperateur
            .libelle(UPDATED_LIBELLE)
            .dateSeq(UPDATED_DATE_SEQ)
            .contrat(UPDATED_CONTRAT)
            .seqPort(UPDATED_SEQ_PORT)
            .seqPortCri(UPDATED_SEQ_PORT_CRI)
            .seqPortCr(UPDATED_SEQ_PORT_CR);

        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOperateur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOperateur.getSeq()).isEqualTo(DEFAULT_SEQ);
        assertThat(testOperateur.getDateSeq()).isEqualTo(UPDATED_DATE_SEQ);
        assertThat(testOperateur.getContrat()).isEqualTo(UPDATED_CONTRAT);
        assertThat(testOperateur.getSeqPort()).isEqualTo(UPDATED_SEQ_PORT);
        assertThat(testOperateur.getSeqPortAr()).isEqualTo(DEFAULT_SEQ_PORT_AR);
        assertThat(testOperateur.getSeqPortCri()).isEqualTo(UPDATED_SEQ_PORT_CRI);
        assertThat(testOperateur.getSeqPortCr()).isEqualTo(UPDATED_SEQ_PORT_CR);
        assertThat(testOperateur.getLibelleAs()).isEqualTo(DEFAULT_LIBELLE_AS);
    }

    @Test
    @Transactional
    void fullUpdateOperateurWithPatch() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur using partial update
        Operateur partialUpdatedOperateur = new Operateur();
        partialUpdatedOperateur.setId(operateur.getId());

        partialUpdatedOperateur
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .seq(UPDATED_SEQ)
            .dateSeq(UPDATED_DATE_SEQ)
            .contrat(UPDATED_CONTRAT)
            .seqPort(UPDATED_SEQ_PORT)
            .seqPortAr(UPDATED_SEQ_PORT_AR)
            .seqPortCri(UPDATED_SEQ_PORT_CRI)
            .seqPortCr(UPDATED_SEQ_PORT_CR)
            .libelleAs(UPDATED_LIBELLE_AS);

        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOperateur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOperateur.getSeq()).isEqualTo(UPDATED_SEQ);
        assertThat(testOperateur.getDateSeq()).isEqualTo(UPDATED_DATE_SEQ);
        assertThat(testOperateur.getContrat()).isEqualTo(UPDATED_CONTRAT);
        assertThat(testOperateur.getSeqPort()).isEqualTo(UPDATED_SEQ_PORT);
        assertThat(testOperateur.getSeqPortAr()).isEqualTo(UPDATED_SEQ_PORT_AR);
        assertThat(testOperateur.getSeqPortCri()).isEqualTo(UPDATED_SEQ_PORT_CRI);
        assertThat(testOperateur.getSeqPortCr()).isEqualTo(UPDATED_SEQ_PORT_CR);
        assertThat(testOperateur.getLibelleAs()).isEqualTo(UPDATED_LIBELLE_AS);
    }

    @Test
    @Transactional
    void patchNonExistingOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeDelete = operateurRepository.findAll().size();

        // Delete the operateur
        restOperateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, operateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
