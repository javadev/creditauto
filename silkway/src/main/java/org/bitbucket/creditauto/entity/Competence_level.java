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
 *
 */
@Entity
@Table(name = "competence_level")
public class Competence_level implements Serializable {

    private static final long serialVersionUID =-2144140881L;
    private Long id;
    private String name;
    private List<Urole> uroles;

    @Id
    @Column(name = "id", columnDefinition = "BIGINT", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Size(max = 30)
    @Column(name = "name", columnDefinition = "VARCHAR(30)", length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy="competence_level")
    public List<Urole> getUroles() {
        return uroles;
    }

    public void setUroles(List<Urole> uroles) {
        this.uroles = uroles;
    }

    @Override
    public String toString() {
        return "Competence_level@" + hashCode();
    }
}
