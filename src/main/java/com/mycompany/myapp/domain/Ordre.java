package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ordre.
 */
@Entity
@Table(name = "ordre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ordre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "is_init_mvm")
    private Boolean isInitMvm;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ordre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Ordre libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getIsInitMvm() {
        return this.isInitMvm;
    }

    public Ordre isInitMvm(Boolean isInitMvm) {
        this.setIsInitMvm(isInitMvm);
        return this;
    }

    public void setIsInitMvm(Boolean isInitMvm) {
        this.isInitMvm = isInitMvm;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ordre)) {
            return false;
        }
        return id != null && id.equals(((Ordre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ordre{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", isInitMvm='" + getIsInitMvm() + "'" +
            "}";
    }
}
