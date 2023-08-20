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

public class CredittypeDetailInfoPage extends CredittypeSelectionPage {
    
    
    public CredittypeDetailInfoPage() {
        super();
                this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
    
    public CredittypeDetailInfoPage(In_instance inInstance) {
        super(inInstance);
        this.inInstance = inInstance;
        init();
    }
    private void init() {
        List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTabWithForm(new Model<String>(CarDetailsInfoPage.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
                Panel panel = new CarDetailsInfoPage(inInstance).createPanel(panelId);
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
        tabs.add(new AbstractTabWithForm(new Model<String>(CredittypeDetailInfoPage.getTitle())) {
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
        ajaxTabbedPanel.setSelectedTab(2 - 1);
        replace(ajaxTabbedPanel);
    }

    public Panel createPanel(String panelId) {
        MainPanel panel = new MainPanel(panelId);
        panel.add(panel.new MainForm("form"));
        return panel;
    }

    public static String getTitle() {
        return "Выбор типа кредита";
    }

    protected class MainPanel extends CredittypeSelectionPage.MainPanel {
        public MainPanel(String id) {
            super(id);
        }
    protected class MainForm extends CredittypeSelectionPage.MainPanel.MainForm {
        
        
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel( inInstance);
            setModel(model);
            final Form form = this;
            form.setOutputMarkupId(true);
            
                TextField ddc1 = new TextField("in_dossier.credittype.name_id", model.bind("in_dossier.credittype.name"));
                form.add(new LineBorder("in_dossier.credittype.name_line", new EditBorder("in_dossier.credittype.name_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);
                ddc1.setEnabled(false);

                TextField ddc2 = new TextField("in_dossier.date_of_entering_dossier_id", model.bind("in_dossier.date_of_entering_dossier"));
                form.add(new LineBorder("in_dossier.date_of_entering_dossier_line", new EditBorder("in_dossier.date_of_entering_dossier_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);
                ddc2.setEnabled(false);

                TextField ddc3 = new TextField("in_dossier.date_of_end_of_the_deal_id", model.bind("in_dossier.date_of_end_of_the_deal"));
                form.add(new LineBorder("in_dossier.date_of_end_of_the_deal_line", new EditBorder("in_dossier.date_of_end_of_the_deal_border", ddc3, true)));
                ddc3.setOutputMarkupId(true);
                ddc3.setEnabled(false);

                TextField ddc4 = new TextField("date_of_print_contract_sign_id", model.bind("date_of_print_contract_sign"));
                form.add(new LineBorder("date_of_print_contract_sign_line", new EditBorder("date_of_print_contract_sign_border", ddc4, true)));
                ddc4.setOutputMarkupId(true);
                ddc4.setEnabled(false);

                TextField ddc5 = new TextField("in_dossier.duration_id", model.bind("in_dossier.duration"));
                form.add(new LineBorder("in_dossier.duration_line", new EditBorder("in_dossier.duration_border", ddc5, true)));
                ddc5.setOutputMarkupId(true);
                ddc5.setEnabled(false);

                TextField ddc6 = new TextField("in_dossier.due_date_id", model.bind("in_dossier.due_date"));
                form.add(new LineBorder("in_dossier.due_date_line", new EditBorder("in_dossier.due_date_border", ddc6, true)));
                ddc6.setOutputMarkupId(true);
                ddc6.setEnabled(false);

                TextField ddc7 = new TextField("in_dossier.annuity_sum_id", model.bind("in_dossier.annuity_sum"));
                form.add(new LineBorder("in_dossier.annuity_sum_line", new EditBorder("in_dossier.annuity_sum_border", ddc7, true)));
                ddc7.setOutputMarkupId(true);
                ddc7.setEnabled(false);

                TextField ddc8 = new TextField("in_dossier.total_monthly_payment_id", model.bind("in_dossier.total_monthly_payment"));
                form.add(new LineBorder("in_dossier.total_monthly_payment_line", new EditBorder("in_dossier.total_monthly_payment_border", ddc8, true)));
                ddc8.setOutputMarkupId(true);
                ddc8.setEnabled(false);

                TextField ddc9 = new TextField("in_dossier.rate_id", model.bind("in_dossier.rate"));
                form.add(new LineBorder("in_dossier.rate_line", new EditBorder("in_dossier.rate_border", ddc9, true)));
                ddc9.setOutputMarkupId(true);
                ddc9.setEnabled(false);

                TextField ddc10 = new TextField("in_dossier.opening_fee_id", model.bind("in_dossier.opening_fee"));
                form.add(new LineBorder("in_dossier.opening_fee_line", new EditBorder("in_dossier.opening_fee_border", ddc10, true)));
                ddc10.setOutputMarkupId(true);
                ddc10.setEnabled(false);

                TextField ddc11 = new TextField("in_dossier.monthly_fee_id", model.bind("in_dossier.monthly_fee"));
                form.add(new LineBorder("in_dossier.monthly_fee_line", new EditBorder("in_dossier.monthly_fee_border", ddc11, true)));
                ddc11.setOutputMarkupId(true);
                ddc11.setEnabled(false);

                TextField ddc12 = new TextField("in_dossier.monthly_fee_payment_id", model.bind("in_dossier.monthly_fee_payment"));
                form.add(new LineBorder("in_dossier.monthly_fee_payment_line", new EditBorder("in_dossier.monthly_fee_payment_border", ddc12, true)));
                ddc12.setOutputMarkupId(true);
                ddc12.setEnabled(false);

                TextField ddc13 = new TextField("in_dossier.effective_rate_id", model.bind("in_dossier.effective_rate"));
                form.add(new LineBorder("in_dossier.effective_rate_line", new EditBorder("in_dossier.effective_rate_border", ddc13, true)));
                ddc13.setOutputMarkupId(true);
                ddc13.setEnabled(false);

                TextField ddc14 = new TextField("in_dossier.total_interest_id", model.bind("in_dossier.total_interest"));
                form.add(new LineBorder("in_dossier.total_interest_line", new EditBorder("in_dossier.total_interest_border", ddc14, true)));
                ddc14.setOutputMarkupId(true);
                ddc14.setEnabled(false);

                TextField ddc15 = new TextField("in_dossier.total_loan_cost_id", model.bind("in_dossier.total_loan_cost"));
                form.add(new LineBorder("in_dossier.total_loan_cost_line", new EditBorder("in_dossier.total_loan_cost_border", ddc15, true)));
                ddc15.setOutputMarkupId(true);
                ddc15.setEnabled(false);

                form.addOrReplace(new Link("back") {
                    @Override 
                    public void onClick()  {
            setResponsePage(new CredittypeSelectionPage());
                    }           
                                    });
                form.addOrReplace(new SubmitLink("next") {
                    @Override 
                    public void  onSubmit() {
            saveWorkflowPath("CredittypeDetailInfoPage");
            setResponsePage(new CredittypeDetailInfoPage());
                    }           
                                    });
    }
        
    }
    }    
}
