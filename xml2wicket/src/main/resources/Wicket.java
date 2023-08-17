/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package $package;

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

public class $classname extends #if ($extends.isEmpty())TemplatePage#else$extends#end {
#if ($extends.isEmpty() || $extends.endsWith("Panel"))
    protected IDictionary dictionary;
#end
#if(!$extends.endsWith("Panel"))
#if($extends.isEmpty())
    protected In_instance inInstance;
#end
#else
    private final  #set( $pp = ${xmltag.getText("compoundPropertyModel")} ) $pp.split(" ")[0] modelObject;
#end
    
    $pageInit
#if (!$extends.endsWith("Panel")) 
    public $classname() {
        super();
        #if ($xmltag.hasTag("defaultConstructor"))
        $xmltag.getText("defaultConstructor")
        #end
        this.inInstance = getCreditautoSession().getInInstance();
        init();       
    }
    
#if ($xmltag.hasTag("constructor"))
    public $classname($xmltag.getText("constructor/parameters")) {
        super(#if(!$extends.isEmpty())#set( $pp = ${xmltag.getText("constructor/parameters")} ) $pp.split(" ")[1] #end);
        this.inInstance = getCreditautoSession().getInInstance();
        $xmltag.getText("constructor/body")
        init();
    }
#end
    
    public $classname(In_instance inInstance) {
        #if (!$extends.isEmpty())super(inInstance);
#end
        this.inInstance = inInstance;
        init();
    }
#end
#if ($extends.endsWith("Panel"))
        public $classname(String id, IModel model) {
            super(id, model);
            this.modelObject = ($pp.split(" ")[0])this.getDefaultModelObject();
            #if ($xmltag.hasTag("constructor"))
            $xmltag.getText("constructor/body")
            #end
            init();
        }
#end
    private void init() {
#if ($extends.isEmpty() || $extends.endsWith("Panel"))
        dictionary = new DictionaryServerImpl();
#end
#if (!$extends.endsWith("Panel"))
#if ($xmltag.hasAttribute("combines"))
        List<ITab> tabs = new ArrayList<ITab>();
#set($curIndex=${xmltag.getAttribute("combines").split(",").size()})
#foreach ($tab in ${xmltag.getAttribute("combines").split(",")})
        tabs.add(new AbstractTabWithForm(new Model<String>(${tab}.getTitle())) {
            private Form form;
            @Override
            public Panel getPanel(String panelId) {
#if (${tab} == ${classname})#evaluate('#set($curIndex=${foreach.count})')#end
                Panel panel =#if (${tab} == ${classname}) createPanel(panelId);
#else new ${tab}(inInstance).createPanel(panelId);
#end
                form = (Form) panel.get("form");
                return panel;
            }
            @Override
            public Form getForm() {
                return form;
            }
            @Override
            public boolean isVisible() {
#if (${tab.startsWith("Partner")})
                String maritasStatus = inInstance.getIn_person().getDictionary_marital_status();
                if (maritasStatus == null || (!"1".equals(maritasStatus) && !"5".equals(maritasStatus)) ) {
                    return false;
                }
#elseif (${tab.startsWith("Guarant")} && ${tab} != ${classname})
                String gender = inInstance.getIn_guarantor().getDictionary_gender();
                if (gender == null) {
                    return false;
                }
#end
                return true;
            }
        });
#end
        AjaxTabbedPanel ajaxTabbedPanel = new FormTabbedPanel("panel", tabs);
        ajaxTabbedPanel.setSelectedTab($curIndex - 1);
#if ($extends.isEmpty())
        add(ajaxTabbedPanel);
        add(new Label("header", #if (!$extends.endsWith("Panel"))StringUtil.join(": ", inInstance.getIn_dossier().getId(),
                StringUtil.join(" ", inInstance.getIn_person().getLast_name_ru(), inInstance.getIn_person().getFirst_name_ru(),
                    inInstance.getIn_person().getPatronymic_name_ru()))#else""#end));
#elseif (!$extends.isEmpty() && !$extends.endsWith("Panel"))
        replace(ajaxTabbedPanel);
#end
#else
#if ($extends.isEmpty())
        add(createPanel("panel"));
        add(new Label("header", #if (!$extends.endsWith("Panel"))StringUtil.join(": ", inInstance.getIn_dossier().getId(),
                StringUtil.join(" ", inInstance.getIn_person().getLast_name_ru(), inInstance.getIn_person().getFirst_name_ru(),
                    inInstance.getIn_person().getPatronymic_name_ru()))#else""#end));
#elseif (!$extends.isEmpty() && !$extends.endsWith("Panel"))
        replace(createPanel("panel"));
#end
#end
    }

    public Panel createPanel(String panelId) {
        MainPanel panel = new MainPanel(panelId);
        panel.add(panel.new MainForm("form"));
        return panel;
    }

    public static String getTitle() {
        return "$xmltag.getText("pageTitle")";
    }

    protected class MainPanel extends #if ($extends.isEmpty())Panel#else${extends}.MainPanel#end {
        public MainPanel(String id) {
            super(id);
#if ($xmltag.hasTag("modalWindow"))
            $xmltag.getText("modalWindow")
#end
        }
    protected class MainForm extends #if ($extends.isEmpty())Form#else${extends}.MainPanel.MainForm#end {
        
        $formInit
        public MainForm(String id) {
            super(id);
            
            CompoundPropertyModel model = new CompoundPropertyModel(#if (${compoundPropertyModel.isEmpty()}) inInstance#else${compoundPropertyModel}#end);
            setModel(model);
#end
#if ($extends.endsWith("Panel"))
    CompoundPropertyModel model = new CompoundPropertyModel(modelObject); 
#end
            final #if (!$extends.endsWith("Panel"))Form#else Panel#end form = this;
            form.setOutputMarkupId(true);
            
#foreach ($var in $variables)
#if ($var.get("type") == "AjaxLink" || $var.get("type") == "AjaxSubmitLink")
    ${var.get("type")} ddc${foreach.count} = new ${var.get("type")}("${var.get("name")}_id") {
            @Override
            #if ($var.get("type") == "AjaxLink") public void onClick(AjaxRequestTarget target) #else protected void onSubmit(AjaxRequestTarget target, Form form) #end {
                ${var.get("onSubmit")}
            }
    };
    #if ($var.get("defaultFormProcess") == "false")
         ddc${foreach.count}.setDefaultFormProcessing(false);
    #end
         form.add(new LineBorder("${var.get("name")}_line", ddc${foreach.count}));
    #if ($var.get("readonly") == "true")
         ddc${foreach.count}.setEnabled(false);
    #end
    #if ($var.get("visible") != "")
                form.get("${var.get("name")}_line").setOutputMarkupPlaceholderTag(true);
                form.get("${var.get("name")}_line").setVisible(#if (!$var.get("visible").contains("null"))${var.get("visible")} == null ? false : #end${var.get("visible")});
    #end
#elseif ($var.get("type") == "Link")
    ${var.get("type")} ddc${foreach.count} = new ${var.get("type")}("${var.get("name")}_id") {
            @Override
            public void onClick() {
                ${var.get("onSubmit")}
            }
    };
        form.add(new LineBorder("${var.get("name")}_line", ddc${foreach.count}));
#if ($var.get("readonly") == "true")
                ddc${foreach.count}.setEnabled(false);
#end
#elseif ($var.get("type") == "DateTextField")
$var.get("type") ddc${foreach.count} = new $var.get("type")("${var.get("name")}_id", model.bind("${var.get("name")}"), new StyleDateConverter("S-", true));
ddc${foreach.count}.add(new DatePicker().setShowOnFieldClick(true));
form.add(new LineBorder("${var.get("name")}_line", new EditBorder("${var.get("name")}_border", ddc${foreach.count}, true)));
ddc${foreach.count}.setOutputMarkupId(true);
#elseif ($var.get("type").endsWith("Panel"))
form.add(new $var.get("type")("$var.get("name")", $var.get("model")));
#if ($var.get("afterInit0") != "")
    $var.get("afterInit0");
#end
#else
                ${var.get("type")} ddc${foreach.count} = new ${var.get("type")}("${var.get("name")}_id"#if ($var.get("type") != "DropDownChoice" && $var.get("type") != "DictionaryTextField" && $var.get("type") != "FileUploadField" && ${var.get("model")} == ""), model.bind("${var.get("name")}")#end${var.get("model")})#{if}(!($var.get("isEnabled").isEmpty()) || !($var.get("onSelectionChanged").isEmpty()) || !($var.get("onUpdate").isEmpty())) {
                    #if (!($var.get("onUpdate").isEmpty()))
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            ${var.get("onUpdate")}
                        }#end
                    
                    #if (!($var.get("isEnabled").isEmpty()))
                        @Override
                        public boolean isEnabled() {
                            ${var.get("isEnabled")}
                        }#end
                    #if (!($var.get("onSelectionChanged").isEmpty()))
                        protected boolean wantOnSelectionChangedNotifications() {
                            return true;
                        }
                        protected void onSelectionChanged(Object newSelection) {
                            ${var.get("onSelectionChanged")}
                        }#end
                }#{end};
#if ($var.get("type") == "DropDownChoice" && !$var.get("model").contains("model."))
                ddc${foreach.count}.setModel(new DictionaryModelWrapper(model.bind("${var.get("name")}"), ddc${foreach.count}));
                ddc${foreach.count}.setNullValid(true);
#elseif ($var.get("type") == "DictionaryTextField")
                ddc${foreach.count}.setModel(new DictionaryModelWrapper(model.bind("${var.get("name")}"), ddc${foreach.count}));
#elseif ($var.get("type") == "FileUploadField")
                ddc${foreach.count}.setModel(new FileUploadModelWrapper(model.bind("${var.get("name").replace(".data", "")}")));
#end
#if (!$var.get("ajaxOnUpdate").isEmpty())
                EditBorder.OnChange change${foreach.count} = new EditBorder.OnChange() {
                    public void onUpdate(AjaxRequestTarget target) {
${var.get("ajaxOnUpdate")}
                    }
                };
#end
                form.add(new LineBorder("${var.get("name")}_line", new EditBorder("${var.get("name")}_border", ddc${foreach.count},#if ($var.get("type") == "FileUploadField") false#else true#end#if (!$var.get("ajaxOnUpdate").isEmpty()), change${foreach.count}#end)));
                ddc${foreach.count}.setOutputMarkupId(true);
#if ($var.get("afterInit0") != "")
                $var.get("afterInit0");
#end
#if ($var.get("afterInit1") != "")
                $var.get("afterInit1");
#end
#if ($var.get("readonly") == "true")
                ddc${foreach.count}.setEnabled(false);
#end
#if ($var.get("visible") != "")
                form.get("${var.get("name")}_line").setOutputMarkupPlaceholderTag(true);
                form.get("${var.get("name")}_line").setVisible(#if (!$var.get("visible").contains("null"))${var.get("visible")} == null ? false : #end${var.get("visible")});
#end
#if (!$var.get("ajaxOnEvent").isEmpty())
            ddc${foreach.count}.add(new AjaxEventBehavior("onchange") {
                protected void onEvent(AjaxRequestTarget target) {
${var.get("ajaxOnEvent")}
                }
            });
#end
#end

#end
#foreach ($button in $buttons)
                form.addOrReplace(new ${button.get("type")}("${button.get("name")}") {
                    @Override 
                    #if ($button.get("type") == "Link" || $button.get("type") == "SubmitLink")public void #if ($button.get("type") == "Link")onClick()#end #if ($button.get("type") == "SubmitLink")onSubmit()#end {
            ${button.get("onSubmit")}
                    }           
                    #elseif ($button.get("type") == "AjaxSubmitLink") protected void onSubmit(AjaxRequestTarget target, Form form) {
                ${button.get("onSubmit")}
                target.addComponent(form);
                    }
                    @Override
                    protected void onError(AjaxRequestTarget target, Form form) {
                        super.onError(target, form);
                        target.addComponent(form);
                    }#end
                });
#if ($button.get("afterInit0") != "")
                ${button.get("afterInit0")};
#end
#end
#if (!$repeaters.isEmpty())
//repeaters
#foreach ($rep in $repeaters)
WebMarkupContainer listViewContainer = new WebMarkupContainer("${rep.getExtra().get("name")}_container");
    ListView listview = new ListView("${rep.getExtra().get("name")}", ${rep.getExtra().get("model")}) {
        @Override 
        protected void populateItem(ListItem item) {
            final ${rep.getExtra().get("modelClass")} modelObject = (${rep.getExtra().get("modelClass")})item.getModelObject();
            CompoundPropertyModel model = new CompoundPropertyModel(modelObject);
            item.setModel(model);
            #foreach ($data in ${rep.getData()})
              #if ($data.get("type") == "links")
                #foreach ($link in $rep.getLinks())
                  item.add(new $link.get("type")("${link.get("name")}"#if($link.get("model") != ""), new Model($link.get("model"))#end) {
                     @Override 
                     public void #if ($link.get("type") == "AjaxLink")onClick#elseif ($link.get("type") == "AjaxCheckBox") onUpdate #end (#if ($link.get("type").startsWith("Ajax"))AjaxRequestTarget target#end) {
                         $link.get("onClick")
                     }           
                }#if($link.get("readonly") == "true").setEnabled(false)#end);
                 #end
              #elseif ($data.get("type") == "Link" || $data.get("type") == "AjaxLink")
                 item.add(new $data.get("type")("link") {
                     @Override 
                     public void onClick(#if ($data.get("type") == "AjaxLink")AjaxRequestTarget target#end) {
                         $data.get("onClick")
                     }           
                }.add(new Label("${data.get("name")}", model.bind("${data.get("name")}"))));
              #elseif ($data.get("type") == "CheckBox")
                 item.add(new CheckBox("${data.get("name")}", model.bind("${data.get("name")}")).setEnabled(false)); 
              #else
               #if ($data.get("model").isEmpty())
                 item.add(new Label("${data.get("name")}", model.bind("${data.get("name")}")));
               #elseif (!$data.get("model").startsWith("new "))
                 item.add(new Label("${data.get("name")}", new Model($data.get("model"))));
               #else
                 item.add(new Label("${data.get("name")}",$data.get("model")));
               #end
            #end
            #end
        }
        @Override
        protected ListItem newItem(int index) {
            return new OddEvenListItem(index, getListItemModel(getModel(), index));
        }
    };
    listViewContainer.setOutputMarkupId(true);
    form.add(listViewContainer.add(listview));

#end
//end of repeaters
#end
    }
        $formMethods
#if (!$extends.endsWith("Panel")) 
    }
    }#end
    $pageMethods
}
