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

public class GoodSelectionPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public GoodSelectionPage() {
        super();
                
            org.bitbucket.creditauto.entity.Product product = new org.bitbucket.creditauto.entity.Product();
            product.setId(1L);
            product.setName("CAR");
            getCreditautoSession().setInInstance(new DossierServerServerImpl().initDossier(
                getCreditautoSession().getUserShop(), getCreditautoSession().getUser(), product).inInstance);
        
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public GoodSelectionPage(In_instance inInstance) {
                this.inInstance = inInstance;
        init();
    }
    private void init() {
        dictionary = new DictionaryServerImpl();
        List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTabWithForm(new Model<String>(GoodSelectionPage.getTitle())) {
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
        ajaxTabbedPanel.setSelectedTab(1 - 1);
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
            
                DropDownChoice ddc1 = new DropDownChoice("in_goods.0.dictionary_carBrand_id",
                    dictionary.getDictionary("carBrand", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc1.setModel(new DictionaryModelWrapper(model.bind("in_goods.0.dictionary_carBrand"), ddc1));
                ddc1.setNullValid(true);
                EditBorder.OnChange change1 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                DropDownChoice carBrand = (DropDownChoice) form.get("in_goods.0.dictionary_carBrand_line:in_goods.0.dictionary_carBrand_border:in_goods.0.dictionary_carBrand_id");
                DropDownChoice carModel = (DropDownChoice) form.get("in_goods.0.dictionary_carModel_line:in_goods.0.dictionary_carModel_border:in_goods.0.dictionary_carModel_id");
                inInstance.getIn_goods().get(0).setDictionary_carModel(null);
                carModel.setChoices(dictionary.getDictionary("carModel", ((Dictionary_data) carBrand.getModelObject()).getDkey(), IDictionary.LANGUAGE_RU, new Date()));
                target.addComponent(carModel);
            
                    }
                };
                form.add(new LineBorder("in_goods.0.dictionary_carBrand_line", new EditBorder("in_goods.0.dictionary_carBrand_border", ddc1, true, change1)));
                ddc1.setOutputMarkupId(true);

                DropDownChoice ddc2 = new DropDownChoice("in_goods.0.dictionary_carModel_id",
                    dictionary.getDictionary("carModel", inInstance.getIn_goods().get(0).getDictionary_carBrand(), IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc2.setModel(new DictionaryModelWrapper(model.bind("in_goods.0.dictionary_carModel"), ddc2));
                ddc2.setNullValid(true);
                EditBorder.OnChange change2 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                DropDownChoice carModel = (DropDownChoice) form.get("in_goods.0.dictionary_carModel_line:in_goods.0.dictionary_carModel_border:in_goods.0.dictionary_carModel_id");
                DropDownChoice carColor = (DropDownChoice) form.get("in_goods.0.dict_car_color_line:in_goods.0.dict_car_color_border:in_goods.0.dict_car_color_id");
                DropDownChoice carBodyType = (DropDownChoice) form.get("in_goods.0.dict_car_body_type_line:in_goods.0.dict_car_body_type_border:in_goods.0.dict_car_body_type_id");
                
                if (carColor != null && carBodyType != null) {
                   carColor.setChoices(dictionary.getDictionary("carColorList", 
                        ((Dictionary_data)((FormComponent)get("in_goods.0.dictionary_carModel_line:in_goods.0.dictionary_carModel_border:in_goods.0.dictionary_carModel_id")).getModelObject()).getDkey(),
                        IDictionary.LANGUAGE_RU, new Date()));
                   carBodyType.setChoices(dictionary.getDictionary("carBodyTypeList",
                       ((Dictionary_data)((FormComponent)get("in_goods.0.dictionary_carModel_line:in_goods.0.dictionary_carModel_border:in_goods.0.dictionary_carModel_id")).getModelObject()).getDkey(),
                        IDictionary.LANGUAGE_RU, new Date()));
                
                   target.addComponent(carColor);
                   target.addComponent(carBodyType);
                }
                
            
                    }
                };
                form.add(new LineBorder("in_goods.0.dictionary_carModel_line", new EditBorder("in_goods.0.dictionary_carModel_border", ddc2, true, change2)));
                ddc2.setOutputMarkupId(true);

                TextField ddc3 = new TextField("in_goods.0.price_id", model.bind("in_goods.0.price"));
                EditBorder.OnChange change3 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                    updateDownPayment(target);
            
                    }
                };
                form.add(new LineBorder("in_goods.0.price_line", new EditBorder("in_goods.0.price_border", ddc3, true, change3)));
                ddc3.setOutputMarkupId(true);

                TextField ddc4 = new TextField("in_dossier.down_payment_value_id", model.bind("in_dossier.down_payment_value"));
                EditBorder.OnChange change4 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                    updateDownPayment(target);
            
                    }
                };
                form.add(new LineBorder("in_dossier.down_payment_value_line", new EditBorder("in_dossier.down_payment_value_border", ddc4, true, change4)));
                ddc4.setOutputMarkupId(true);

                DropDownChoice ddc5 = new DropDownChoice("in_dossier.down_payment_calc_method_id",
                    dictionary.getDictionary("priceVariant", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc5.setModel(new DictionaryModelWrapper(model.bind("in_dossier.down_payment_calc_method"), ddc5));
                ddc5.setNullValid(true);
                EditBorder.OnChange change5 = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {

                    updateDownPayment(target);
            
                    }
                };
                form.add(new LineBorder("in_dossier.down_payment_calc_method_line", new EditBorder("in_dossier.down_payment_calc_method_border", ddc5, true, change5)));
                ddc5.setOutputMarkupId(true);
                if (inInstance.getIn_dossier().getDown_payment_calc_method() == null) { inInstance.getIn_dossier().setDown_payment_calc_method("1"); };
                ddc5.setNullValid(false);;

                TextField ddc6 = new TextField("in_dossier.down_payment_prc_id", model.bind("in_dossier.down_payment_prc"));
                form.add(new LineBorder("in_dossier.down_payment_prc_line", new EditBorder("in_dossier.down_payment_prc_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                form.add(new org.bitbucket.creditauto.wicket.validator.PercentValidator(
(FormComponent) form.get("in_dossier.down_payment_value_line:in_dossier.down_payment_value_border:in_dossier.down_payment_value_id"),
(FormComponent) form.get("in_dossier.down_payment_calc_method_line:in_dossier.down_payment_calc_method_border:in_dossier.down_payment_calc_method_id")));
            form.add(new org.bitbucket.creditauto.wicket.validator.SumValidator(
(FormComponent) form.get("in_dossier.down_payment_value_line:in_dossier.down_payment_value_border:in_dossier.down_payment_value_id"),
(FormComponent) form.get("in_dossier.down_payment_calc_method_line:in_dossier.down_payment_calc_method_border:in_dossier.down_payment_calc_method_id"),
(FormComponent) form.get("in_goods.0.price_line:in_goods.0.price_border:in_goods.0.price_id")));
            ;
                ddc6.setEnabled(false);

                TextField ddc7 = new TextField("in_dossier.down_payment_id", model.bind("in_dossier.down_payment"));
                form.add(new LineBorder("in_dossier.down_payment_line", new EditBorder("in_dossier.down_payment_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                ddc7.setEnabled(false);

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            
                    }           
                                    });
                form.get("back").setVisible(false);
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            updateDownPayment(new AjaxRequestTarget(GoodSelectionPage.this));
            org.bitbucket.creditauto.wicket.InstanceHelper.save(inInstance);
            saveWorkflowPath("GoodSelectionPage");
            saveWorkflowPath("CarDetailsInfoPage");
                    
            if (inInstance.getIn_dossier().getTotal_price() != null && inInstance.getIn_dossier().getDown_payment() != null) {
                inInstance.getIn_dossier().setAmount_of_loan(inInstance.getIn_dossier().getTotal_price()
                        .subtract(inInstance.getIn_dossier().getDown_payment()));
            }
            setResponsePage(new CarDetailsInfoPage(inInstance));
                    }           
                                    });
    }
        
        public void updateDownPayment(AjaxRequestTarget target) {
            if (inInstance.getIn_dossier().getDown_payment_calc_method() != null && 
                inInstance.getIn_goods() != null && !inInstance.getIn_goods().isEmpty() && 
                !java.math.BigDecimal.ZERO.equals(inInstance.getIn_goods().get(0).getPrice())) {
                            
                String calcType = inInstance.getIn_dossier().getDown_payment_calc_method();

                java.math.BigDecimal rez = org.bitbucket.creditauto.tarification.server.Calculator.calculateDownPayment(
                    inInstance.getIn_goods().get(0).getPrice(),
                    inInstance.getIn_dossier().getDown_payment_value(),
                    inInstance.getIn_dossier().getDown_payment_calc_method());
                inInstance.getIn_dossier().setDown_payment(calcType.equals(org.bitbucket.creditauto.tarification.server.Calculator.TYPE_PER_CENT) ? rez : inInstance.getIn_dossier().getDown_payment_value());
                inInstance.getIn_dossier().setDown_payment_prc(calcType.equals(org.bitbucket.creditauto.tarification.server.Calculator.TYPE_PER_CENT) ? inInstance.getIn_dossier().getDown_payment_value() : rez);
                inInstance.getIn_dossier().setTotal_price(inInstance.getIn_goods().get(0).getPrice());
            } 
            target.addComponent(get("in_dossier.down_payment_prc_line:in_dossier.down_payment_prc_border:in_dossier.down_payment_prc_id"));
            target.addComponent(get("in_dossier.down_payment_line:in_dossier.down_payment_border:in_dossier.down_payment_id"));
        }
    }
    }    
}
