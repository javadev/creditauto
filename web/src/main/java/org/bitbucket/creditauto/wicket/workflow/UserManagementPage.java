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

public class UserManagementPage extends UserPage {
    
    
    public UserManagementPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    public UserManagementPage(User user) {
        super( user );
        this.inInstance = getCreditautoSession().getInInstance();
        this.user = user;
        init();
    }
    
    public UserManagementPage(In_instance inInstance) {
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

    protected class MainPanel extends UserPage.MainPanel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends UserPage.MainPanel.MainForm {
        
        
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel(user
        );
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                AjaxCheckBox ddc1 = new AjaxCheckBox("isDefaultPassword_id", model.bind("isDefaultPassword")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            
           
                        }                    
                                                        };
                form.add(new LineBorder("isDefaultPassword_line", new EditBorder("isDefaultPassword_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);

                AjaxCheckBox ddc2 = new AjaxCheckBox("isDeleted_id", model.bind("isDeleted")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            this.getForm().get("deleted_comment_line").setVisible(((Boolean) this.getConvertedInput()));
                       target.addComponent((LineBorder) form.get("deleted_comment_line"));
           
                        }                    
                                                        };
                form.add(new LineBorder("isDeleted_line", new EditBorder("isDeleted_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);

                TextField ddc3 = new TextField("deleted_comment_id", model.bind("deleted_comment"));
                form.add(new LineBorder("deleted_comment_line", new EditBorder("deleted_comment_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);
                form.get("deleted_comment_line").setOutputMarkupPlaceholderTag(true);
                form.get("deleted_comment_line").setVisible(user.getIsDeleted() != null ? user.getIsDeleted() : false);

                AjaxCheckBox ddc4 = new AjaxCheckBox("blocked_id", model.bind("blocked")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            this.getForm().get("blocked_comment_line").setVisible(((Boolean) this.getConvertedInput()));
                       target.addComponent((LineBorder) form.get("blocked_comment_line"));
           
                        }                    
                                                        };
                form.add(new LineBorder("blocked_line", new EditBorder("blocked_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);

                TextField ddc5 = new TextField("blocked_comment_id", model.bind("blocked_comment"));
                form.add(new LineBorder("blocked_comment_line", new EditBorder("blocked_comment_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);
                form.get("blocked_comment_line").setOutputMarkupPlaceholderTag(true);
                form.get("blocked_comment_line").setVisible(user.getBlocked() != null ? user.getBlocked() : false);

                AjaxCheckBox ddc6 = new AjaxCheckBox("isBlockedDateSet_id", model.bind("isBlockedDateSet")) {
                                            @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            this.getForm().get("blocked_start_line").setVisible(((Boolean) this.getConvertedInput()));
                       target.addComponent((LineBorder) form.get("blocked_start_line"));
                       this.getForm().get("blocked_finish_line").setVisible(((Boolean) this.getConvertedInput()));
                       target.addComponent((LineBorder) form.get("blocked_finish_line"));
           
                        }                    
                                                        };
                form.add(new LineBorder("isBlockedDateSet_line", new EditBorder("isBlockedDateSet_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);

                TextField ddc7 = new TextField("blocked_start_id", model.bind("blocked_start"));
                form.add(new LineBorder("blocked_start_line", new EditBorder("blocked_start_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                form.get("blocked_start_line").setOutputMarkupPlaceholderTag(true);
                form.get("blocked_start_line").setVisible(user.getIsBlockedDateSet() != null ? user.getIsBlockedDateSet() : false);

                TextField ddc8 = new TextField("blocked_finish_id", model.bind("blocked_finish"));
                form.add(new LineBorder("blocked_finish_line", new EditBorder("blocked_finish_border", ddc8, true)));
                ddc8.setOutputMarkupId(true);
                form.get("blocked_finish_line").setOutputMarkupPlaceholderTag(true);
                form.get("blocked_finish_line").setVisible(user.getIsBlockedDateSet() != null ? user.getIsBlockedDateSet() : false);

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            saveWorkflowPath("UserRolesPage");
         setResponsePage(new UserRolesPage(user));
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            user.setDeleted(user.getIsDeleted() ? new Date() : null);
            user.setPassword(user.getIsDefaultPassword() ? org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl.DEFAULT_PASSWORD : user.getPassword());
            user.setActivated(user.getIsDefaultPassword() ? null : user.getActivated());
            user.setBlocked_comment(user.getBlocked() ? user.getBlocked_comment() : null);
            user.setBlocked_start(user.getIsBlockedDateSet() ? user.getBlocked_start() : null);
            user.setBlocked_finish(user.getIsBlockedDateSet() ? user.getBlocked_finish() : null);
            new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().save(user);
            saveWorkflowPath("UserShopsPage");
            setResponsePage(new UserShopsPage(user));
                    }           
                                    });
    }
        
    }
    }    
}
