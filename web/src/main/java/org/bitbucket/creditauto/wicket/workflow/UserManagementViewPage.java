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

public class UserManagementViewPage extends UserViewPage {
    
    
    public UserManagementViewPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    public UserManagementViewPage(User user) {
        super( user );
        this.inInstance = getCreditautoSession().getInInstance();
        this.user = user;
        init();
    }
    
    public UserManagementViewPage(In_instance inInstance) {
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

    protected class MainPanel extends UserViewPage.MainPanel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends UserViewPage.MainPanel.MainForm {
        
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel(user
        );
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                CheckBox ddc1 = new CheckBox("isDefaultPassword_id", model.bind("isDefaultPassword"));
                form.add(new LineBorder("isDefaultPassword_line", new EditBorder("isDefaultPassword_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);
                ddc1.setEnabled(false);

                CheckBox ddc2 = new CheckBox("isDeleted_id", model.bind("isDeleted"));
                form.add(new LineBorder("isDeleted_line", new EditBorder("isDeleted_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);
                ddc2.setEnabled(false);

                TextField ddc3 = new TextField("deleted_comment_id", model.bind("deleted_comment"));
                form.add(new LineBorder("deleted_comment_line", new EditBorder("deleted_comment_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);
                ddc3.setEnabled(false);

                CheckBox ddc4 = new CheckBox("blocked_id", model.bind("blocked"));
                form.add(new LineBorder("blocked_line", new EditBorder("blocked_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                ddc4.setEnabled(false);

                TextField ddc5 = new TextField("blocked_comment_id", model.bind("blocked_comment"));
                form.add(new LineBorder("blocked_comment_line", new EditBorder("blocked_comment_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);
                ddc5.setEnabled(false);

                CheckBox ddc6 = new CheckBox("isBlockedDateSet_id", model.bind("isBlockedDateSet"));
                form.add(new LineBorder("isBlockedDateSet_line", new EditBorder("isBlockedDateSet_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                ddc6.setEnabled(false);

                TextField ddc7 = new TextField("blocked_start_id", model.bind("blocked_start"));
                form.add(new LineBorder("blocked_start_line", new EditBorder("blocked_start_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                ddc7.setEnabled(false);

                TextField ddc8 = new TextField("blocked_finish_id", model.bind("blocked_finish"));
                form.add(new LineBorder("blocked_finish_line", new EditBorder("blocked_finish_border", ddc8, true)));
                ddc8.setOutputMarkupId(true);
                ddc8.setEnabled(false);

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            setResponsePage(new UserViewPage(user));
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            setResponsePage(new UserShopsViewPage(user));
                    }           
                                    });
    }
        
    }
    }    
}
