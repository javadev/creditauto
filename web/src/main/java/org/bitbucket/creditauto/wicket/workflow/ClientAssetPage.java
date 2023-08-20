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

public class ClientAssetPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public ClientAssetPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public ClientAssetPage(In_instance inInstance) {
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
                Panel panel = new ClientEmployerInfoPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(PartnerEmployerInfoPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new PartnerEmployerInfoPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientIncomeInfoPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ClientIncomeInfoPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientAssetPage.getTitle())) {
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
        return "Собственность клиента";
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
            
                DropDownChoice ddc1 = new DropDownChoice("in_assets.0.dictionary_asset_id",
                    dictionary.getDictionary("assetList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("in_assets.0.dictionary_asset"), ddc1));
                ddc1.setNullValid(true);
                form.add(new LineBorder("in_assets.0.dictionary_asset_line", new EditBorder("in_assets.0.dictionary_asset_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);
                form.get("in_assets.0.dictionary_asset_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.0.dictionary_asset_line").setVisible(inInstance.getIn_assets().get(0).getVisible() == null ? false : inInstance.getIn_assets().get(0).getVisible());

                TextField ddc2 = new TextField("in_assets.0.count_of_id", model.bind("in_assets.0.count_of"));
                form.add(new LineBorder("in_assets.0.count_of_line", new EditBorder("in_assets.0.count_of_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);
                form.get("in_assets.0.count_of_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.0.count_of_line").setVisible(inInstance.getIn_assets().get(0).getVisible() == null ? false : inInstance.getIn_assets().get(0).getVisible());

                AjaxCheckBox ddc3 = new AjaxCheckBox("in_assets.0.selected_id", model.bind("in_assets.0.selected")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                             
                        }                    
                                                        };
                form.add(new LineBorder("in_assets.0.selected_line", new EditBorder("in_assets.0.selected_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);
                form.get("in_assets.0.selected_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.0.selected_line").setVisible(inInstance.getIn_assets().get(0).getVisible() == null ? false : inInstance.getIn_assets().get(0).getVisible());

                DropDownChoice ddc4 = new DropDownChoice("in_assets.1.dictionary_asset_id",
                    dictionary.getDictionary("assetList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc4.setModel(new DictionaryModelWrapper(model.bind("in_assets.1.dictionary_asset"), ddc4));
                ddc4.setNullValid(true);
                form.add(new LineBorder("in_assets.1.dictionary_asset_line", new EditBorder("in_assets.1.dictionary_asset_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                form.get("in_assets.1.dictionary_asset_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.1.dictionary_asset_line").setVisible(inInstance.getIn_assets().get(1).getVisible() == null ? false : inInstance.getIn_assets().get(1).getVisible());

                TextField ddc5 = new TextField("in_assets.1.count_of_id", model.bind("in_assets.1.count_of"));
                form.add(new LineBorder("in_assets.1.count_of_line", new EditBorder("in_assets.1.count_of_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);
                form.get("in_assets.1.count_of_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.1.count_of_line").setVisible(inInstance.getIn_assets().get(1).getVisible() == null ? false : inInstance.getIn_assets().get(1).getVisible());

                AjaxCheckBox ddc6 = new AjaxCheckBox("in_assets.1.selected_id", model.bind("in_assets.1.selected")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                             
                        }                    
                                                        };
                form.add(new LineBorder("in_assets.1.selected_line", new EditBorder("in_assets.1.selected_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                form.get("in_assets.1.selected_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.1.selected_line").setVisible(inInstance.getIn_assets().get(1).getVisible() == null ? false : inInstance.getIn_assets().get(1).getVisible());

                DropDownChoice ddc7 = new DropDownChoice("in_assets.2.dictionary_asset_id",
                    dictionary.getDictionary("assetList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc7.setModel(new DictionaryModelWrapper(model.bind("in_assets.2.dictionary_asset"), ddc7));
                ddc7.setNullValid(true);
                form.add(new LineBorder("in_assets.2.dictionary_asset_line", new EditBorder("in_assets.2.dictionary_asset_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                form.get("in_assets.2.dictionary_asset_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.2.dictionary_asset_line").setVisible(inInstance.getIn_assets().get(2).getVisible() == null ? false : inInstance.getIn_assets().get(2).getVisible());

                TextField ddc8 = new TextField("in_assets.2.count_of_id", model.bind("in_assets.2.count_of"));
                form.add(new LineBorder("in_assets.2.count_of_line", new EditBorder("in_assets.2.count_of_border", ddc8, true)));
                ddc8.setOutputMarkupId(true);
                form.get("in_assets.2.count_of_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.2.count_of_line").setVisible(inInstance.getIn_assets().get(2).getVisible() == null ? false : inInstance.getIn_assets().get(2).getVisible());

                AjaxCheckBox ddc9 = new AjaxCheckBox("in_assets.2.selected_id", model.bind("in_assets.2.selected")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                             
                        }                    
                                                        };
                form.add(new LineBorder("in_assets.2.selected_line", new EditBorder("in_assets.2.selected_border", ddc9, true)));
                ddc9.setOutputMarkupId(true);
                form.get("in_assets.2.selected_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.2.selected_line").setVisible(inInstance.getIn_assets().get(2).getVisible() == null ? false : inInstance.getIn_assets().get(2).getVisible());

                DropDownChoice ddc10 = new DropDownChoice("in_assets.3.dictionary_asset_id",
                    dictionary.getDictionary("assetList", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc10.setModel(new DictionaryModelWrapper(model.bind("in_assets.3.dictionary_asset"), ddc10));
                ddc10.setNullValid(true);
                form.add(new LineBorder("in_assets.3.dictionary_asset_line", new EditBorder("in_assets.3.dictionary_asset_border", ddc10, true)));
                ddc10.setOutputMarkupId(true);
                form.get("in_assets.3.dictionary_asset_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.3.dictionary_asset_line").setVisible(inInstance.getIn_assets().get(3).getVisible() == null ? false : inInstance.getIn_assets().get(3).getVisible());

                TextField ddc11 = new TextField("in_assets.3.count_of_id", model.bind("in_assets.3.count_of"));
                form.add(new LineBorder("in_assets.3.count_of_line", new EditBorder("in_assets.3.count_of_border", ddc11, true)));
                ddc11.setOutputMarkupId(true);
                form.get("in_assets.3.count_of_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.3.count_of_line").setVisible(inInstance.getIn_assets().get(3).getVisible() == null ? false : inInstance.getIn_assets().get(3).getVisible());

                AjaxCheckBox ddc12 = new AjaxCheckBox("in_assets.3.selected_id", model.bind("in_assets.3.selected")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                             
                        }                    
                                                        };
                form.add(new LineBorder("in_assets.3.selected_line", new EditBorder("in_assets.3.selected_border", ddc12, true)));
                ddc12.setOutputMarkupId(true);
                form.get("in_assets.3.selected_line").setOutputMarkupPlaceholderTag(true);
                form.get("in_assets.3.selected_line").setVisible(inInstance.getIn_assets().get(3).getVisible() == null ? false : inInstance.getIn_assets().get(3).getVisible());

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            setResponsePage(new ClientIncomeInfoPage());
                    }           
                                    });
                form.addOrReplace(new Link("new") {
                    @Override 
                    public void onClick()  {
            for (int index = 0; index < 4; index += 1) {
                if (inInstance.getIn_assets().get(index).getVisible() == null
                    || !inInstance.getIn_assets().get(index).getVisible()) {
                    inInstance.getIn_assets().get(index).setVisible(true);
                    form.get("in_assets." + index + ".dictionary_asset_line").setVisible(true);
                    form.get("in_assets." + index + ".count_of_line").setVisible(true);
                    form.get("in_assets." + index + ".selected_line").setVisible(true);
                    break;
                }
            };
            setVisibleForDeleteButton(form);
                    }           
                                    });
                form.addOrReplace(new Link("delete") {
                    @Override 
                    public void onClick()  {
            for (int index = 0; index < 4; index += 1) {
                if (inInstance.getIn_assets().get(index).getVisible() != null
                    && inInstance.getIn_assets().get(index).getVisible()
                    && inInstance.getIn_assets().get(index).getSelected() != null
                    && inInstance.getIn_assets().get(index).getSelected()) {
                    inInstance.getIn_assets().get(index).setVisible(false);
                    form.get("in_assets." + index + ".dictionary_asset_line").setVisible(false);
                    form.get("in_assets." + index + ".count_of_line").setVisible(false);
                    form.get("in_assets." + index + ".selected_line").setVisible(false);
                }
            };
            setVisibleForDeleteButton(form);
                    }           
                                    });
                
            form.get("delete").setOutputMarkupPlaceholderTag(true);
            setVisibleForDeleteButton(form)
            ;
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            boolean guarantorPresent = inInstance.getIn_guarantor() != null && inInstance.getIn_guarantor().getTin() != null && !inInstance.getIn_guarantor().getTin().isEmpty();
            saveWorkflowPath(guarantorPresent ? "GuaranteePartnerInfoPage" : "ClientContactInfoPage");
            org.bitbucket.creditauto.wicket.InstanceHelper.save(inInstance);
            setResponsePage(guarantorPresent ? new GuaranteePartnerInfoPage(inInstance) : new ClientContactInfoPage());
                    }           
                                    });
    }
        
        public void setVisibleForDeleteButton(Form form) {
            int countVisible = 0;
            for (int index = 0; index < 4; index += 1) {
                if (inInstance.getIn_assets().get(index).getVisible() != null
                    && inInstance.getIn_assets().get(index).getVisible()) {
                    countVisible += 1;
                }
            }
            form.get("delete").setVisible(countVisible > 0);
        }
        
    }
    }    
}
