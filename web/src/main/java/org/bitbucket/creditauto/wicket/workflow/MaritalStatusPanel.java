/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket.workflow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.authorization.server.AuthorizationServerImpl;
import org.bitbucket.creditauto.dictionary.facade.IDictionary;
import org.bitbucket.creditauto.dictionary.server.DictionaryServerImpl;
import org.bitbucket.creditauto.dossierserver.server.DossierServerServerImpl;
import org.bitbucket.creditauto.entity.Dictionary_data;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.In_bloknot;
import org.bitbucket.creditauto.entity.In_dossier;
import org.bitbucket.creditauto.entity.In_asset;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.entity.In_person;
import org.bitbucket.creditauto.entity.Powerofattorney;
import org.bitbucket.creditauto.wicket.SearchData;
import org.bitbucket.creditauto.entity.Urole;
import org.bitbucket.creditauto.entity.User;
import org.bitbucket.creditauto.tarification.facade.ITarification;
import org.bitbucket.creditauto.tarification.server.TarificationServerImpl;
import org.bitbucket.creditauto.wicket.AbstractTabWithForm;
import org.bitbucket.creditauto.wicket.CreditautoSession;
import org.bitbucket.creditauto.wicket.DictionaryModelWrapper;
import org.bitbucket.creditauto.wicket.DictionaryTextField;
import org.bitbucket.creditauto.wicket.EditBorder;
import org.bitbucket.creditauto.wicket.FileUploadModelWrapper;
import org.bitbucket.creditauto.wicket.FormTabbedPanel;
import org.bitbucket.creditauto.wicket.InDossierStatus;
import org.bitbucket.creditauto.wicket.LineBorder;
import org.bitbucket.creditauto.wicket.ModalTextOnlyPanel;
import org.bitbucket.creditauto.wicket.StringUtil;
import org.bitbucket.creditauto.wicket.TemplatePage;

public class MaritalStatusPanel extends Panel {
    protected IDictionary dictionary;
    private final   In_person modelObject;
    
    
        public MaritalStatusPanel(String id, IModel model) {
            super(id, model);
            this.modelObject = (In_person)this.getDefaultModelObject();
                        init();
        }
    private void init() {
        dictionary = new DictionaryServerImpl();
    CompoundPropertyModel model = new CompoundPropertyModel(modelObject); 
            final  Panel form = this;
            form.setOutputMarkupId(true);
            
                DropDownChoice ddc1 = new DropDownChoice("dictionary_marital_status_id",
                    dictionary.getDictionary("maritalStatus", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("dictionary_marital_status"), ddc1));
                ddc1.setNullValid(true);
                form.add(new LineBorder("dictionary_marital_status_line", new EditBorder("dictionary_marital_status_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);

                TextField ddc2 = new TextField("number_of_children_id", model.bind("number_of_children"));
                form.add(new LineBorder("number_of_children_line", new EditBorder("number_of_children_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);

                DropDownChoice ddc3 = new DropDownChoice("dictionary_habitation_situation_id",
                    dictionary.getDictionary("habitationSituation", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc3.setModel(new DictionaryModelWrapper(model.bind("dictionary_habitation_situation"), ddc3));
                ddc3.setNullValid(true);
                form.add(new LineBorder("dictionary_habitation_situation_line", new EditBorder("dictionary_habitation_situation_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);

                TextField ddc4 = new TextField("habit_situation_start_date_id", new org.bitbucket.creditauto.wicket.DateModelWrapper("yyyy", model.bind("habit_situation_start_date")));
                form.add(new LineBorder("habit_situation_start_date_line", new EditBorder("habit_situation_start_date_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);

    }
        
    
}
