package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Operateur.
 */
@Entity
@Table(name = "operateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operateur implements Serializable {

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

    @Column(name = "seq")
    private Long seq;

    @Column(name = "date_seq")
    private String dateSeq;

    @Column(name = "contrat")
    private String contrat;

    @Column(name = "seq_port")
    private Long seqPort;

    @Column(name = "seq_port_ar")
    private Long seqPortAr;

    @Column(name = "seq_port_cri")
    private Long seqPortCri;

    @Column(name = "seq_port_cr")
    private Long seqPortCr;

    @Column(name = "libelle_as")
    private String libelleAs;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Operateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Operateur code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Operateur libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Long getSeq() {
        return this.seq;
    }

    public Operateur seq(Long seq) {
        this.setSeq(seq);
        return this;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getDateSeq() {
        return this.dateSeq;
    }

    public Operateur dateSeq(String dateSeq) {
        this.setDateSeq(dateSeq);
        return this;
    }

    public void setDateSeq(String dateSeq) {
        this.dateSeq = dateSeq;
    }

    public String getContrat() {
        return this.contrat;
    }

    public Operateur contrat(String contrat) {
        this.setContrat(contrat);
        return this;
    }

    public void setContrat(String contrat) {
        this.contrat = contrat;
    }

    public Long getSeqPort() {
        return this.seqPort;
    }

    public Operateur seqPort(Long seqPort) {
        this.setSeqPort(seqPort);
        return this;
    }

    public void setSeqPort(Long seqPort) {
        this.seqPort = seqPort;
    }

    public Long getSeqPortAr() {
        return this.seqPortAr;
    }

    public Operateur seqPortAr(Long seqPortAr) {
        this.setSeqPortAr(seqPortAr);
        return this;
    }

    public void setSeqPortAr(Long seqPortAr) {
        this.seqPortAr = seqPortAr;
    }

    public Long getSeqPortCri() {
        return this.seqPortCri;
    }

    public Operateur seqPortCri(Long seqPortCri) {
        this.setSeqPortCri(seqPortCri);
        return this;
    }

    public void setSeqPortCri(Long seqPortCri) {
        this.seqPortCri = seqPortCri;
    }

    public Long getSeqPortCr() {
        return this.seqPortCr;
    }

    public Operateur seqPortCr(Long seqPortCr) {
        this.setSeqPortCr(seqPortCr);
        return this;
    }

    public void setSeqPortCr(Long seqPortCr) {
        this.seqPortCr = seqPortCr;
    }

    public String getLibelleAs() {
        return this.libelleAs;
    }

    public Operateur libelleAs(String libelleAs) {
        this.setLibelleAs(libelleAs);
        return this;
    }

    public void setLibelleAs(String libelleAs) {
        this.libelleAs = libelleAs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operateur)) {
            return false;
        }
        return id != null && id.equals(((Operateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operateur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", seq=" + getSeq() +
            ", dateSeq='" + getDateSeq() + "'" +
            ", contrat='" + getContrat() + "'" +
            ", seqPort=" + getSeqPort() +
            ", seqPortAr=" + getSeqPortAr() +
            ", seqPortCri=" + getSeqPortCri() +
            ", seqPortCr=" + getSeqPortCr() +
            ", libelleAs='" + getLibelleAs() + "'" +
            "}";
    }
}
