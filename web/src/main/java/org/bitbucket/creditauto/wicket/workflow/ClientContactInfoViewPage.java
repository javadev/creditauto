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

public class ClientContactInfoViewPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public ClientContactInfoViewPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public ClientContactInfoViewPage(In_instance inInstance) {
                this.inInstance = inInstance;
        init();
    }
    private void init() {
        dictionary = new DictionaryServerImpl();
        List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTabWithForm(new Model<String>(CarDetailsInfoViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new CarDetailsInfoViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(CredittypeSelectionViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new CredittypeSelectionViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientMaritalStatusViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientMaritalStatusViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientAddressMailChooseViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientAddressMailChooseViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientEmployerInfoViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientEmployerInfoViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientIncomeInfoViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientIncomeInfoViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientAssetViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientAssetViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(GuaranteePartnerInfoViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new GuaranteePartnerInfoViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                String gender = inInstance.getIn_guarantor().getDictionary_gender();
                if (gender == null) {
                    return false;
                }
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientContactInfoViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(PrintApplicationFormViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new PrintApplicationFormViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientDocumentStoreViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientDocumentStoreViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        AjaxTabbedPanel ajaxTabbedPanel = new FormTabbedPanel("panel", tabs);
        ajaxTabbedPanel.setSelectedTab(9 - 1);
        add(ajaxTabbedPanel);
        add(new Label("header", StringUtil.join(": ", inInstance.getIn_dossier().getId(),
                StringUtil.join(" ", inInstance.getIn_person().getLast_name_ru(), inInstance.getIn_person().getFirst_name_ru(),
                    inInstance.getIn_person().getPatronymic_name_ru()))));
    }

    public Panel createPanel(String panelId) {
        MainPanel panel = new MainPanel(panelId);
        panel.add(panel.new MainForm("form"));
        return panel;
    }

    public static String getTitle() {
        return "Контактная информация клиента";
    }

    protected class MainPanel extends Panel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends Form {
        
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel( inInstance);
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                TextField ddc1 = new TextField("in_person.phone_id", model.bind("in_person.phone"));
                form.add(new LineBorder("in_person.phone_line", new EditBorder("in_person.phone_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);
                ddc1.setEnabled(false);

                TextField ddc2 = new TextField("in_person.mobile_phone_id", model.bind("in_person.mobile_phone"));
                form.add(new LineBorder("in_person.mobile_phone_line", new EditBorder("in_person.mobile_phone_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);
                ddc2.setEnabled(false);

                TextField ddc3 = new TextField("in_person.email_id", model.bind("in_person.email"));
                form.add(new LineBorder("in_person.email_line", new EditBorder("in_person.email_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);
                ddc3.setEnabled(false);

                TextField ddc4 = new TextField("in_person.last_name_latin_id", model.bind("in_person.last_name_latin"));
                form.add(new LineBorder("in_person.last_name_latin_line", new EditBorder("in_person.last_name_latin_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                ddc4.setEnabled(false);

                TextField ddc5 = new TextField("in_person.first_name_latin_id", model.bind("in_person.first_name_latin"));
                form.add(new LineBorder("in_person.first_name_latin_line", new EditBorder("in_person.first_name_latin_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);
                ddc5.setEnabled(false);

                DictionaryTextField ddc6 = new DictionaryTextField("in_goods.0.car_driving_license_ownership_id",
                    dictionary.getDictionary("drivingLicense", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc6.setModel(new DictionaryModelWrapper(model.bind("in_goods.0.car_driving_license_ownership"), ddc6));
                form.add(new LineBorder("in_goods.0.car_driving_license_ownership_line", new EditBorder("in_goods.0.car_driving_license_ownership_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                ddc6.setEnabled(false);

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            setResponsePage(inInstance.getIn_guarantor() != null && inInstance.getIn_guarantor().getTin() != null && !inInstance.getIn_guarantor().getTin().isEmpty() ? new GuaranteePartnerInfoViewPage(inInstance) : new ClientAssetViewPage(inInstance));
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            setResponsePage(new ThirdPersonInfoViewPage(inInstance));
                    }           
                                    });
    }
        
    }
    }    
}
