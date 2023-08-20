/*
 * $Id$
 */
package org.bitbucket.creditauto.wicket.workflow;

import java.util.Date;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.bitbucket.creditauto.dictionary.facade.IDictionary;
import org.bitbucket.creditauto.dictionary.server.DictionaryServerImpl;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.wicket.EditBorder;
import org.bitbucket.creditauto.wicket.InDossierStatus;
import org.bitbucket.creditauto.wicket.LineBorder;
import org.bitbucket.creditauto.wicket.StringUtil;
import org.bitbucket.creditauto.wicket.TemplatePage;

public class AllRequestsPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;

    public AllRequestsPage() {
        super();
        this.inInstance = getCreditautoSession().getInInstance();
        init();
    }

    public AllRequestsPage(In_instance inInstance) {
        this.inInstance = inInstance;
        init();
    }

    private void init() {
        dictionary = new DictionaryServerImpl();
        add(createPanel("panel"));
        add(
                new Label(
                        "header",
                        StringUtil.join(
                                ": ",
                                inInstance.getIn_dossier().getId(),
                                StringUtil.join(
                                        " ",
                                        inInstance.getIn_person().getLast_name_ru(),
                                        inInstance.getIn_person().getFirst_name_ru(),
                                        inInstance.getIn_person().getPatronymic_name_ru()))));
    }

    public Panel createPanel(String panelId) {
        MainPanel panel = new MainPanel(panelId);
        panel.add(panel.new MainForm("form"));
        return panel;
    }

    public static String getTitle() {
        return "Все заявки агенства";
    }

    protected class MainPanel extends Panel {
        public MainPanel(String id) {
            super(id);
        }

        protected class MainForm extends Form {

            private Date dateFrom =
                    getCreditautoSession().getSearchData().getAllDossiers().getFrom();
            private Date dateTo = getCreditautoSession().getSearchData().getAllDossiers().getTo();

            public MainForm(String id) {
                super(id);

                CompoundPropertyModel model = new CompoundPropertyModel(this);

                setModel(model);
                final Form form = this;
                form.setOutputMarkupId(true);

                TextField ddc1 = new TextField("dateFrom_id", model.bind("dateFrom"));
                form.add(
                        new LineBorder(
                                "dateFrom_line", new EditBorder("dateFrom_border", ddc1, true)));
                ddc1.setOutputMarkupId(true);

                TextField ddc2 = new TextField("dateTo_id", model.bind("dateTo"));
                form.add(
                        new LineBorder("dateTo_line", new EditBorder("dateTo_border", ddc2, true)));
                ddc2.setOutputMarkupId(true);

                AjaxLink ddc3 =
                        new AjaxLink("search_id") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {

                                getCreditautoSession()
                                        .getSearchData()
                                        .getAllDossiers()
                                        .setFrom(dateFrom);
                                getCreditautoSession()
                                        .getSearchData()
                                        .getAllDossiers()
                                        .setTo(dateTo);
                                ((ListView) form.get("list_container:list"))
                                        .setModelObject(
                                                org.bitbucket.creditauto.wicket.InstanceHelper.load(
                                                        dateFrom, dateTo));
                                target.addComponent(form);
                            }
                        };
                form.add(new LineBorder("search_line", ddc3));

                // repeaters
                WebMarkupContainer listViewContainer = new WebMarkupContainer("list_container");
                ListView listview =
                        new ListView(
                                "list",
                                org.bitbucket.creditauto.wicket.InstanceHelper.load(
                                        dateFrom, dateTo)) {
                            @Override
                            protected void populateItem(ListItem item) {
                                final In_instance modelObject = (In_instance) item.getModelObject();
                                CompoundPropertyModel model =
                                        new CompoundPropertyModel(modelObject);
                                item.setModel(model);
                                item.add(new Label("in_dossier.id", model.bind("in_dossier.id")));
                                item.add(
                                        new Label(
                                                "in_dossier.externaldistributor.name",
                                                model.bind("in_dossier.externaldistributor.name")));
                                item.add(
                                        new Label(
                                                "in_dossier.date_of_coming_dossier",
                                                model.bind("in_dossier.date_of_coming_dossier")));
                                item.add(
                                        new Label(
                                                "in_dossier.product.name",
                                                model.bind("in_dossier.product.name")));
                                item.add(
                                        new Label(
                                                "in_dossier.dict_status_of_dossier",
                                                new Model(
                                                        InDossierStatus.getStatusNameByKey(
                                                                modelObject
                                                                        .getIn_dossier()
                                                                        .getDict_status_of_dossier()))));
                                item.add(
                                        new Label(
                                                "in_person.last_name",
                                                model.bind("in_person.last_name")));
                                item.add(
                                        new Label(
                                                "in_person.first_name",
                                                model.bind("in_person.first_name")));
                                item.add(
                                        new Label(
                                                "in_dossier.amount_of_loan",
                                                model.bind("in_dossier.amount_of_loan")));
                                item.add(
                                        new Label(
                                                "in_person.exp_id",
                                                model.bind("in_person.exp_id")));
                                item.add(
                                        new Label(
                                                "in_dossier.dossier_exp_id",
                                                model.bind("in_dossier.dossier_exp_id")));
                                item.add(
                                        new Label(
                                                "in_dossier.user_name_enters_dossier",
                                                model.bind("in_dossier.user_name_enters_dossier")));
                            }

                            @Override
                            protected ListItem newItem(int index) {
                                return new OddEvenListItem(
                                        index, getListItemModel(getModel(), index));
                            }
                        };
                listViewContainer.setOutputMarkupId(true);
                form.add(listViewContainer.add(listview));

                // end of repeaters
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
