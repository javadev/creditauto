/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.entity;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Automatically generated.
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class In_instance implements Serializable {

    private static final long serialVersionUID =-835409328L;
    private In_dossier in_dossier;
    private In_person in_person;
    private In_person in_person_partner;
    private In_person in_guarantor;
    private In_person in_guarantor_partner;
    private List<In_good> in_goods;
    private In_third_person in_third_person;
    private List<In_document_store> in_document_stores;
    private List<In_asset> in_assets;

    public In_instance(boolean initFields) {
        if (initFields) {
            in_dossier = new In_dossier();
            in_person = new In_person();
            in_person.setDict_client_type("1");
            in_person_partner = new In_person();
            in_person_partner.setDict_client_type("2");
            in_guarantor = new In_person();
            in_guarantor.setDict_client_type("3");
            in_guarantor_partner = new In_person();
            in_guarantor_partner.setDict_client_type("4");
            in_goods = new ArrayList<In_good>();
            in_third_person = new In_third_person();
            in_document_stores = new ArrayList<In_document_store>();
            in_assets = new ArrayList<In_asset>();
            in_person.setIn_dossier(in_dossier);
            in_person_partner.setIn_dossier(in_dossier);
            in_guarantor.setIn_dossier(in_dossier);
            in_guarantor_partner.setIn_dossier(in_dossier);
            In_good in_good = new In_good();
            in_good.setIn_dossier(in_dossier);
            in_goods.add(in_good);
            in_third_person.setIn_dossier(in_dossier);
            In_document_store in_document_store = new In_document_store();
            in_document_store.setIn_dossier(in_dossier);
            in_document_stores.add(in_document_store);
            in_document_store = new In_document_store();
            in_document_store.setIn_dossier(in_dossier);
            in_document_stores.add(in_document_store);
            in_document_store = new In_document_store();
            in_document_store.setIn_dossier(in_dossier);
            in_document_stores.add(in_document_store);
            In_asset in_asset = new In_asset();
            in_asset.setIn_dossier(in_dossier);
            in_assets.add(in_asset);
            in_asset = new In_asset();
            in_asset.setIn_dossier(in_dossier);
            in_assets.add(in_asset);
            in_asset = new In_asset();
            in_asset.setIn_dossier(in_dossier);
            in_assets.add(in_asset);
            in_asset = new In_asset();
            in_asset.setIn_dossier(in_dossier);
            in_assets.add(in_asset);
            in_dossier.setIn_bloknots(new ArrayList<In_bloknot>());
        }
    }

    public In_dossier getIn_dossier() {
        return in_dossier;
    }

    public void setIn_dossier(In_dossier in_dossier) {
        this.in_dossier = in_dossier;
    }

    public In_person getIn_person() {
        return in_person;
    }

    public void setIn_person(In_person in_person) {
        this.in_person = in_person;
    }

    public In_person getIn_person_partner() {
        return in_person_partner;
    }

    public void setIn_person_partner(In_person in_person_partner) {
        this.in_person_partner = in_person_partner;
    }

    public In_person getIn_guarantor() {
        return in_guarantor;
    }

    public void setIn_guarantor(In_person in_guarantor) {
        this.in_guarantor = in_guarantor;
    }
    public In_person getIn_guarantor_partner() {
        return in_guarantor_partner;
    }

    public void setIn_guarantor_partner(In_person in_guarantor_partner) {
        this.in_guarantor_partner = in_guarantor_partner;
    }
    public List<In_good> getIn_goods() {
        return in_goods;
    }

    public void setIn_goods(List<In_good> in_goods) {
        this.in_goods = in_goods;
    }

    public In_third_person getIn_third_person() {
        return in_third_person;
    }

    public void setIn_third_person(In_third_person in_third_person) {
        this.in_third_person = in_third_person;
    }

    public List<In_document_store> getIn_document_stores() {
        return in_document_stores;
    }

    public void setIn_document_stores(List<In_document_store> in_document_stores) {
        this.in_document_stores = in_document_stores;
    }

    public List<In_asset> getIn_assets() {
        return in_assets;
    }

    public void setIn_assets(List<In_asset> in_assets) {
        this.in_assets = in_assets;
    }

    @Override
    public String toString() {
        return "In_instance@" + hashCode();
    }
}
