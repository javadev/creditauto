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

public class NotesViewPanel extends Panel {
    protected IDictionary dictionary;
    private final   In_dossier modelObject;
    
    
        private In_bloknot in_bloknot = new In_bloknot();
        public In_bloknot getIn_bloknot() {
                            return in_bloknot;
        }
         public void setIn_bloknot(In_bloknot in_bloknot) {
                            this.in_bloknot = in_bloknot;
        }
        public In_dossier getIn_dossier() {
                            return modelObject;
        }
         
        public NotesViewPanel(String id, IModel model) {
            super(id, model);
            this.modelObject = (In_dossier)this.getDefaultModelObject();
                        
                        init();
        }
    private void init() {
        dictionary = new DictionaryServerImpl();
    CompoundPropertyModel model = new CompoundPropertyModel(modelObject); 
            final  Panel form = this;
            form.setOutputMarkupId(true);
            
//repeaters
WebMarkupContainer listViewContainer = new WebMarkupContainer("listNotes_container");
    ListView listview = new ListView("listNotes", modelObject.getIn_bloknots()) {
        @Override 
        protected void populateItem(ListItem item) {
            final In_bloknot modelObject = (In_bloknot)item.getModelObject();
            CompoundPropertyModel model = new CompoundPropertyModel(modelObject);
            item.setModel(model);
                                                          item.add(new Label("enter_date", model.bind("enter_date")));
                                                                                     item.add(new Label("user_login", model.bind("user_login")));
                                                                                     item.add(new Label("bloknot_type", model.bind("bloknot_type")));
                                                                                     item.add(new Label("text", model.bind("text")));
                                               }
        @Override
        protected ListItem newItem(int index) {
            return new OddEvenListItem(index, getListItemModel(getModel(), index));
        }
    };
    listViewContainer.setOutputMarkupId(true);
    form.add(listViewContainer.add(listview));

//end of repeaters
    }
        
    
}
