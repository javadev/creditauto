package org.bitbucket.creditauto.wicket;

import java.util.Set;
import javax.persistence.Entity;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.bitbucket.creditauto.entity.Dictionary_data;

/**
 * .
 */
public class EditBorder extends Border {

    /** OnChange. */
    public abstract static class OnChange {
        /**
         * onUpdate.
         *
         * @param target the target
         */
        public abstract void onUpdate(AjaxRequestTarget target);
    }

    private final class EditBorderBehavior extends AbstractBehavior {
        private final FormComponent fc;

        private EditBorderBehavior(FormComponent fc) {
            this.fc = fc;
        }

        @Override
        public void beforeRender(Component c) {
            super.beforeRender(c);
            IModel model = c.getInnermostModel();
            if (model != null
                    && model instanceof PropertyModel
                    && ((PropertyModel) model).getChainedModel() instanceof CompoundPropertyModel) {
                model = ((PropertyModel) model).getChainedModel();
            }
            if (model != null && model instanceof CompoundPropertyModel) {
                CompoundPropertyModel cpm = (CompoundPropertyModel) model;
                Class clazz = cpm.getObject().getClass();
                if (clazz.isAnnotationPresent(Entity.class) && !clazz.getName().endsWith("User")) {
                    fc.add(
                            getValidator(
                                    clazz,
                                    fc.getId()
                                            .replaceFirst("_id$", "")
                                            .replaceAll("(.*)\\.(.*)", "$2")));
                } else if (clazz.getName().endsWith("In_instance")
                        || clazz.getName().endsWith("User")) {
                    try {
                        String type =
                                clazz.getDeclaredField(
                                                fc.getId()
                                                        .replaceFirst("_id$", "")
                                                        .replaceAll("(.*?)\\.(.*)", "$1"))
                                        .getType()
                                        .getName();
                        if (type.endsWith("List")) {
                            String type2 =
                                    fc.getId()
                                            .replaceFirst("_id$", "")
                                            .replaceAll("(.*?)\\.(.*)", "$1");
                            type =
                                    clazz.getName().replaceAll("(.*\\.)(.*)", "$1")
                                            + type2.substring(0, 1).toUpperCase()
                                            + type2.substring(1, type2.length() - 1);
                            clazz = Class.forName(type);
                        }
                        if (clazz.getName().endsWith("In_instance")) {
                            clazz = Class.forName(type);
                        }
                        fc.add(
                                getValidator(
                                        clazz,
                                        fc.getId()
                                                .replaceFirst("_id$", "")
                                                .replaceAll("(.*)\\.(.*)", "$2")));
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        }
    }

    private final class JSR303Validator implements INullAcceptingValidator {
        private final Class clazz;
        private final String expression;

        private JSR303Validator(Class clazz, String expression) {
            this.clazz = clazz;
            this.expression = expression;
        }

        public void validate(IValidatable v) {
            Object value = v.getValue();
            if (value instanceof Dictionary_data) {
                value = ((Dictionary_data) value).getDkey();
            }
            Set<ConstraintViolation<?>> constraintViolations =
                    validator.validateValue(clazz, expression, value);
            if (!constraintViolations.isEmpty()) {
                String message = constraintViolations.iterator().next().getMessage();
                v.error(new ValidationError().setMessage(message));
            }
        }
    }

    private final class OnChangeBehavior extends AjaxFormComponentUpdatingBehavior {
        private OnChange change;

        private OnChangeBehavior(String event, OnChange change) {
            super(event);
            this.change = change;
        }

        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            getFormComponent().validate();
            target.addComponent(EditBorder.this);
            if (change != null) {
                change.onUpdate(target);
            }
        }

        @Override
        protected void onError(AjaxRequestTarget target, RuntimeException e) {
            target.addComponent(EditBorder.this);
        }
    }

    private Validator validator;
    public static final String BORDER = "Border";

    public EditBorder(String id) {
        super(id);
        add(new FeedbackPanel("message", new ContainerFeedbackMessageFilter(this)));
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public EditBorder(String id, FormComponent fc, boolean ajax) {
        this(id);
        add(fc, ajax, null);
    }

    public EditBorder(String id, FormComponent fc, boolean ajax, OnChange change) {
        this(id);
        add(fc, ajax, change);
    }

    public EditBorder(String id, FormComponent fc) {
        this(id, fc, false, null);
    }

    public EditBorder(String id, FormComponent fc, OnChange change) {
        this(id, fc, true, change);
    }

    public EditBorder(FormComponent fc) {
        this(fc.getId() + BORDER, fc, false, null);
    }

    public void add(FormComponent fc) {
        add(fc, false, null);
    }

    public void add(final FormComponent fc, boolean ajax, OnChange change) {
        super.add(fc);
        fc.add(new EditBorderBehavior(fc));
        add(
                new AbstractBehavior() {
                    @Override
                    public void onComponentTag(Component c, ComponentTag tag) {
                        if (!fc.isValid()) {
                            tag.put("class", "input errors");
                        }
                    }
                });
        if (ajax) {
            setOutputMarkupId(true);
            fc.add(new OnChangeBehavior("onchange", change));
        }
    }

    private INullAcceptingValidator getValidator(final Class clazz, final String expression) {
        return new JSR303Validator(clazz, expression);
    }
}
