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

public class AddressMailViewPanel extends Panel {
    protected IDictionary dictionary;
    private final   In_person modelObject;
    
    
        public AddressMailViewPanel(String id, IModel model) {
            super(id, model);
            this.modelObject = (In_person)this.getDefaultModelObject();
                        init();
        }
    private void init() {
        dictionary = new DictionaryServerImpl();
    CompoundPropertyModel model = new CompoundPropertyModel(modelObject); 
            final  Panel form = this;
            form.setOutputMarkupId(true);
            
                DictionaryTextField ddc1 = new DictionaryTextField("dictionary_mailprovince_id",
                    dictionary.getDictionary("provinceList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("dictionary_mailprovince"), ddc1));
                EditBorder.OnChange change1 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

            
                    }
                };
                form.add(new LineBorder("dictionary_mailprovince_line", new EditBorder("dictionary_mailprovince_border", ddc1, true, change1)));
                ddc1.setOutputMarkupId(true);
                ddc1.setEnabled(false);

                DictionaryTextField ddc2 = new DictionaryTextField("dictionary_mailregion_id",
                    dictionary.getDictionary("regionList", modelObject.getDictionary_mailprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc2.setModel(new DictionaryModelWrapper(model.bind("dictionary_mailregion"), ddc2));
                EditBorder.OnChange change2 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

            
                    }
                };
                form.add(new LineBorder("dictionary_mailregion_line", new EditBorder("dictionary_mailregion_border", ddc2, true, change2)));
                ddc2.setOutputMarkupId(true);
                ddc2.setEnabled(false);

                DictionaryTextField ddc3 = new DictionaryTextField("dictionary_mailcity_id",
                    dictionary.getDictionary("cityList", modelObject.getDictionary_mailregion(),
                        modelObject.getDictionary_mailprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc3.setModel(new DictionaryModelWrapper(model.bind("dictionary_mailcity"), ddc3));
                EditBorder.OnChange change3 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

            
                    }
                };
                form.add(new LineBorder("dictionary_mailcity_line", new EditBorder("dictionary_mailcity_border", ddc3, true, change3)));
                ddc3.setOutputMarkupId(true);
                ddc3.setEnabled(false);

                TextField ddc4 = new TextField("mailpostindex_id", model.bind("mailpostindex"));
                form.add(new LineBorder("mailpostindex_line", new EditBorder("mailpostindex_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                ddc4.setEnabled(false);

                TextField ddc5 = new TextField("mailstreet_id", model.bind("mailstreet"));
                EditBorder.OnChange change5 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
 
                    }
                };
                form.add(new LineBorder("mailstreet_line", new EditBorder("mailstreet_border", ddc5, true, change5)));
                ddc5.setOutputMarkupId(true);
                ddc5.setEnabled(false);

                TextField ddc6 = new TextField("mailhouse_id", model.bind("mailhouse"));
                EditBorder.OnChange change6 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
 
                    }
                };
                form.add(new LineBorder("mailhouse_line", new EditBorder("mailhouse_border", ddc6, true, change6)));
                ddc6.setOutputMarkupId(true);
                ddc6.setEnabled(false);

                TextField ddc7 = new TextField("mailflat_id", model.bind("mailflat"));
                EditBorder.OnChange change7 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
 
                    }
                };
                form.add(new LineBorder("mailflat_line", new EditBorder("mailflat_border", ddc7, true, change7)));
                ddc7.setOutputMarkupId(true);
                ddc7.setEnabled(false);

                TextField ddc8 = new TextField("mailadditionalinfo_id", model.bind("mailadditionalinfo"));
                EditBorder.OnChange change8 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
 
                    }
                };
                form.add(new LineBorder("mailadditionalinfo_line", new EditBorder("mailadditionalinfo_border", ddc8, true, change8)));
                ddc8.setOutputMarkupId(true);
                ddc8.setEnabled(false);

    }
        
    
}
