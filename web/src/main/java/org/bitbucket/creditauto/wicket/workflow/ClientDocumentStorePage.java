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

public class ClientDocumentStorePage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public ClientDocumentStorePage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public ClientDocumentStorePage(In_instance inInstance) {
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
                Panel panel = new ClientAssetPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(ThirdPersonInfoPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new ThirdPersonInfoPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(GuaranteePartnerInfoPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new GuaranteePartnerInfoPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(PrintApplicationFormPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new PrintApplicationFormPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(ClientDocumentStorePage.getTitle())) {
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
        ajaxTabbedPanel.setSelectedTab(13 - 1);
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
        return "Требуемые Скан-копии документов";
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
            
                FileUploadField ddc1 = new FileUploadField("in_document_stores.0.data_id");
                ddc1.setModel(new FileUploadModelWrapper(model.bind("in_document_stores.0")));
                form.add(new LineBorder("in_document_stores.0.data_line", new EditBorder("in_document_stores.0.data_border", ddc1, false)));
                ddc1.setOutputMarkupId(true);
                form.add(new org.bitbucket.creditauto.wicket.InDocumentStoreValidator(ddc1, inInstance.getIn_document_stores().get(0)));

    AjaxLink ddc2 = new AjaxLink("preview_doc_0_link_id") {
            @Override
             public void onClick(AjaxRequestTarget target)  {
                setResponsePage(new org.bitbucket.creditauto.wicket.PreviewPage("Паспорт клиента", inInstance.getIn_document_stores().get(0), ClientDocumentStorePage.this)); 
        
            }
    };
             form.add(new LineBorder("preview_doc_0_link_line", ddc2));
                        form.get("preview_doc_0_link_line").setOutputMarkupPlaceholderTag(true);
                form.get("preview_doc_0_link_line").setVisible(Boolean.FALSE == null ? false : Boolean.FALSE);
    
                AjaxCheckBox ddc3 = new AjaxCheckBox("in_document_stores.0.is_not_required_id", model.bind("in_document_stores.0.is_not_required")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                             
                        }                    
                                                        };
                form.add(new LineBorder("in_document_stores.0.is_not_required_line", new EditBorder("in_document_stores.0.is_not_required_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);

                FileUploadField ddc4 = new FileUploadField("in_document_stores.1.data_id");
                ddc4.setModel(new FileUploadModelWrapper(model.bind("in_document_stores.1")));
                form.add(new LineBorder("in_document_stores.1.data_line", new EditBorder("in_document_stores.1.data_border", ddc4, false)));
                ddc4.setOutputMarkupId(true);
                form.add(new org.bitbucket.creditauto.wicket.InDocumentStoreValidator(ddc4, inInstance.getIn_document_stores().get(1)));

    AjaxLink ddc5 = new AjaxLink("preview_doc_1_link_id") {
            @Override
             public void onClick(AjaxRequestTarget target)  {
                setResponsePage(new org.bitbucket.creditauto.wicket.PreviewPage("ИНН клиента", inInstance.getIn_document_stores().get(1), ClientDocumentStorePage.this)); 
        
            }
    };
             form.add(new LineBorder("preview_doc_1_link_line", ddc5));
                        form.get("preview_doc_1_link_line").setOutputMarkupPlaceholderTag(true);
                form.get("preview_doc_1_link_line").setVisible(Boolean.FALSE == null ? false : Boolean.FALSE);
    
                AjaxCheckBox ddc6 = new AjaxCheckBox("in_document_stores.1.is_not_required_id", model.bind("in_document_stores.1.is_not_required")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                             
                        }                    
                                                        };
                form.add(new LineBorder("in_document_stores.1.is_not_required_line", new EditBorder("in_document_stores.1.is_not_required_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);

                FileUploadField ddc7 = new FileUploadField("in_document_stores.2.data_id");
                ddc7.setModel(new FileUploadModelWrapper(model.bind("in_document_stores.2")));
                form.add(new LineBorder("in_document_stores.2.data_line", new EditBorder("in_document_stores.2.data_border", ddc7, false)));
                ddc7.setOutputMarkupId(true);
                form.add(new org.bitbucket.creditauto.wicket.InDocumentStoreValidator(ddc7, inInstance.getIn_document_stores().get(2)));

    AjaxLink ddc8 = new AjaxLink("preview_doc_2_link_id") {
            @Override
             public void onClick(AjaxRequestTarget target)  {
                setResponsePage(new org.bitbucket.creditauto.wicket.PreviewPage("Фото клиента", inInstance.getIn_document_stores().get(2), ClientDocumentStorePage.this)); 
        
            }
    };
             form.add(new LineBorder("preview_doc_2_link_line", ddc8));
                        form.get("preview_doc_2_link_line").setOutputMarkupPlaceholderTag(true);
                form.get("preview_doc_2_link_line").setVisible(Boolean.FALSE == null ? false : Boolean.FALSE);
    
                AjaxCheckBox ddc9 = new AjaxCheckBox("in_document_stores.2.is_not_required_id", model.bind("in_document_stores.2.is_not_required")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                             
                        }                    
                                                        };
                form.add(new LineBorder("in_document_stores.2.is_not_required_line", new EditBorder("in_document_stores.2.is_not_required_border", ddc9, true)));
                ddc9.setOutputMarkupId(true);

                AjaxCheckBox ddc10 = new AjaxCheckBox("in_dossier.preview_documents_id", model.bind("in_dossier.preview_documents")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                             
                        }                    
                                                        };
                form.add(new LineBorder("in_dossier.preview_documents_line", new EditBorder("in_dossier.preview_documents_border", ddc10, true)));
                ddc10.setOutputMarkupId(true);
                inInstance.getIn_dossier().setPreview_documents(true);

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            setResponsePage(new PrintApplicationFormPage());
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            if (inInstance.getIn_dossier().getPreview_documents()) {
                if (!inInstance.getIn_document_stores().get(0).getIs_not_required()) {
                form.get("preview_doc_0_link_line").setVisible(true);
                }
                if (!inInstance.getIn_document_stores().get(1).getIs_not_required()) {
                form.get("preview_doc_1_link_line").setVisible(true);
                }
                if (!inInstance.getIn_document_stores().get(2).getIs_not_required()) {
                form.get("preview_doc_2_link_line").setVisible(true);
                }
            } else {
            saveWorkflowPath("ConfirmSendRequestPage");
            // TODO: change to the new DossierAnalyserServerImpl().startAnalyse()
            inInstance.getIn_dossier().setDict_status_of_dossier(InDossierStatus.TOBE_STUDY.getKey());
            org.bitbucket.creditauto.wicket.InstanceHelper.save(inInstance);
            setResponsePage(new ConfirmSendRequestPage()); }
                    }           
                                    });
    }
        
    }
    }    
}
