package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Affaire.
 */
@Entity
@Table(name = "affaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Affaire implements Serializable {

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

    @Column(name = "contact")
    private String contact;

    @Column(name = "est_affiche")
    private Integer estAffiche;

    @Column(name = "com_id")
    private Long comId;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Integer actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Affaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Affaire code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Affaire libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getContact() {
        return this.contact;
    }

    public Affaire contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getEstAffiche() {
        return this.estAffiche;
    }

    public Affaire estAffiche(Integer estAffiche) {
        this.setEstAffiche(estAffiche);
        return this;
    }

    public void setEstAffiche(Integer estAffiche) {
        this.estAffiche = estAffiche;
    }

    public Long getComId() {
        return this.comId;
    }

    public Affaire comId(Long comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Long comId) {
        this.comId = comId;
    }

    public Integer getActif() {
        return this.actif;
    }

    public Affaire actif(Integer actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Integer actif) {
        this.actif = actif;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Affaire)) {
            return false;
        }
        return id != null && id.equals(((Affaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Affaire{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", contact='" + getContact() + "'" +
            ", estAffiche=" + getEstAffiche() +
            ", comId=" + getComId() +
            ", actif=" + getActif() +
            "}";
    }
}
