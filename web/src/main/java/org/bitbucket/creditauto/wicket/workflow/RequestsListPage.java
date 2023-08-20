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

public class RequestsListPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;
    
    
    public RequestsListPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public RequestsListPage(In_instance inInstance) {
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
        return "Перечень всех заявок";
    }

    protected class MainPanel extends Panel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends Form {
        
        
                    private Date dateFrom = getCreditautoSession().getSearchData().getAllDossiers().getFrom();
                    private Date dateTo = getCreditautoSession().getSearchData().getAllDossiers().getTo();
                    
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel(
                    this
                    
        );
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                TextField ddc1 = new TextField("dateFrom_id", model.bind("dateFrom"));
                form.add(new LineBorder("dateFrom_line", new EditBorder("dateFrom_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);

                TextField ddc2 = new TextField("dateTo_id", model.bind("dateTo"));
                form.add(new LineBorder("dateTo_line", new EditBorder("dateTo_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);

    AjaxLink ddc3 = new AjaxLink("search_id") {
            @Override
             public void onClick(AjaxRequestTarget target)  {
                
                getCreditautoSession().getSearchData().getAllDossiers().setFrom(dateFrom);
                getCreditautoSession().getSearchData().getAllDossiers().setTo(dateTo);
                ((ListView)form.get("list_container:list")).setModelObject(org.bitbucket.creditauto.wicket.InstanceHelper.load(dateFrom, dateTo));
                target.addComponent(form);

                
            }
    };
             form.add(new LineBorder("search_line", ddc3));
        
//repeaters
WebMarkupContainer listViewContainer = new WebMarkupContainer("list_container");
    ListView listview = new ListView("list", org.bitbucket.creditauto.wicket.InstanceHelper.load(dateFrom, dateTo)
                    ) {
        @Override 
        protected void populateItem(ListItem item) {
            final In_instance modelObject = (In_instance)item.getModelObject();
            CompoundPropertyModel model = new CompoundPropertyModel(modelObject);
            item.setModel(model);
                                                          item.add(new Label("in_dossier.id", model.bind("in_dossier.id")));
                                                                                     item.add(new Label("in_dossier.externaldistributor.name", model.bind("in_dossier.externaldistributor.name")));
                                                                                     item.add(new Label("in_dossier.date_of_entering_dossier", model.bind("in_dossier.date_of_entering_dossier")));
                                                                                     item.add(new Label("in_dossier.product.name", model.bind("in_dossier.product.name")));
                                                                                     item.add(new Label("in_dossier.dict_status_of_dossier", new Model(InDossierStatus.getStatusNameByKey(modelObject.getIn_dossier().getDict_status_of_dossier()))));
                                                                                     item.add(new Label("in_person.last_name", model.bind("in_person.last_name")));
                                                                                     item.add(new Label("in_person.first_name", model.bind("in_person.first_name")));
                                                                                     item.add(new Label("in_dossier.amount_of_loan", model.bind("in_dossier.amount_of_loan")));
                                                                                     item.add(new Label("in_person.exp_id", model.bind("in_person.exp_id")));
                                                                                     item.add(new Label("in_dossier.dossier_exp_id", model.bind("in_dossier.dossier_exp_id")));
                                                                                     item.add(new Label("in_dossier.user_name_enters_dossier", model.bind("in_dossier.user_name_enters_dossier")));
                                                                                       item.add(new AjaxLink("viewlink") {
                     @Override 
                     public void onClick (AjaxRequestTarget target) {
                         setResponsePage(new CarDetailsInfoViewPage(org.bitbucket.creditauto.wicket.InstanceHelper.load(modelObject.getIn_dossier().getId())));
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
        
                    public Date getDateFrom() {
                         return dateFrom;
                    }
                    public void setDateFrom(Date dateFrom) {
                        this.dateFrom = dateFrom;
                    }
                    public Date getDateTo() {
                         return dateTo;
                    }
                    public void setDateTo(Date dateTo) {
                        this.dateTo = dateTo;
                    }
                    
        
    }
    }    
}
