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
@Table(name = "user_has_externaldistributor")
public class User_has_externaldistributor implements Serializable {

    private static final long serialVersionUID =493669415L;
    private Boolean active;
    private Long externaldistributor_id;
    private Long id;
    private Long user_id;

    @Column(name = "active", columnDefinition = "BIT(1)")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "externaldistributor_id", columnDefinition = "BIGINT", nullable = false)
    public Long getExternaldistributor_id() {
        return externaldistributor_id;
    }

    public void setExternaldistributor_id(Long externaldistributor_id) {
        this.externaldistributor_id = externaldistributor_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "user_id", columnDefinition = "BIGINT", nullable = false)
    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User_has_externaldistributor@" + hashCode();
    }
}
