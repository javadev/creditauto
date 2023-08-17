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

public class UserShopsPage extends UserPowerOfAttorneyPage {
    
    
    public UserShopsPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    public UserShopsPage(User user) {
        super( user );
        this.inInstance = getCreditautoSession().getInInstance();
        this.user = user;
            if (this.user.getExternaldistributors() == null ) {
                 this.user.setExternaldistributors(new ArrayList<Externaldistributor>());
            }
            this.user.getExternaldistributors().add(0, new Externaldistributor());
            
        init();
    }
    
    public UserShopsPage(In_instance inInstance) {
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

    protected class MainPanel extends UserPowerOfAttorneyPage.MainPanel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends UserPowerOfAttorneyPage.MainPanel.MainForm {
        
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel(user
        );
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                TextField ddc1 = new TextField("externaldistributors.0.ext_id_id", model.bind("externaldistributors.0.ext_id"));
                form.add(new LineBorder("externaldistributors.0.ext_id_line", new EditBorder("externaldistributors.0.ext_id_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);
                form.get("externaldistributors.0.ext_id_line").setOutputMarkupPlaceholderTag(true);

    AjaxSubmitLink ddc2 = new AjaxSubmitLink("addShop_id") {
            @Override
             protected void onSubmit(AjaxRequestTarget target, Form form)  {
                 if (user.getExternaldistributors() != null && !user.getExternaldistributors().isEmpty() && user.getExternaldistributors().get(0) != null) {
                 Long result = new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().addUserExternaldistributor(user, user.getExternaldistributors().get(0).getExt_id());
                 if (result == -1) {
                     modal2.setContent(new ModalTextOnlyPanel(modal2.getContentId(), "Магазин с номером " + user.getExternaldistributors().get(0).getExt_id() + " не найден.", false));
                     modal2.show(target);
                 } 
                 if (result == -2) {
                     modal2.setContent(new ModalTextOnlyPanel(modal2.getContentId(), "Магазин с номером " + user.getExternaldistributors().get(0).getExt_id() + " уже привязан к пользователю " + user.getName() + ".", false));
                     modal2.show(target);
                 } 
                  if (result > -1) {
                    ((ListView)form.get("listShops_container:listShops")).setDefaultModelObject(new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getUserExternaldistributors(user));
                     target.addComponent(form.get("listShops_container"));
                     user.getExternaldistributors().add(0, new Externaldistributor());
                     form.setModelObject(user);
                     target.addComponent((LineBorder) form.get("externaldistributors.0.ext_id_line"));
                 }
             }
              
            }
    };
             ddc2.setDefaultFormProcessing(false);
             form.add(new LineBorder("addShop_line", ddc2));
        
                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            saveWorkflowPath("UserManagementPage");
         setResponsePage(new UserManagementPage(user));
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            
                    }           
                                    });
                form.get("next").setVisible(false);
//repeaters
WebMarkupContainer listViewContainer = new WebMarkupContainer("listShops_container");
    ListView listview = new ListView("listShops", new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getUserExternaldistributors(user)) {
        @Override 
        protected void populateItem(ListItem item) {
            final Externaldistributor modelObject = (Externaldistributor)item.getModelObject();
            CompoundPropertyModel model = new CompoundPropertyModel(modelObject);
            item.setModel(model);
                                                          item.add(new Label("ext_id", model.bind("ext_id")));
                                                                                     item.add(new Label("name", model.bind("name")));
                                                                                     item.add(new Label("city", model.bind("city")));
                                                                                     item.add(new Label("province", model.bind("province")));
                                                                                     item.add(new Label("street", model.bind("street")));
                                                                                     item.add(new Label("house", model.bind("house")));
                                                                                       item.add(new AjaxLink("droplink") {
                     @Override 
                     public void onClick (AjaxRequestTarget target) {
                         modal2.setContent(new ModalTextOnlyPanel(modal2.getContentId(), "Вы уверенны, что хотите отвязать магазин " + modelObject.getExt_id() + " от пользователя " + user.getName() + "?", true) {
                   @Override
                   protected  void onConfirm(AjaxRequestTarget target) {
                       Long result = new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().deleteUserExternaldistributor(user, modelObject);
                       ((ListView)form.get("listShops_container:listShops")).setDefaultModelObject(new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getUserExternaldistributors(user));
                       target.addComponent(form.get("listShops_container")); 
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
