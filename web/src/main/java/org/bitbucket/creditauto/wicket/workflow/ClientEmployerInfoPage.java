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

public class ClientEmployerInfoPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public ClientEmployerInfoPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public ClientEmployerInfoPage(In_instance inInstance) {
                this.inInstance = inInstance;
        init();
    }
    private void init() {
        dictionary = new DictionaryServerImpl();
        List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTabWithForm(new Model<String>(CarDetailsInfoPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new CarDetailsInfoPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(CredittypeSelectionPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new CredittypeSelectionPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientMaritalStatusPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientMaritalStatusPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(PartnerPersonalInfoPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new PartnerPersonalInfoPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientAddressMailChoosePage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientAddressMailChoosePage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientEmployerInfoPage.getTitle())) {
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
            
                DropDownChoice ddc1 = new DropDownChoice("in_person.dict_level_of_education_id",
                    dictionary.getDictionary("levelOfEducation", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_level_of_education"), ddc1));
                ddc1.setNullValid(true);
                form.add(new LineBorder("in_person.dict_level_of_education_line", new EditBorder("in_person.dict_level_of_education_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);

                DropDownChoice ddc2 = new DropDownChoice("in_person.dict_activity_sector_id",
                    dictionary.getDictionary("activitySector", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc2.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_activity_sector"), ddc2));
                ddc2.setNullValid(true);
                form.add(new LineBorder("in_person.dict_activity_sector_line", new EditBorder("in_person.dict_activity_sector_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);

                DropDownChoice ddc3 = new DropDownChoice("in_person.dict_type_of_activity_spd_id",
                    dictionary.getDictionary("typeOfActivitySectorSpd", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc3.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_type_of_activity_spd"), ddc3));
                ddc3.setNullValid(true);
                form.add(new LineBorder("in_person.dict_type_of_activity_spd_line", new EditBorder("in_person.dict_type_of_activity_spd_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);

                DropDownChoice ddc4 = new DropDownChoice("in_person.dict_profession_id",
                    dictionary.getDictionary("profession", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc4.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_profession"), ddc4));
                ddc4.setNullValid(true);
                form.add(new LineBorder("in_person.dict_profession_line", new EditBorder("in_person.dict_profession_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);

                DropDownChoice ddc5 = new DropDownChoice("in_person.dict_employment_type_id",
                    dictionary.getDictionary("employmentType", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc5.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_employment_type"), ddc5));
                ddc5.setNullValid(true);
                form.add(new LineBorder("in_person.dict_employment_type_line", new EditBorder("in_person.dict_employment_type_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);

                TextField ddc6 = new TextField("in_person.start_employment_date_id", new org.bitbucket.creditauto.wicket.DateModelWrapper("MMyyyy", model.bind("in_person.start_employment_date")));
                form.add(new LineBorder("in_person.start_employment_date_line", new EditBorder("in_person.start_employment_date_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);

                TextField ddc7 = new TextField("in_person.end_data_of_employment_id", model.bind("in_person.end_data_of_employment"));
                form.add(new LineBorder("in_person.end_data_of_employment_line", new EditBorder("in_person.end_data_of_employment_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);

                TextField ddc8 = new TextField("in_person.employer_id", model.bind("in_person.employer"));
                form.add(new LineBorder("in_person.employer_line", new EditBorder("in_person.employer_border", ddc8, true)));
                ddc8.setOutputMarkupId(true);

                DropDownChoice ddc9 = new DropDownChoice("in_person.dict_empl_regprovince_id",
                    dictionary.getDictionary("provinceList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc9.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_empl_regprovince"), ddc9));
                ddc9.setNullValid(true);
                EditBorder.OnChange change9 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                DropDownChoice regprovince = (DropDownChoice) form.get("in_person.dict_empl_regprovince_line:in_person.dict_empl_regprovince_border:in_person.dict_empl_regprovince_id");
                DropDownChoice regregion = (DropDownChoice) form.get("in_person.dict_empl_regregion_line:in_person.dict_empl_regregion_border:in_person.dict_empl_regregion_id");
                inInstance.getIn_person().setDict_empl_regregion(null);
                regregion.setChoices(dictionary.getDictionary("regionList", ((Dictionary_data) regprovince.getConvertedInput()).getDkey(), IDictionary.LANGUAGE_RU, new Date()));
                target.addComponent(regregion);
            
                    }
                };
                form.add(new LineBorder("in_person.dict_empl_regprovince_line", new EditBorder("in_person.dict_empl_regprovince_border", ddc9, true, change9)));
                ddc9.setOutputMarkupId(true);

                DropDownChoice ddc10 = new DropDownChoice("in_person.dict_empl_regregion_id",
                    dictionary.getDictionary("regionList", inInstance.getIn_person().getDict_empl_regprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc10.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_empl_regregion"), ddc10));
                ddc10.setNullValid(true);
                EditBorder.OnChange change10 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                DropDownChoice regprovince = (DropDownChoice) form.get("in_person.dict_empl_regprovince_line:in_person.dict_empl_regprovince_border:in_person.dict_empl_regprovince_id");
                DropDownChoice regregion = (DropDownChoice) form.get("in_person.dict_empl_regregion_line:in_person.dict_empl_regregion_border:in_person.dict_empl_regregion_id");
                DropDownChoice regcity = (DropDownChoice) form.get("in_person.dict_empl_regcity_line:in_person.dict_empl_regcity_border:in_person.dict_empl_regcity_id");
                inInstance.getIn_person().setDict_empl_regcity(null);
                regcity.setChoices(dictionary.getDictionary("cityList", ((Dictionary_data) regregion.getConvertedInput()).getDkey(),
                    ((Dictionary_data) regprovince.getModelObject()).getDkey(), IDictionary.LANGUAGE_RU, new Date()));
                target.addComponent(regcity);
            
                    }
                };
                form.add(new LineBorder("in_person.dict_empl_regregion_line", new EditBorder("in_person.dict_empl_regregion_border", ddc10, true, change10)));
                ddc10.setOutputMarkupId(true);

                DropDownChoice ddc11 = new DropDownChoice("in_person.dict_empl_regcity_id",
                    dictionary.getDictionary("cityList", inInstance.getIn_person().getDict_empl_regregion(),
                        inInstance.getIn_person().getDict_empl_regprovince(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc11.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_empl_regcity"), ddc11));
                ddc11.setNullValid(true);
                EditBorder.OnChange change11 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                DropDownChoice regcity = (DropDownChoice) form.get("in_person.dict_empl_regcity_line:in_person.dict_empl_regcity_border:in_person.dict_empl_regcity_id");
                TextField regpostindex = (TextField) form.get("in_person.empl_regpostindex_line:in_person.empl_regpostindex_border:in_person.empl_regpostindex_id");
                inInstance.getIn_person().setEmpl_regpostindex(((Dictionary_data) regcity.getConvertedInput()).getExpkey3());
                target.addComponent(regpostindex);
            
                    }
                };
                form.add(new LineBorder("in_person.dict_empl_regcity_line", new EditBorder("in_person.dict_empl_regcity_border", ddc11, true, change11)));
                ddc11.setOutputMarkupId(true);

                TextField ddc12 = new TextField("in_person.empl_regpostindex_id", model.bind("in_person.empl_regpostindex"));
                form.add(new LineBorder("in_person.empl_regpostindex_line", new EditBorder("in_person.empl_regpostindex_border", ddc12, true)));
                ddc12.setOutputMarkupId(true);
                ddc12.setEnabled(false);

                TextField ddc13 = new TextField("in_person.edrpou_of_employer_id", model.bind("in_person.edrpou_of_employer"));
                form.add(new LineBorder("in_person.edrpou_of_employer_line", new EditBorder("in_person.edrpou_of_employer_border", ddc13, true)));
                ddc13.setOutputMarkupId(true);

                TextField ddc14 = new TextField("in_person.empl_regstreet_id", model.bind("in_person.empl_regstreet"));
                form.add(new LineBorder("in_person.empl_regstreet_line", new EditBorder("in_person.empl_regstreet_border", ddc14, true)));
                ddc14.setOutputMarkupId(true);

                TextField ddc15 = new TextField("in_person.empl_reghouse_id", model.bind("in_person.empl_reghouse"));
                form.add(new LineBorder("in_person.empl_reghouse_line", new EditBorder("in_person.empl_reghouse_border", ddc15, true)));
                ddc15.setOutputMarkupId(true);

                TextField ddc16 = new TextField("in_person.empl_regflat_id", model.bind("in_person.empl_regflat"));
                form.add(new LineBorder("in_person.empl_regflat_line", new EditBorder("in_person.empl_regflat_border", ddc16, true)));
                ddc16.setOutputMarkupId(true);

                TextField ddc17 = new TextField("in_person.empl_regadditionalinfo_id", model.bind("in_person.empl_regadditionalinfo"));
                form.add(new LineBorder("in_person.empl_regadditionalinfo_line", new EditBorder("in_person.empl_regadditionalinfo_border", ddc17, true)));
                ddc17.setOutputMarkupId(true);

                TextField ddc18 = new TextField("in_person.phone_of_employer_id", model.bind("in_person.phone_of_employer"));
                form.add(new LineBorder("in_person.phone_of_employer_line", new EditBorder("in_person.phone_of_employer_border", ddc18, true)));
                ddc18.setOutputMarkupId(true);

                TextField ddc19 = new TextField("in_person.second_phone_employer_id", model.bind("in_person.second_phone_employer"));
                form.add(new LineBorder("in_person.second_phone_employer_line", new EditBorder("in_person.second_phone_employer_border", ddc19, true)));
                ddc19.setOutputMarkupId(true);

                DropDownChoice ddc20 = new DropDownChoice("in_person.dict_type_employer_by_size_id",
                    dictionary.getDictionary("typeEmployerBySize", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc20.setModel(new DictionaryModelWrapper(model.bind("in_person.dict_type_employer_by_size"), ddc20));
                ddc20.setNullValid(true);
                form.add(new LineBorder("in_person.dict_type_employer_by_size_line", new EditBorder("in_person.dict_type_employer_by_size_border", ddc20, true)));
                ddc20.setOutputMarkupId(true);

                TextArea ddc21 = new TextArea("in_person.another_employers_id", model.bind("in_person.another_employers"));
                form.add(new LineBorder("in_person.another_employers_line", new EditBorder("in_person.another_employers_border", ddc21, true)));
                ddc21.setOutputMarkupId(true);

                TextField ddc22 = new TextField("in_person.prev_employer_name_id", model.bind("in_person.prev_employer_name"));
                form.add(new LineBorder("in_person.prev_employer_name_line", new EditBorder("in_person.prev_employer_name_border", ddc22, true)));
                ddc22.setOutputMarkupId(true);

                TextField ddc23 = new TextField("in_person.prev_employer_num_years_experience_id", model.bind("in_person.prev_employer_num_years_experience"));
                form.add(new LineBorder("in_person.prev_employer_num_years_experience_line", new EditBorder("in_person.prev_employer_num_years_experience_border", ddc23, true)));
                ddc23.setOutputMarkupId(true);

                TextField ddc24 = new TextField("in_person.number_of_years_of_experience_id", model.bind("in_person.number_of_years_of_experience"));
                form.add(new LineBorder("in_person.number_of_years_of_experience_line", new EditBorder("in_person.number_of_years_of_experience_border", ddc24, true)));
                ddc24.setOutputMarkupId(true);

                DropDownChoice ddc25 = new DropDownChoice("in_person.number_of_months_of_experience_id", new org.bitbucket.creditauto.wicket.IntegerModelWrapper(model.bind("in_person.number_of_months_of_experience")),
                java.util.Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"));
                form.add(new LineBorder("in_person.number_of_months_of_experience_line", new EditBorder("in_person.number_of_months_of_experience_border", ddc25, true)));
                ddc25.setOutputMarkupId(true);

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            setResponsePage(new ClientAddressMailChoosePage());
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            String maritasStatus = inInstance.getIn_person().getDictionary_marital_status();
            if (maritasStatus != null && ("1".equals(maritasStatus) || "5".equals(maritasStatus)) ) {
            saveWorkflowPath("PartnerEmployerInfoPage");
            org.bitbucket.creditauto.wicket.InstanceHelper.save(inInstance);
            setResponsePage(new PartnerEmployerInfoPage());
            } else {
            saveWorkflowPath("ClientIncomeInfoPage");
            org.bitbucket.creditauto.wicket.InstanceHelper.save(inInstance);
            setResponsePage(new ClientIncomeInfoPage());
            };
                    }           
                                    });
    }
        
    }
    }    
}
