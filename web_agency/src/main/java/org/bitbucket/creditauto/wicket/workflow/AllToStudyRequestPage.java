package org.bitbucket.creditauto.wicket.workflow;


import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.bitbucket.creditauto.dictionary.facade.IDictionary;
import org.bitbucket.creditauto.dictionary.server.DictionaryServerImpl;
import org.bitbucket.creditauto.entity.In_instance;
import org.bitbucket.creditauto.wicket.StringUtil;
import org.bitbucket.creditauto.wicket.TemplatePage;

public class AllToStudyRequestPage extends TemplatePage {
    protected IDictionary dictionary;
    protected In_instance inInstance;

    public AllToStudyRequestPage() {
        super();
        this.inInstance = getCreditautoSession().getInInstance();
        init();
    }

    public AllToStudyRequestPage(In_instance inInstance) {
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
        return "Все заявки агенства к изучению";
    }

    protected class MainPanel extends Panel {
        public MainPanel(String id) {
            super(id);
        }

        protected class MainForm extends Form {

            public MainForm(String id) {
                super(id);

                CompoundPropertyModel model = new CompoundPropertyModel(inInstance);
                setModel(model);
                final Form form = this;
                form.setOutputMarkupId(true);
            }
        }
    }
}
