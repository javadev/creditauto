/*
 * $Id$
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

public class EmployerAddressViewPanel extends Panel {
    protected IDictionary dictionary;
    private final   In_person modelObject;
    
    
        public EmployerAddressViewPanel(String id, IModel model) {
            super(id, model);
            this.modelObject = (In_person)this.getDefaultModelObject();
                        init();
        }
    private void init() {
        dictionary = new DictionaryServerImpl();
    CompoundPropertyModel model = new CompoundPropertyModel(modelObject); 
            final  Panel form = this;
            form.setOutputMarkupId(true);
            
                DictionaryTextField ddc1 = new DictionaryTextField("dict_empl_regprovince_id",
                    dictionary.getDictionary("provinceList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("dict_empl_regprovince"), ddc1));
                EditBorder.OnChange change1 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

            
                    }
                };
                form.add(new LineBorder("dict_empl_regprovince_line", new EditBorder("dict_empl_regprovince_border", ddc1, true, change1)));
                ddc1.setOutputMarkupId(true);
                ddc1.setEnabled(false);

                DictionaryTextField ddc2 = new DictionaryTextField("dict_empl_regregion_id",
                    dictionary.getDictionary("regionList", modelObject.getDict_empl_regprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc2.setModel(new DictionaryModelWrapper(model.bind("dict_empl_regregion"), ddc2));
                EditBorder.OnChange change2 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

            
                    }
                };
                form.add(new LineBorder("dict_empl_regregion_line", new EditBorder("dict_empl_regregion_border", ddc2, true, change2)));
                ddc2.setOutputMarkupId(true);
                ddc2.setEnabled(false);

                DictionaryTextField ddc3 = new DictionaryTextField("dict_empl_regcity_id",
                    dictionary.getDictionary("cityList", modelObject.getDict_empl_regregion(),
                        modelObject.getDict_empl_regprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc3.setModel(new DictionaryModelWrapper(model.bind("dict_empl_regcity"), ddc3));
                EditBorder.OnChange change3 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

            
                    }
                };
                form.add(new LineBorder("dict_empl_regcity_line", new EditBorder("dict_empl_regcity_border", ddc3, true, change3)));
                ddc3.setOutputMarkupId(true);
                ddc3.setEnabled(false);

                TextField ddc4 = new TextField("empl_regpostindex_id", model.bind("empl_regpostindex"));
                form.add(new LineBorder("empl_regpostindex_line", new EditBorder("empl_regpostindex_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                ddc4.setEnabled(false);

                TextField ddc5 = new TextField("empl_regstreet_id", model.bind("empl_regstreet"));
                form.add(new LineBorder("empl_regstreet_line", new EditBorder("empl_regstreet_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);
                ddc5.setEnabled(false);

                TextField ddc6 = new TextField("empl_reghouse_id", model.bind("empl_reghouse"));
                form.add(new LineBorder("empl_reghouse_line", new EditBorder("empl_reghouse_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                ddc6.setEnabled(false);

                TextField ddc7 = new TextField("empl_regflat_id", model.bind("empl_regflat"));
                form.add(new LineBorder("empl_regflat_line", new EditBorder("empl_regflat_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                ddc7.setEnabled(false);

                TextField ddc8 = new TextField("empl_regadditionalinfo_id", model.bind("empl_regadditionalinfo"));
                form.add(new LineBorder("empl_regadditionalinfo_line", new EditBorder("empl_regadditionalinfo_border", ddc8, true)));
                ddc8.setOutputMarkupId(true);
                ddc8.setEnabled(false);

                TextField ddc9 = new TextField("phone_of_employer_id", model.bind("phone_of_employer"));
                form.add(new LineBorder("phone_of_employer_line", new EditBorder("phone_of_employer_border", ddc9, true)));
                ddc9.setOutputMarkupId(true);
                ddc9.setEnabled(false);

                TextField ddc10 = new TextField("second_phone_employer_id", model.bind("second_phone_employer"));
                form.add(new LineBorder("second_phone_employer_line", new EditBorder("second_phone_employer_border", ddc10, true)));
                ddc10.setOutputMarkupId(true);
                ddc10.setEnabled(false);

    }
        
    
}
