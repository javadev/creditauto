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

public class PartnerPassportPanel extends Panel {
    protected IDictionary dictionary;
    private final   In_person modelObject;
    
    
        public PartnerPassportPanel(String id, IModel model) {
            super(id, model);
            this.modelObject = (In_person)this.getDefaultModelObject();
                        
                        init();
        }
    private void init() {
        dictionary = new DictionaryServerImpl();
    CompoundPropertyModel model = new CompoundPropertyModel(modelObject); 
            final  Panel form = this;
            form.setOutputMarkupId(true);
            
                TextField ddc1 = new TextField("last_name_ru_id", model.bind("last_name_ru"));
                form.add(new LineBorder("last_name_ru_line", new EditBorder("last_name_ru_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);

                TextField ddc2 = new TextField("first_name_ru_id", model.bind("first_name_ru"));
                form.add(new LineBorder("first_name_ru_line", new EditBorder("first_name_ru_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);

                TextField ddc3 = new TextField("patronymic_name_ru_id", model.bind("patronymic_name_ru"));
                form.add(new LineBorder("patronymic_name_ru_line", new EditBorder("patronymic_name_ru_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);

                TextField ddc4 = new TextField("birthday_id", model.bind("birthday"));
                form.add(new LineBorder("birthday_line", new EditBorder("birthday_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);

                AjaxCheckBox ddc5 = new AjaxCheckBox("has_no_tin_id", model.bind("has_no_tin")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            
                       modelObject.setTin(((Boolean) this.getConvertedInput()) != null && ((Boolean) this.getConvertedInput()) ? "0000000000" : "");
                       form.get("tin_line").setVisible(!((Boolean) this.getConvertedInput()));
                       form.get("date_of_issue_tin_certificate_line").setVisible(!((Boolean) this.getConvertedInput()));
                       form.get("date_of_giving_tin_certificate_line").setVisible(!((Boolean) this.getConvertedInput()));
                       form.get("issuer_of_tin_line").setVisible(!((Boolean) this.getConvertedInput()));
                       target.addComponent((LineBorder) form.get("tin_line"));
                       target.addComponent((LineBorder) form.get("date_of_issue_tin_certificate_line"));
                       target.addComponent((LineBorder) form.get("date_of_giving_tin_certificate_line"));
                       target.addComponent((LineBorder) form.get("issuer_of_tin_line"));
           
                        }                    
                                                        };
                form.add(new LineBorder("has_no_tin_line", new EditBorder("has_no_tin_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);

                TextField ddc6 = new TextField("tin_id", model.bind("tin"));
                form.add(new LineBorder("tin_line", new EditBorder("tin_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                form.get("tin_line").setOutputMarkupPlaceholderTag(true);
                form.get("tin_line").setVisible(modelObject.getHas_no_tin() == null ? true : !modelObject.getHas_no_tin());

                TextField ddc7 = new TextField("date_of_issue_tin_certificate_id", model.bind("date_of_issue_tin_certificate"));
                form.add(new LineBorder("date_of_issue_tin_certificate_line", new EditBorder("date_of_issue_tin_certificate_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                form.get("date_of_issue_tin_certificate_line").setOutputMarkupPlaceholderTag(true);
                form.get("date_of_issue_tin_certificate_line").setVisible(modelObject.getHas_no_tin() == null ? true : !modelObject.getHas_no_tin());

                TextField ddc8 = new TextField("date_of_giving_tin_certificate_id", model.bind("date_of_giving_tin_certificate"));
                form.add(new LineBorder("date_of_giving_tin_certificate_line", new EditBorder("date_of_giving_tin_certificate_border", ddc8, true)));
                ddc8.setOutputMarkupId(true);
                form.get("date_of_giving_tin_certificate_line").setOutputMarkupPlaceholderTag(true);
                form.get("date_of_giving_tin_certificate_line").setVisible(modelObject.getHas_no_tin() == null ? true : !modelObject.getHas_no_tin());

                TextField ddc9 = new TextField("issuer_of_tin_id", model.bind("issuer_of_tin"));
                form.add(new LineBorder("issuer_of_tin_line", new EditBorder("issuer_of_tin_border", ddc9, true)));
                ddc9.setOutputMarkupId(true);
                form.get("issuer_of_tin_line").setOutputMarkupPlaceholderTag(true);
                form.get("issuer_of_tin_line").setVisible(modelObject.getHas_no_tin() == null ? true : !modelObject.getHas_no_tin());

                DropDownChoice ddc10 = new DropDownChoice("citizenship_id",
                    dictionary.getDictionary("citizenship", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc10.setModel(new DictionaryModelWrapper(model.bind("citizenship"), ddc10));
                ddc10.setNullValid(true);
                form.add(new LineBorder("citizenship_line", new EditBorder("citizenship_border", ddc10, true)));
                ddc10.setOutputMarkupId(true);

                DropDownChoice ddc11 = new DropDownChoice("dict_identity_document_type_id",
                    dictionary.getDictionary("docTypes", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc11.setModel(new DictionaryModelWrapper(model.bind("dict_identity_document_type"), ddc11));
                ddc11.setNullValid(true);
                form.add(new LineBorder("dict_identity_document_type_line", new EditBorder("dict_identity_document_type_border", ddc11, true)));
                ddc11.setOutputMarkupId(true);

                TextField ddc12 = new TextField("identity_document_number_id", model.bind("identity_document_number"));
                form.add(new LineBorder("identity_document_number_line", new EditBorder("identity_document_number_border", ddc12, true)));
                ddc12.setOutputMarkupId(true);

                TextField ddc13 = new TextField("date_of_issuing_identity_doc_id", model.bind("date_of_issuing_identity_doc"));
                form.add(new LineBorder("date_of_issuing_identity_doc_line", new EditBorder("date_of_issuing_identity_doc_border", ddc13, true)));
                ddc13.setOutputMarkupId(true);

                TextField ddc14 = new TextField("issuer_of_identity_document_id", model.bind("issuer_of_identity_document"));
                form.add(new LineBorder("issuer_of_identity_document_line", new EditBorder("issuer_of_identity_document_border", ddc14, true)));
                ddc14.setOutputMarkupId(true);

                TextField ddc15 = new TextField("phone_id", model.bind("phone"));
                form.add(new LineBorder("phone_line", new EditBorder("phone_border", ddc15, true)));
                ddc15.setOutputMarkupId(true);

                TextField ddc16 = new TextField("mobile_phone_id", model.bind("mobile_phone"));
                form.add(new LineBorder("mobile_phone_line", new EditBorder("mobile_phone_border", ddc16, true)));
                ddc16.setOutputMarkupId(true);

    }
        
    
}
