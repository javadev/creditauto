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

public class UsersListPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public UsersListPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public UsersListPage(In_instance inInstance) {
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
        return "Поиск пользователей системы";
    }

    protected class MainPanel extends Panel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends Form {
        
        private SearchData searchData = new SearchData();
                    
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel(this
        );
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                TextField ddc1 = new TextField("searchData.allUsers.userLogin_id", model.bind("searchData.allUsers.userLogin"));
                form.add(new LineBorder("searchData.allUsers.userLogin_line", new EditBorder("searchData.allUsers.userLogin_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);

                TextField ddc2 = new TextField("searchData.allUsers.userTableNumber_id", model.bind("searchData.allUsers.userTableNumber"));
                form.add(new LineBorder("searchData.allUsers.userTableNumber_line", new EditBorder("searchData.allUsers.userTableNumber_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);

                TextField ddc3 = new TextField("searchData.allUsers.userName_id", model.bind("searchData.allUsers.userName"));
                form.add(new LineBorder("searchData.allUsers.userName_line", new EditBorder("searchData.allUsers.userName_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);

                TextField ddc4 = new TextField("searchData.allUsers.powerOfAttNumber_id", model.bind("searchData.allUsers.powerOfAttNumber"));
                form.add(new LineBorder("searchData.allUsers.powerOfAttNumber_line", new EditBorder("searchData.allUsers.powerOfAttNumber_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);

DateTextField ddc5 = new DateTextField("searchData.allUsers.powerOfAttStart_id", model.bind("searchData.allUsers.powerOfAttStart"), new StyleDateConverter("S-", true));
ddc5.add(new DatePicker().setShowOnFieldClick(true));
form.add(new LineBorder("searchData.allUsers.powerOfAttStart_line", new EditBorder("searchData.allUsers.powerOfAttStart_border", ddc5, true)));
ddc5.setOutputMarkupId(true);

DateTextField ddc6 = new DateTextField("searchData.allUsers.powerOfAttFinish_id", model.bind("searchData.allUsers.powerOfAttFinish"), new StyleDateConverter("S-", true));
ddc6.add(new DatePicker().setShowOnFieldClick(true));
form.add(new LineBorder("searchData.allUsers.powerOfAttFinish_line", new EditBorder("searchData.allUsers.powerOfAttFinish_border", ddc6, true)));
ddc6.setOutputMarkupId(true);

    AjaxLink ddc7 = new AjaxLink("search_id") {
            @Override
             public void onClick(AjaxRequestTarget target)  {
                getCreditautoSession().setSearchData(searchData);
                setResponsePage(new UsersListResultPage());
                
            }
    };
             form.add(new LineBorder("search_line", ddc7));
        
    }
        
                    public SearchData getSearchData() {
                         return searchData;
                    }
                    public void setSearchData(SearchData searchData) {
                        this.searchData = searchData;
                    }
                    
        
    }
    }    
}
