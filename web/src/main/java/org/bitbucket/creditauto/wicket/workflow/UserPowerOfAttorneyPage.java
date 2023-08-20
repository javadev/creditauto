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

public class UserPowerOfAttorneyPage extends UserManagementPage {
    
    
        private SimpleDateFormat format;
        private ITarification tarification;
    public UserPowerOfAttorneyPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    public UserPowerOfAttorneyPage(User user) {
        super( user );
        this.inInstance = getCreditautoSession().getInInstance();
        this.user = user;
            if (this.user.getPowerofattorneys() == null ) {
                 this.user.setPowerofattorneys(new ArrayList<Powerofattorney>());
            }
            tarification = new TarificationServerImpl();
            this.user.getPowerofattorneys().add(0, new Powerofattorney());
            this.user.getPowerofattorneys().get(0).setProduct(!tarification.getProductTypes().isEmpty() ? tarification.getProductTypes().get(0) : null);
            format = new SimpleDateFormat("yyyy-MM-dd");
            
        init();
    }
    
    public UserPowerOfAttorneyPage(In_instance inInstance) {
        super(inInstance);
        this.inInstance = inInstance;
        init();
    }
    private void init() {
        replace(createPanel("panel"));
    }

    public Panel createPanel(String panelId) {
        MainPanel panel = new MainPanel(panelId);
        panel.add(panel.new MainForm("form"));
        return panel;
    }

    public static String getTitle() {
        return "Параметры пользователя";
    }

    protected class MainPanel extends UserManagementPage.MainPanel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends UserManagementPage.MainPanel.MainForm {
        
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel(user
        );
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                TextField ddc1 = new TextField("powerofattorneys.0.attorney_number_id", model.bind("powerofattorneys.0.attorney_number"));
                form.add(new LineBorder("powerofattorneys.0.attorney_number_line", new EditBorder("powerofattorneys.0.attorney_number_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);

                TextField ddc2 = new TextField("powerofattorneys.0.attorney_date_start_id", model.bind("powerofattorneys.0.attorney_date_start"));
                form.add(new LineBorder("powerofattorneys.0.attorney_date_start_line", new EditBorder("powerofattorneys.0.attorney_date_start_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);

                TextField ddc3 = new TextField("powerofattorneys.0.attorney_date_finish_id", model.bind("powerofattorneys.0.attorney_date_finish"));
                form.add(new LineBorder("powerofattorneys.0.attorney_date_finish_line", new EditBorder("powerofattorneys.0.attorney_date_finish_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);

                DropDownChoice ddc4 = new DropDownChoice("powerofattorneys.0.dictionary_type_of_attorney_id",
                    dictionary.getDictionary("attorney_types", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc4.setModel(new DictionaryModelWrapper(model.bind("powerofattorneys.0.dictionary_type_of_attorney"), ddc4));
                ddc4.setNullValid(true);
                form.add(new LineBorder("powerofattorneys.0.dictionary_type_of_attorney_line", new EditBorder("powerofattorneys.0.dictionary_type_of_attorney_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);

                DropDownChoice ddc5 = new DropDownChoice("powerofattorneys.0.product_id", model.bind("powerofattorneys.0.product"),
                    new TarificationServerImpl().getProductTypes(),
                        new ChoiceRenderer("name", "id"));
                form.add(new LineBorder("powerofattorneys.0.product_line", new EditBorder("powerofattorneys.0.product_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);

                DropDownChoice ddc6 = new DropDownChoice("powerofattorneys.0.dictionary_type_purpose_id",
                    dictionary.getDictionary("attorney_purposes", IDictionary.LANGUAGE_RU, new Date()),
                        new ChoiceRenderer("dvalue"));
                ddc6.setModel(new DictionaryModelWrapper(model.bind("powerofattorneys.0.dictionary_type_purpose"), ddc6));
                ddc6.setNullValid(true);
                form.add(new LineBorder("powerofattorneys.0.dictionary_type_purpose_line", new EditBorder("powerofattorneys.0.dictionary_type_purpose_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);

    AjaxSubmitLink ddc7 = new AjaxSubmitLink("addPow_id") {
            @Override
             protected void onSubmit(AjaxRequestTarget target, Form form)  {
                 if (user.getPowerofattorneys().get(0) != null) {
                 user.getPowerofattorneys().get(0).setUser(user);
                 user.getPowerofattorneys().get(0).setExternaldistributor(getCreditautoSession().getUserShop());
                 new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().save(user.getPowerofattorneys().get(0));
                 ((ListView)form.get("list_container:list")).setModelObject(new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getUserPowerofattorneys(user));
                 target.addComponent(form.get("list_container"));
                 user.getPowerofattorneys().add(0, new Powerofattorney());
                 user.getPowerofattorneys().get(0).setProduct(!tarification.getProductTypes().isEmpty() ? tarification.getProductTypes().get(0) : null);
                 form.setModelObject(user);
                 target.addComponent(form);
             }
        
            }
    };
             ddc7.setDefaultFormProcessing(false);
             form.add(new LineBorder("addPow_line", ddc7));
        
                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            setResponsePage(new UserManagementPage(user));
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            saveWorkflowPath("UserShopsPage");
            setResponsePage(new UserShopsPage(user));
                    }           
                                    });
//repeaters
WebMarkupContainer listViewContainer = new WebMarkupContainer("list_container");
    ListView listview = new ListView("list", new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getUserPowerofattorneys(user)) {
        @Override 
        protected void populateItem(ListItem item) {
            final Powerofattorney modelObject = (Powerofattorney)item.getModelObject();
            CompoundPropertyModel model = new CompoundPropertyModel(modelObject);
            item.setModel(model);
                                                          item.add(new Label("attorney_number", model.bind("attorney_number")));
                                                                                     item.add(new Label("attorney_date_start", new Model(modelObject.getAttorney_date_start() != null ? format.format(modelObject.getAttorney_date_start()) : "")));
                                                                                     item.add(new Label("attorney_date_finish", new Model(modelObject.getAttorney_date_finish() != null ? format.format(modelObject.getAttorney_date_finish()) : "")));
                                                                                     item.add(new Label("dictionary_type_of_attorney", model.bind("dictionary_type_of_attorney")));
                                                                                     item.add(new Label("product.name", model.bind("product.name")));
                                                                                     item.add(new Label("dictionary_type_purpose", model.bind("dictionary_type_purpose")));
                                                                                       item.add(new AjaxLink("droplink") {
                     @Override 
                     public void onClick (AjaxRequestTarget target) {
                         modal2.setContent(new ModalTextOnlyPanel(modal2.getContentId(), "Вы уверенны, что хотите звершить доверенность " + modelObject.getProduct().getName() + " №" + modelObject.getAttorney_number() + " пользователя " + user.getName() + "?", true) {
                   @Override
                   protected  void onConfirm(AjaxRequestTarget target) {
                        Long result = new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().deleteUserPowerofattorney(user, modelObject);
                       ((ListView)form.get("list_container:list")).setDefaultModelObject(new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getUserPowerofattorneys(user));
                       target.addComponent(form.get("list_container")); 
                       modal2.close(target);
                   }
                   @Override
                   protected  void onCancel(AjaxRequestTarget target) {
                       modal2.close(target);
                   }
               });
               modal2.show(target);
                     }           
                });
                                                   }
        @Override
        protected ListItem newItem(int index) {
            return new OddEvenListItem(index, getListItemModel(getModel(), index));
        }
    };
    listViewContainer.setOutputMarkupId(true);
    form.add(listViewContainer.add(listview));

//end of repeaters
    }
        
    }
    }    
}
