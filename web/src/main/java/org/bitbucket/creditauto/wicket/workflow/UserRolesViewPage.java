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

public class UserRolesViewPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    protected  User user = new User();
        private Map<Long, Boolean> rolesMap = new HashMap<Long, Boolean>();
        
        public Map<Long, Boolean> getRolesMap(){
           return this.rolesMap;
        }
        public void setRolesMap(Map<Long, Boolean> rolesMap){
            this.rolesMap = rolesMap;
        }
        
        
    public UserRolesViewPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    public UserRolesViewPage(User user) {
        super();
        this.inInstance = getCreditautoSession().getInInstance();
        this.user = user;
            for(Urole role : new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getAllRoles()) {
                rolesMap.put(role.getId(), role.getUsers().contains(user));
            }
            
        init();
    }
    
    public UserRolesViewPage(In_instance inInstance) {
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
        return "Назначение ролей пользователю системы:";
    }

    protected class MainPanel extends Panel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends Form {
        
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel(user
        );
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            saveWorkflowPath("UserRolesViewPage");
        saveWorkflowPath("UserShopsViewPage");
        setResponsePage(new UserShopsViewPage(user));
                    }           
                                    });
//repeaters
WebMarkupContainer listViewContainer = new WebMarkupContainer("listRoles_container");
    ListView listview = new ListView("listRoles", new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getAllRoles()) {
        @Override 
        protected void populateItem(ListItem item) {
            final Urole modelObject = (Urole)item.getModelObject();
            CompoundPropertyModel model = new CompoundPropertyModel(modelObject);
            item.setModel(model);
                                                          item.add(new Label("id", model.bind("id")));
                                                                                     item.add(new Label("name", model.bind("name")));
                                                                                     item.add(new Label("description", model.bind("description")));
                                                                                     item.add(new Label("competence_level.name", model.bind("competence_level.name")));
                                                                                       item.add(new AjaxCheckBox("assign", new Model(getRolesMap().get(modelObject.getId()))) {
                     @Override 
                     public void  onUpdate  (AjaxRequestTarget target) {
                         
                     }           
                }.setEnabled(false));
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
