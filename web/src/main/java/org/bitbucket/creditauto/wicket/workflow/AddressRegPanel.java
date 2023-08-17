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

public class AddressRegPanel extends Panel {
    protected IDictionary dictionary;
    private final   In_person modelObject;
    
    
        public AddressRegPanel(String id, IModel model) {
            super(id, model);
            this.modelObject = (In_person)this.getDefaultModelObject();
                        init();
        }
    private void init() {
        dictionary = new DictionaryServerImpl();
    CompoundPropertyModel model = new CompoundPropertyModel(modelObject); 
            final  Panel form = this;
            form.setOutputMarkupId(true);
            
                DropDownChoice ddc1 = new DropDownChoice("dictionary_regprovince_id",
                    dictionary.getDictionary("provinceList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("dictionary_regprovince"), ddc1));
                ddc1.setNullValid(true);
                EditBorder.OnChange change1 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                DropDownChoice regprovince = (DropDownChoice) form.get("dictionary_regprovince_line:dictionary_regprovince_border:dictionary_regprovince_id");
                DropDownChoice regregion = (DropDownChoice) form.get("dictionary_regregion_line:dictionary_regregion_border:dictionary_regregion_id");
                modelObject.setDictionary_regregion(null);
                regregion.setChoices(dictionary.getDictionary("regionList", ((Dictionary_data) regprovince.getConvertedInput()).getDkey(), IDictionary.LANGUAGE_RU, new Date()));
                target.addComponent(regregion);
            
                    }
                };
                form.add(new LineBorder("dictionary_regprovince_line", new EditBorder("dictionary_regprovince_border", ddc1, true, change1)));
                ddc1.setOutputMarkupId(true);

                DropDownChoice ddc2 = new DropDownChoice("dictionary_regregion_id",
                    dictionary.getDictionary("regionList", modelObject.getDictionary_regprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc2.setModel(new DictionaryModelWrapper(model.bind("dictionary_regregion"), ddc2));
                ddc2.setNullValid(true);
                EditBorder.OnChange change2 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                DropDownChoice regprovince = (DropDownChoice) form.get("dictionary_regprovince_line:dictionary_regprovince_border:dictionary_regprovince_id");
                DropDownChoice regregion = (DropDownChoice) form.get("dictionary_regregion_line:dictionary_regregion_border:dictionary_regregion_id");
                DropDownChoice regcity = (DropDownChoice) form.get("dictionary_regcity_line:dictionary_regcity_border:dictionary_regcity_id");
                modelObject.setDictionary_regcity(null);
                regcity.setChoices(dictionary.getDictionary("cityList", ((Dictionary_data) regregion.getConvertedInput()).getDkey(),
                    ((Dictionary_data) regprovince.getModelObject()).getDkey(), IDictionary.LANGUAGE_RU, new Date()));
                target.addComponent(regcity);
            
                    }
                };
                form.add(new LineBorder("dictionary_regregion_line", new EditBorder("dictionary_regregion_border", ddc2, true, change2)));
                ddc2.setOutputMarkupId(true);

                DropDownChoice ddc3 = new DropDownChoice("dictionary_regcity_id",
                    dictionary.getDictionary("cityList", modelObject.getDictionary_regregion(),
                        modelObject.getDictionary_regprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc3.setModel(new DictionaryModelWrapper(model.bind("dictionary_regcity"), ddc3));
                ddc3.setNullValid(true);
                EditBorder.OnChange change3 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                DropDownChoice regcity = (DropDownChoice) form.get("dictionary_regcity_line:dictionary_regcity_border:dictionary_regcity_id");
                TextField regpostindex = (TextField) form.get("regpostindex_line:regpostindex_border:regpostindex_id");
                modelObject.setRegpostindex(((Dictionary_data) regcity.getConvertedInput()).getExpkey3());
                target.addComponent(regpostindex);
            
                    }
                };
                form.add(new LineBorder("dictionary_regcity_line", new EditBorder("dictionary_regcity_border", ddc3, true, change3)));
                ddc3.setOutputMarkupId(true);

                TextField ddc4 = new TextField("regpostindex_id", model.bind("regpostindex"));
                form.add(new LineBorder("regpostindex_line", new EditBorder("regpostindex_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                ddc4.setEnabled(false);

                TextField ddc5 = new TextField("regstreet_id", model.bind("regstreet"));
                EditBorder.OnChange change5 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
 
                    }
                };
                form.add(new LineBorder("regstreet_line", new EditBorder("regstreet_border", ddc5, true, change5)));
                ddc5.setOutputMarkupId(true);

                TextField ddc6 = new TextField("reghouse_id", model.bind("reghouse"));
                EditBorder.OnChange change6 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
 
                    }
                };
                form.add(new LineBorder("reghouse_line", new EditBorder("reghouse_border", ddc6, true, change6)));
                ddc6.setOutputMarkupId(true);

                TextField ddc7 = new TextField("regflat_id", model.bind("regflat"));
                EditBorder.OnChange change7 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
 
                    }
                };
                form.add(new LineBorder("regflat_line", new EditBorder("regflat_border", ddc7, true, change7)));
                ddc7.setOutputMarkupId(true);

                TextField ddc8 = new TextField("regadditionalinfo_id", model.bind("regadditionalinfo"));
                EditBorder.OnChange change8 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
 
                    }
                };
                form.add(new LineBorder("regadditionalinfo_line", new EditBorder("regadditionalinfo_border", ddc8, true, change8)));
                ddc8.setOutputMarkupId(true);

                CheckBox ddc9 = new CheckBox("is_regstatus_temporary_id", model.bind("is_regstatus_temporary"));
                form.add(new LineBorder("is_regstatus_temporary_line", new EditBorder("is_regstatus_temporary_border", ddc9, true)));
                ddc9.setOutputMarkupId(true);

    AjaxLink ddc10 = new AjaxLink("copy_to_mail_address_submit_id") {
            @Override
             public void onClick(AjaxRequestTarget target)  {
                
                String id = form.getId().replace("Reg", "Mail");
                org.bitbucket.creditauto.wicket.InstanceHelper.copyRegToMail(modelObject);
                DropDownChoice mailprovince = (DropDownChoice) form.getParent().get(id+":dictionary_mailprovince_line:dictionary_mailprovince_border:dictionary_mailprovince_id");
                DropDownChoice mailregion = (DropDownChoice) form.getParent().get(id+":dictionary_mailregion_line:dictionary_mailregion_border:dictionary_mailregion_id");
                DropDownChoice mailcity = (DropDownChoice) form.getParent().get(id+":dictionary_mailcity_line:dictionary_mailcity_border:dictionary_mailcity_id");
                DropDownChoice regprovince = (DropDownChoice) form.get("dictionary_regprovince_line:dictionary_regprovince_border:dictionary_regprovince_id");
                DropDownChoice regregion = (DropDownChoice) form.get("dictionary_regregion_line:dictionary_regregion_border:dictionary_regregion_id");
                DropDownChoice regcity = (DropDownChoice) form.get("dictionary_regcity_line:dictionary_regcity_border:dictionary_regcity_id");
                mailprovince.setChoices(regprovince.getChoices());
                mailregion.setChoices(regregion.getChoices());
                mailcity.setChoices(regcity.getChoices());
                target.addComponent(form.getParent().get(id+":dictionary_mailcity_line:dictionary_mailcity_border:dictionary_mailcity_id"));
                target.addComponent(form.getParent().get(id+":dictionary_mailprovince_line:dictionary_mailprovince_border:dictionary_mailprovince_id"));
                target.addComponent(form.getParent().get(id+":dictionary_mailregion_line:dictionary_mailregion_border:dictionary_mailregion_id"));
                target.addComponent(form.getParent().get(id+":mailpostindex_line:mailpostindex_border:mailpostindex_id"));
                target.addComponent(form.getParent().get(id+":mailstreet_line:mailstreet_border:mailstreet_id"));
                target.addComponent(form.getParent().get(id+":mailhouse_line:mailhouse_border:mailhouse_id"));
                target.addComponent(form.getParent().get(id+":mailflat_line:mailflat_border:mailflat_id"));
                target.addComponent(form.getParent().get(id+"::mailadditionalinfo_line:mailadditionalinfo_border:mailadditionalinfo_id"));
        
            }
    };
             form.add(new LineBorder("copy_to_mail_address_submit_line", ddc10));
        
    }
        
    
}
