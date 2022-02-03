package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tarif.
 */
@Entity
@Table(name = "tarif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tarif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "famille")
    private String famille;

    @Column(name = "libelle_famille")
    private String libelleFamille;

    @Column(name = "tarif_communication")
    private String tarifCommunication;

    @Column(name = "ht_tarif_ala_minute")
    private Float htTarifAlaMinute;

    @Column(name = "ttc_tarif_ala_minute")
    private Float ttcTarifAlaMinute;

    @Column(name = "ht_tarif_aappel")
    private Float htTarifAappel;

    @Column(name = "ttc_tarif_aappel")
    private Float ttcTarifAappel;

    @Column(name = "date_debut_valide")
    private Instant dateDebutValide;

    @Column(name = "date_fin_valide")
    private Instant dateFinValide;

    @Column(name = "date_maj")
    private Instant dateMaj;

    @Column(name = "commentaires")
    private String commentaires;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tarif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Tarif code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Tarif libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getOrdre() {
        return this.ordre;
    }

    public Tarif ordre(Integer ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getFamille() {
        return this.famille;
    }

    public Tarif famille(String famille) {
        this.setFamille(famille);
        return this;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getLibelleFamille() {
        return this.libelleFamille;
    }

    public Tarif libelleFamille(String libelleFamille) {
        this.setLibelleFamille(libelleFamille);
        return this;
    }

    public void setLibelleFamille(String libelleFamille) {
        this.libelleFamille = libelleFamille;
    }

    public String getTarifCommunication() {
        return this.tarifCommunication;
    }

    public Tarif tarifCommunication(String tarifCommunication) {
        this.setTarifCommunication(tarifCommunication);
        return this;
    }

    public void setTarifCommunication(String tarifCommunication) {
        this.tarifCommunication = tarifCommunication;
    }

    public Float getHtTarifAlaMinute() {
        return this.htTarifAlaMinute;
    }

    public Tarif htTarifAlaMinute(Float htTarifAlaMinute) {
        this.setHtTarifAlaMinute(htTarifAlaMinute);
        return this;
    }

    public void setHtTarifAlaMinute(Float htTarifAlaMinute) {
        this.htTarifAlaMinute = htTarifAlaMinute;
    }

    public Float getTtcTarifAlaMinute() {
        return this.ttcTarifAlaMinute;
    }

    public Tarif ttcTarifAlaMinute(Float ttcTarifAlaMinute) {
        this.setTtcTarifAlaMinute(ttcTarifAlaMinute);
        return this;
    }

    public void setTtcTarifAlaMinute(Float ttcTarifAlaMinute) {
        this.ttcTarifAlaMinute = ttcTarifAlaMinute;
    }

    public Float getHtTarifAappel() {
        return this.htTarifAappel;
    }

    public Tarif htTarifAappel(Float htTarifAappel) {
        this.setHtTarifAappel(htTarifAappel);
        return this;
    }

    public void setHtTarifAappel(Float htTarifAappel) {
        this.htTarifAappel = htTarifAappel;
    }

    public Float getTtcTarifAappel() {
        return this.ttcTarifAappel;
    }

    public Tarif ttcTarifAappel(Float ttcTarifAappel) {
        this.setTtcTarifAappel(ttcTarifAappel);
        return this;
    }

    public void setTtcTarifAappel(Float ttcTarifAappel) {
        this.ttcTarifAappel = ttcTarifAappel;
    }

    public Instant getDateDebutValide() {
        return this.dateDebutValide;
    }

    public Tarif dateDebutValide(Instant dateDebutValide) {
        this.setDateDebutValide(dateDebutValide);
        return this;
    }

    public void setDateDebutValide(Instant dateDebutValide) {
        this.dateDebutValide = dateDebutValide;
    }

    public Instant getDateFinValide() {
        return this.dateFinValide;
    }

    public Tarif dateFinValide(Instant dateFinValide) {
        this.setDateFinValide(dateFinValide);
        return this;
    }

    public void setDateFinValide(Instant dateFinValide) {
        this.dateFinValide = dateFinValide;
    }

    public Instant getDateMaj() {
        return this.dateMaj;
    }

    public Tarif dateMaj(Instant dateMaj) {
        this.setDateMaj(dateMaj);
        return this;
    }

    public void setDateMaj(Instant dateMaj) {
        this.dateMaj = dateMaj;
    }

    public String getCommentaires() {
        return this.commentaires;
    }

    public Tarif commentaires(String commentaires) {
        this.setCommentaires(commentaires);
        return this;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarif)) {
            return false;
        }
        return id != null && id.equals(((Tarif) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarif{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", ordre=" + getOrdre() +
            ", famille='" + getFamille() + "'" +
            ", libelleFamille='" + getLibelleFamille() + "'" +
            ", tarifCommunication='" + getTarifCommunication() + "'" +
            ", htTarifAlaMinute=" + getHtTarifAlaMinute() +
            ", ttcTarifAlaMinute=" + getTtcTarifAlaMinute() +
            ", htTarifAappel=" + getHtTarifAappel() +
            ", ttcTarifAappel=" + getTtcTarifAappel() +
            ", dateDebutValide='" + getDateDebutValide() + "'" +
            ", dateFinValide='" + getDateFinValide() + "'" +
            ", dateMaj='" + getDateMaj() + "'" +
            ", commentaires='" + getCommentaires() + "'" +
            "}";
    }
}
