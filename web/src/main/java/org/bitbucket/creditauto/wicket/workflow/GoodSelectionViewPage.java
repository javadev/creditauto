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

public class GoodSelectionViewPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public GoodSelectionViewPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public GoodSelectionViewPage(In_instance inInstance) {
                this.inInstance = inInstance;
        init();
    }
    private void init() {
        dictionary = new DictionaryServerImpl();
        add(createPanel("panel"));
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
        return "Выбор типа товара";
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
            
                DictionaryTextField ddc1 = new DictionaryTextField("in_goods.0.dictionary_carBrand_id",
                    dictionary.getDictionary("carBrand", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("in_goods.0.dictionary_carBrand"), ddc1));
                form.add(new LineBorder("in_goods.0.dictionary_carBrand_line", new EditBorder("in_goods.0.dictionary_carBrand_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);
                ddc1.setEnabled(false);

                DictionaryTextField ddc2 = new DictionaryTextField("in_goods.0.dictionary_carModel_id",
                    dictionary.getDictionary("carModel", inInstance.getIn_goods().get(0).getDictionary_carBrand(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc2.setModel(new DictionaryModelWrapper(model.bind("in_goods.0.dictionary_carModel"), ddc2));
                form.add(new LineBorder("in_goods.0.dictionary_carModel_line", new EditBorder("in_goods.0.dictionary_carModel_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);
                ddc2.setEnabled(false);

                TextField ddc3 = new TextField("in_goods.0.price_id", model.bind("in_goods.0.price"));
                form.add(new LineBorder("in_goods.0.price_line", new EditBorder("in_goods.0.price_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);
                ddc3.setEnabled(false);

                TextField ddc4 = new TextField("in_dossier.down_payment_value_id", model.bind("in_dossier.down_payment_value"));
                form.add(new LineBorder("in_dossier.down_payment_value_line", new EditBorder("in_dossier.down_payment_value_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                ddc4.setEnabled(false);

                DictionaryTextField ddc5 = new DictionaryTextField("in_dossier.down_payment_calc_method_id",
                    dictionary.getDictionary("priceVariant", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc5.setModel(new DictionaryModelWrapper(model.bind("in_dossier.down_payment_calc_method"), ddc5));
                form.add(new LineBorder("in_dossier.down_payment_calc_method_line", new EditBorder("in_dossier.down_payment_calc_method_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);
                ddc5.setEnabled(false);

                TextField ddc6 = new TextField("in_dossier.down_payment_prc_id", model.bind("in_dossier.down_payment_prc"));
                form.add(new LineBorder("in_dossier.down_payment_prc_line", new EditBorder("in_dossier.down_payment_prc_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                ddc6.setEnabled(false);

                TextField ddc7 = new TextField("in_dossier.down_payment_id", model.bind("in_dossier.down_payment"));
                form.add(new LineBorder("in_dossier.down_payment_line", new EditBorder("in_dossier.down_payment_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                ddc7.setEnabled(false);

                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            setResponsePage(new CarDetailsInfoViewPage(inInstance));
                    }           
                                    });
    }
        
    }
    }    
}
