package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Tarif;
import com.mycompany.myapp.repository.TarifRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TarifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TarifResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FAMILLE = "BBBBBBBBBB";

    private static final String DEFAULT_TARIF_COMMUNICATION = "AAAAAAAAAA";
    private static final String UPDATED_TARIF_COMMUNICATION = "BBBBBBBBBB";

    private static final Float DEFAULT_HT_TARIF_ALA_MINUTE = 1F;
    private static final Float UPDATED_HT_TARIF_ALA_MINUTE = 2F;

    private static final Float DEFAULT_TTC_TARIF_ALA_MINUTE = 1F;
    private static final Float UPDATED_TTC_TARIF_ALA_MINUTE = 2F;

    private static final Float DEFAULT_HT_TARIF_AAPPEL = 1F;
    private static final Float UPDATED_HT_TARIF_AAPPEL = 2F;

    private static final Float DEFAULT_TTC_TARIF_AAPPEL = 1F;
    private static final Float UPDATED_TTC_TARIF_AAPPEL = 2F;

    private static final Instant DEFAULT_DATE_DEBUT_VALIDE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT_VALIDE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN_VALIDE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN_VALIDE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MAJ = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MAJ = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTAIRES = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tarifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TarifRepository tarifRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTarifMockMvc;

    private Tarif tarif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarif createEntity(EntityManager em) {
        Tarif tarif = new Tarif()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .ordre(DEFAULT_ORDRE)
            .famille(DEFAULT_FAMILLE)
            .libelleFamille(DEFAULT_LIBELLE_FAMILLE)
            .tarifCommunication(DEFAULT_TARIF_COMMUNICATION)
            .htTarifAlaMinute(DEFAULT_HT_TARIF_ALA_MINUTE)
            .ttcTarifAlaMinute(DEFAULT_TTC_TARIF_ALA_MINUTE)
            .htTarifAappel(DEFAULT_HT_TARIF_AAPPEL)
            .ttcTarifAappel(DEFAULT_TTC_TARIF_AAPPEL)
            .dateDebutValide(DEFAULT_DATE_DEBUT_VALIDE)
            .dateFinValide(DEFAULT_DATE_FIN_VALIDE)
            .dateMaj(DEFAULT_DATE_MAJ)
            .commentaires(DEFAULT_COMMENTAIRES);
        return tarif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarif createUpdatedEntity(EntityManager em) {
        Tarif tarif = new Tarif()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .libelleFamille(UPDATED_LIBELLE_FAMILLE)
            .tarifCommunication(UPDATED_TARIF_COMMUNICATION)
            .htTarifAlaMinute(UPDATED_HT_TARIF_ALA_MINUTE)
            .ttcTarifAlaMinute(UPDATED_TTC_TARIF_ALA_MINUTE)
            .htTarifAappel(UPDATED_HT_TARIF_AAPPEL)
            .ttcTarifAappel(UPDATED_TTC_TARIF_AAPPEL)
            .dateDebutValide(UPDATED_DATE_DEBUT_VALIDE)
            .dateFinValide(UPDATED_DATE_FIN_VALIDE)
            .dateMaj(UPDATED_DATE_MAJ)
            .commentaires(UPDATED_COMMENTAIRES);
        return tarif;
    }

    @BeforeEach
    public void initTest() {
        tarif = createEntity(em);
    }

    @Test
    @Transactional
    void createTarif() throws Exception {
        int databaseSizeBeforeCreate = tarifRepository.findAll().size();
        // Create the Tarif
        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isCreated());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeCreate + 1);
        Tarif testTarif = tarifList.get(tarifList.size() - 1);
        assertThat(testTarif.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTarif.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTarif.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testTarif.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testTarif.getLibelleFamille()).isEqualTo(DEFAULT_LIBELLE_FAMILLE);
        assertThat(testTarif.getTarifCommunication()).isEqualTo(DEFAULT_TARIF_COMMUNICATION);
        assertThat(testTarif.getHtTarifAlaMinute()).isEqualTo(DEFAULT_HT_TARIF_ALA_MINUTE);
        assertThat(testTarif.getTtcTarifAlaMinute()).isEqualTo(DEFAULT_TTC_TARIF_ALA_MINUTE);
        assertThat(testTarif.getHtTarifAappel()).isEqualTo(DEFAULT_HT_TARIF_AAPPEL);
        assertThat(testTarif.getTtcTarifAappel()).isEqualTo(DEFAULT_TTC_TARIF_AAPPEL);
        assertThat(testTarif.getDateDebutValide()).isEqualTo(DEFAULT_DATE_DEBUT_VALIDE);
        assertThat(testTarif.getDateFinValide()).isEqualTo(DEFAULT_DATE_FIN_VALIDE);
        assertThat(testTarif.getDateMaj()).isEqualTo(DEFAULT_DATE_MAJ);
        assertThat(testTarif.getCommentaires()).isEqualTo(DEFAULT_COMMENTAIRES);
    }

    @Test
    @Transactional
    void createTarifWithExistingId() throws Exception {
        // Create the Tarif with an existing ID
        tarif.setId(1L);

        int databaseSizeBeforeCreate = tarifRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarifRepository.findAll().size();
        // set the field null
        tarif.setCode(null);

        // Create the Tarif, which fails.

        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isBadRequest());

        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarifRepository.findAll().size();
        // set the field null
        tarif.setLibelle(null);

        // Create the Tarif, which fails.

        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isBadRequest());

        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTarifs() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarif.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)))
            .andExpect(jsonPath("$.[*].libelleFamille").value(hasItem(DEFAULT_LIBELLE_FAMILLE)))
            .andExpect(jsonPath("$.[*].tarifCommunication").value(hasItem(DEFAULT_TARIF_COMMUNICATION)))
            .andExpect(jsonPath("$.[*].htTarifAlaMinute").value(hasItem(DEFAULT_HT_TARIF_ALA_MINUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].ttcTarifAlaMinute").value(hasItem(DEFAULT_TTC_TARIF_ALA_MINUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].htTarifAappel").value(hasItem(DEFAULT_HT_TARIF_AAPPEL.doubleValue())))
            .andExpect(jsonPath("$.[*].ttcTarifAappel").value(hasItem(DEFAULT_TTC_TARIF_AAPPEL.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDebutValide").value(hasItem(DEFAULT_DATE_DEBUT_VALIDE.toString())))
            .andExpect(jsonPath("$.[*].dateFinValide").value(hasItem(DEFAULT_DATE_FIN_VALIDE.toString())))
            .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ.toString())))
            .andExpect(jsonPath("$.[*].commentaires").value(hasItem(DEFAULT_COMMENTAIRES)));
    }

    @Test
    @Transactional
    void getTarif() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get the tarif
        restTarifMockMvc
            .perform(get(ENTITY_API_URL_ID, tarif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarif.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE))
            .andExpect(jsonPath("$.libelleFamille").value(DEFAULT_LIBELLE_FAMILLE))
            .andExpect(jsonPath("$.tarifCommunication").value(DEFAULT_TARIF_COMMUNICATION))
            .andExpect(jsonPath("$.htTarifAlaMinute").value(DEFAULT_HT_TARIF_ALA_MINUTE.doubleValue()))
            .andExpect(jsonPath("$.ttcTarifAlaMinute").value(DEFAULT_TTC_TARIF_ALA_MINUTE.doubleValue()))
            .andExpect(jsonPath("$.htTarifAappel").value(DEFAULT_HT_TARIF_AAPPEL.doubleValue()))
            .andExpect(jsonPath("$.ttcTarifAappel").value(DEFAULT_TTC_TARIF_AAPPEL.doubleValue()))
            .andExpect(jsonPath("$.dateDebutValide").value(DEFAULT_DATE_DEBUT_VALIDE.toString()))
            .andExpect(jsonPath("$.dateFinValide").value(DEFAULT_DATE_FIN_VALIDE.toString()))
            .andExpect(jsonPath("$.dateMaj").value(DEFAULT_DATE_MAJ.toString()))
            .andExpect(jsonPath("$.commentaires").value(DEFAULT_COMMENTAIRES));
    }

    @Test
    @Transactional
    void getNonExistingTarif() throws Exception {
        // Get the tarif
        restTarifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTarif() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();

        // Update the tarif
        Tarif updatedTarif = tarifRepository.findById(tarif.getId()).get();
        // Disconnect from session so that the updates on updatedTarif are not directly saved in db
        em.detach(updatedTarif);
        updatedTarif
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .libelleFamille(UPDATED_LIBELLE_FAMILLE)
            .tarifCommunication(UPDATED_TARIF_COMMUNICATION)
            .htTarifAlaMinute(UPDATED_HT_TARIF_ALA_MINUTE)
            .ttcTarifAlaMinute(UPDATED_TTC_TARIF_ALA_MINUTE)
            .htTarifAappel(UPDATED_HT_TARIF_AAPPEL)
            .ttcTarifAappel(UPDATED_TTC_TARIF_AAPPEL)
            .dateDebutValide(UPDATED_DATE_DEBUT_VALIDE)
            .dateFinValide(UPDATED_DATE_FIN_VALIDE)
            .dateMaj(UPDATED_DATE_MAJ)
            .commentaires(UPDATED_COMMENTAIRES);

        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTarif.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTarif))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
        Tarif testTarif = tarifList.get(tarifList.size() - 1);
        assertThat(testTarif.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTarif.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTarif.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testTarif.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testTarif.getLibelleFamille()).isEqualTo(UPDATED_LIBELLE_FAMILLE);
        assertThat(testTarif.getTarifCommunication()).isEqualTo(UPDATED_TARIF_COMMUNICATION);
        assertThat(testTarif.getHtTarifAlaMinute()).isEqualTo(UPDATED_HT_TARIF_ALA_MINUTE);
        assertThat(testTarif.getTtcTarifAlaMinute()).isEqualTo(UPDATED_TTC_TARIF_ALA_MINUTE);
        assertThat(testTarif.getHtTarifAappel()).isEqualTo(UPDATED_HT_TARIF_AAPPEL);
        assertThat(testTarif.getTtcTarifAappel()).isEqualTo(UPDATED_TTC_TARIF_AAPPEL);
        assertThat(testTarif.getDateDebutValide()).isEqualTo(UPDATED_DATE_DEBUT_VALIDE);
        assertThat(testTarif.getDateFinValide()).isEqualTo(UPDATED_DATE_FIN_VALIDE);
        assertThat(testTarif.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
        assertThat(testTarif.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
    }

    @Test
    @Transactional
    void putNonExistingTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarif.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTarifWithPatch() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();

        // Update the tarif using partial update
        Tarif partialUpdatedTarif = new Tarif();
        partialUpdatedTarif.setId(tarif.getId());

        partialUpdatedTarif
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .ordre(UPDATED_ORDRE)
            .htTarifAlaMinute(UPDATED_HT_TARIF_ALA_MINUTE)
            .ttcTarifAlaMinute(UPDATED_TTC_TARIF_ALA_MINUTE)
            .ttcTarifAappel(UPDATED_TTC_TARIF_AAPPEL)
            .dateFinValide(UPDATED_DATE_FIN_VALIDE)
            .dateMaj(UPDATED_DATE_MAJ)
            .commentaires(UPDATED_COMMENTAIRES);

        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarif))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
        Tarif testTarif = tarifList.get(tarifList.size() - 1);
        assertThat(testTarif.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTarif.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTarif.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testTarif.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testTarif.getLibelleFamille()).isEqualTo(DEFAULT_LIBELLE_FAMILLE);
        assertThat(testTarif.getTarifCommunication()).isEqualTo(DEFAULT_TARIF_COMMUNICATION);
        assertThat(testTarif.getHtTarifAlaMinute()).isEqualTo(UPDATED_HT_TARIF_ALA_MINUTE);
        assertThat(testTarif.getTtcTarifAlaMinute()).isEqualTo(UPDATED_TTC_TARIF_ALA_MINUTE);
        assertThat(testTarif.getHtTarifAappel()).isEqualTo(DEFAULT_HT_TARIF_AAPPEL);
        assertThat(testTarif.getTtcTarifAappel()).isEqualTo(UPDATED_TTC_TARIF_AAPPEL);
        assertThat(testTarif.getDateDebutValide()).isEqualTo(DEFAULT_DATE_DEBUT_VALIDE);
        assertThat(testTarif.getDateFinValide()).isEqualTo(UPDATED_DATE_FIN_VALIDE);
        assertThat(testTarif.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
        assertThat(testTarif.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
    }

    @Test
    @Transactional
    void fullUpdateTarifWithPatch() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();

        // Update the tarif using partial update
        Tarif partialUpdatedTarif = new Tarif();
        partialUpdatedTarif.setId(tarif.getId());

        partialUpdatedTarif
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .libelleFamille(UPDATED_LIBELLE_FAMILLE)
            .tarifCommunication(UPDATED_TARIF_COMMUNICATION)
            .htTarifAlaMinute(UPDATED_HT_TARIF_ALA_MINUTE)
            .ttcTarifAlaMinute(UPDATED_TTC_TARIF_ALA_MINUTE)
            .htTarifAappel(UPDATED_HT_TARIF_AAPPEL)
            .ttcTarifAappel(UPDATED_TTC_TARIF_AAPPEL)
            .dateDebutValide(UPDATED_DATE_DEBUT_VALIDE)
            .dateFinValide(UPDATED_DATE_FIN_VALIDE)
            .dateMaj(UPDATED_DATE_MAJ)
            .commentaires(UPDATED_COMMENTAIRES);

        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarif))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
        Tarif testTarif = tarifList.get(tarifList.size() - 1);
        assertThat(testTarif.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTarif.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTarif.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testTarif.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testTarif.getLibelleFamille()).isEqualTo(UPDATED_LIBELLE_FAMILLE);
        assertThat(testTarif.getTarifCommunication()).isEqualTo(UPDATED_TARIF_COMMUNICATION);
        assertThat(testTarif.getHtTarifAlaMinute()).isEqualTo(UPDATED_HT_TARIF_ALA_MINUTE);
        assertThat(testTarif.getTtcTarifAlaMinute()).isEqualTo(UPDATED_TTC_TARIF_ALA_MINUTE);
        assertThat(testTarif.getHtTarifAappel()).isEqualTo(UPDATED_HT_TARIF_AAPPEL);
        assertThat(testTarif.getTtcTarifAappel()).isEqualTo(UPDATED_TTC_TARIF_AAPPEL);
        assertThat(testTarif.getDateDebutValide()).isEqualTo(UPDATED_DATE_DEBUT_VALIDE);
        assertThat(testTarif.getDateFinValide()).isEqualTo(UPDATED_DATE_FIN_VALIDE);
        assertThat(testTarif.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
        assertThat(testTarif.getCommentaires()).isEqualTo(UPDATED_COMMENTAIRES);
    }

    @Test
    @Transactional
    void patchNonExistingTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarif() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        int databaseSizeBeforeDelete = tarifRepository.findAll().size();

        // Delete the tarif
        restTarifMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
