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

public class ClientEmployerInfoViewPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public ClientEmployerInfoViewPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public ClientEmployerInfoViewPage(In_instance inInstance) {
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
        tabs.add(new AbstractTabWithForm(new Model<String>(PartnerPersonalInfoViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new PartnerPersonalInfoViewPage(inInstance).createPanel(panelId);
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
                String maritasStatus = inInstance.getIn_person().getDictionary_marital_status();
                if (maritasStatus == null || (!"1".equals(maritasStatus) && !"5".equals(maritasStatus)) ) {
                    return false;
                }
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
        tabs.add(new AbstractTabWithForm(new Model<String>(ThirdPersonInfoViewPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ThirdPersonInfoViewPage(inInstance).createPanel(panelId);
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
        ajaxTabbedPanel.setSelectedTab(6 - 1);
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
        return "Трудовая деятельность клиента";
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
            
                DictionaryTextField ddc1 = new DictionaryTextField("in_person.dict_level_of_education_id",
                    dictionary.getDictionary("levelOfEducation", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_level_of_education"), ddc1));
                form.add(new LineBorder("in_person.dict_level_of_education_line", new EditBorder("in_person.dict_level_of_education_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);
                ddc1.setEnabled(false);

                DictionaryTextField ddc2 = new DictionaryTextField("in_person.dict_activity_sector_id",
                    dictionary.getDictionary("activitySector", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc2.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_activity_sector"), ddc2));
                form.add(new LineBorder("in_person.dict_activity_sector_line", new EditBorder("in_person.dict_activity_sector_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);
                ddc2.setEnabled(false);

                DictionaryTextField ddc3 = new DictionaryTextField("in_person.dict_type_of_activity_spd_id",
                    dictionary.getDictionary("typeOfActivitySectorSpd", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc3.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_type_of_activity_spd"), ddc3));
                form.add(new LineBorder("in_person.dict_type_of_activity_spd_line", new EditBorder("in_person.dict_type_of_activity_spd_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);
                ddc3.setEnabled(false);

                DictionaryTextField ddc4 = new DictionaryTextField("in_person.dict_profession_id",
                    dictionary.getDictionary("profession", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc4.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_profession"), ddc4));
                form.add(new LineBorder("in_person.dict_profession_line", new EditBorder("in_person.dict_profession_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                ddc4.setEnabled(false);

                DictionaryTextField ddc5 = new DictionaryTextField("in_person.dict_employment_type_id",
                    dictionary.getDictionary("employmentType", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc5.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_employment_type"), ddc5));
                form.add(new LineBorder("in_person.dict_employment_type_line", new EditBorder("in_person.dict_employment_type_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);
                ddc5.setEnabled(false);

                TextField ddc6 = new TextField("in_person.start_employment_date_id", new org.bitbucket.creditauto.wicket.DateModelWrapper("MMyyyy", model.bind("in_person.start_employment_date")));
                form.add(new LineBorder("in_person.start_employment_date_line", new EditBorder("in_person.start_employment_date_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                ddc6.setEnabled(false);

                TextField ddc7 = new TextField("in_person.end_data_of_employment_id", model.bind("in_person.end_data_of_employment"));
                form.add(new LineBorder("in_person.end_data_of_employment_line", new EditBorder("in_person.end_data_of_employment_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                ddc7.setEnabled(false);

                TextField ddc8 = new TextField("in_person.employer_id", model.bind("in_person.employer"));
                form.add(new LineBorder("in_person.employer_line", new EditBorder("in_person.employer_border", ddc8, true)));
                ddc8.setOutputMarkupId(true);
                ddc8.setEnabled(false);

                DictionaryTextField ddc9 = new DictionaryTextField("in_person.dict_empl_regprovince_id",
                    dictionary.getDictionary("provinceList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc9.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_empl_regprovince"), ddc9));
                form.add(new LineBorder("in_person.dict_empl_regprovince_line", new EditBorder("in_person.dict_empl_regprovince_border", ddc9, true)));
                ddc9.setOutputMarkupId(true);
                ddc9.setEnabled(false);

                DictionaryTextField ddc10 = new DictionaryTextField("in_person.dict_empl_regregion_id",
                    dictionary.getDictionary("regionList", inInstance.getIn_person().getDict_empl_regprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc10.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_empl_regregion"), ddc10));
                form.add(new LineBorder("in_person.dict_empl_regregion_line", new EditBorder("in_person.dict_empl_regregion_border", ddc10, true)));
                ddc10.setOutputMarkupId(true);
                ddc10.setEnabled(false);

                DictionaryTextField ddc11 = new DictionaryTextField("in_person.dict_empl_regcity_id",
                    dictionary.getDictionary("cityList", inInstance.getIn_person().getDict_empl_regregion(),
                        inInstance.getIn_person().getDict_empl_regprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc11.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_empl_regcity"), ddc11));
                form.add(new LineBorder("in_person.dict_empl_regcity_line", new EditBorder("in_person.dict_empl_regcity_border", ddc11, true)));
                ddc11.setOutputMarkupId(true);
                ddc11.setEnabled(false);

                TextField ddc12 = new TextField("in_person.empl_regpostindex_id", model.bind("in_person.empl_regpostindex"));
                form.add(new LineBorder("in_person.empl_regpostindex_line", new EditBorder("in_person.empl_regpostindex_border", ddc12, true)));
                ddc12.setOutputMarkupId(true);
                ddc12.setEnabled(false);

                TextField ddc13 = new TextField("in_person.edrpou_of_employer_id", model.bind("in_person.edrpou_of_employer"));
                form.add(new LineBorder("in_person.edrpou_of_employer_line", new EditBorder("in_person.edrpou_of_employer_border", ddc13, true)));
                ddc13.setOutputMarkupId(true);
                ddc13.setEnabled(false);

                TextField ddc14 = new TextField("in_person.empl_regstreet_id", model.bind("in_person.empl_regstreet"));
                form.add(new LineBorder("in_person.empl_regstreet_line", new EditBorder("in_person.empl_regstreet_border", ddc14, true)));
                ddc14.setOutputMarkupId(true);
                ddc14.setEnabled(false);

                TextField ddc15 = new TextField("in_person.empl_reghouse_id", model.bind("in_person.empl_reghouse"));
                form.add(new LineBorder("in_person.empl_reghouse_line", new EditBorder("in_person.empl_reghouse_border", ddc15, true)));
                ddc15.setOutputMarkupId(true);
                ddc15.setEnabled(false);

                TextField ddc16 = new TextField("in_person.empl_regflat_id", model.bind("in_person.empl_regflat"));
                form.add(new LineBorder("in_person.empl_regflat_line", new EditBorder("in_person.empl_regflat_border", ddc16, true)));
                ddc16.setOutputMarkupId(true);
                ddc16.setEnabled(false);

                TextField ddc17 = new TextField("in_person.empl_regadditionalinfo_id", model.bind("in_person.empl_regadditionalinfo"));
                form.add(new LineBorder("in_person.empl_regadditionalinfo_line", new EditBorder("in_person.empl_regadditionalinfo_border", ddc17, true)));
                ddc17.setOutputMarkupId(true);
                ddc17.setEnabled(false);

                TextField ddc18 = new TextField("in_person.phone_of_employer_id", model.bind("in_person.phone_of_employer"));
                form.add(new LineBorder("in_person.phone_of_employer_line", new EditBorder("in_person.phone_of_employer_border", ddc18, true)));
                ddc18.setOutputMarkupId(true);
                ddc18.setEnabled(false);

                TextField ddc19 = new TextField("in_person.second_phone_employer_id", model.bind("in_person.second_phone_employer"));
                form.add(new LineBorder("in_person.second_phone_employer_line", new EditBorder("in_person.second_phone_employer_border", ddc19, true)));
                ddc19.setOutputMarkupId(true);
                ddc19.setEnabled(false);

                DictionaryTextField ddc20 = new DictionaryTextField("in_person.dict_type_employer_by_size_id",
                    dictionary.getDictionary("typeEmployerBySize", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc20.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_type_employer_by_size"), ddc20));
                form.add(new LineBorder("in_person.dict_type_employer_by_size_line", new EditBorder("in_person.dict_type_employer_by_size_border", ddc20, true)));
                ddc20.setOutputMarkupId(true);
                ddc20.setEnabled(false);

                TextArea ddc21 = new TextArea("in_person.another_employers_id", model.bind("in_person.another_employers"));
                form.add(new LineBorder("in_person.another_employers_line", new EditBorder("in_person.another_employers_border", ddc21, true)));
                ddc21.setOutputMarkupId(true);
                ddc21.setEnabled(false);

                TextField ddc22 = new TextField("in_person.prev_employer_name_id", model.bind("in_person.prev_employer_name"));
                form.add(new LineBorder("in_person.prev_employer_name_line", new EditBorder("in_person.prev_employer_name_border", ddc22, true)));
                ddc22.setOutputMarkupId(true);
                ddc22.setEnabled(false);

                TextField ddc23 = new TextField("in_person.prev_employer_num_years_experience_id", model.bind("in_person.prev_employer_num_years_experience"));
                form.add(new LineBorder("in_person.prev_employer_num_years_experience_line", new EditBorder("in_person.prev_employer_num_years_experience_border", ddc23, true)));
                ddc23.setOutputMarkupId(true);
                ddc23.setEnabled(false);

                TextField ddc24 = new TextField("in_person.number_of_years_of_experience_id", model.bind("in_person.number_of_years_of_experience"));
                form.add(new LineBorder("in_person.number_of_years_of_experience_line", new EditBorder("in_person.number_of_years_of_experience_border", ddc24, true)));
                ddc24.setOutputMarkupId(true);
                ddc24.setEnabled(false);

                DropDownChoice ddc25 = new DropDownChoice("in_person.number_of_months_of_experience_id", new org.bitbucket.creditauto.wicket.IntegerModelWrapper(model.bind("in_person.number_of_months_of_experience")),
                java.util.Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"));
                form.add(new LineBorder("in_person.number_of_months_of_experience_line", new EditBorder("in_person.number_of_months_of_experience_border", ddc25, true)));
                ddc25.setOutputMarkupId(true);
                ddc25.setEnabled(false);

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            setResponsePage(new ClientAddressMailChooseViewPage(inInstance));
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            setResponsePage(new ClientIncomeInfoViewPage(inInstance));
                    }           
                                    });
    }
        
    }
    }    
}
