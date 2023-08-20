package org.bitbucket.creditauto.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.bitbucket.creditauto.entity.validator.*;

/**
 * Automatically generated.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID =1198658L;
    private Long id;
    private String name;
    private List<In_dossier> in_dossiers;
    private List<Powerofattorney> powerofattorneys;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Size(max = 100)
    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy="product")
    public List<In_dossier> getIn_dossiers() {
        return in_dossiers;
    }

    public void setIn_dossiers(List<In_dossier> in_dossiers) {
        this.in_dossiers = in_dossiers;
    }

    @OneToMany(mappedBy="product")
    public List<Powerofattorney> getPowerofattorneys() {
        return powerofattorneys;
    }

    public void setPowerofattorneys(List<Powerofattorney> powerofattorneys) {
        this.powerofattorneys = powerofattorneys;
    }

    @Override
    public String toString() {
        return "Product@" + hashCode();
    }
}
