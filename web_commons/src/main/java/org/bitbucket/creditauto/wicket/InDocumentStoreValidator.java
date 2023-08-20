/*
 * $Id$
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.bitbucket.creditauto.entity.In_document_store;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class InDocumentStoreValidator extends AbstractFormValidator {
    private static final long serialVersionUID = 1L;
    /** form components to be validated. */
    private final FormComponent[] components;

    private final In_document_store inDocumentStore;

    /**
     * Constructor.
     *
     * @param f1 the component1
     * @param inDocumentStore the In_document_store
     */
    public InDocumentStoreValidator(FormComponent f1, In_document_store inDocumentStore) {
        if (f1 == null) {
            throw new IllegalArgumentException("FormComponent1 cannot be null");
        }
        components = new FormComponent[] {f1};
        this.inDocumentStore = inDocumentStore;
    }

    /**
     * getDependentFormComponents.
     *
     * @return the components
     */
    public FormComponent[] getDependentFormComponents() {
        return components.clone();
    }

    /**
     * Validates the form.
     *
     * @param form the form
     */
    public void validate(Form form) {
        final FileUploadField component1 = (FileUploadField) components[0];
        if (inDocumentStore.getIs_not_required() != null && inDocumentStore.getIs_not_required()) {
            inDocumentStore.setData(null);
        } else if (component1.getFileUpload() != null) {
            inDocumentStore.setData(component1.getFileUpload().getBytes());
        }
        boolean isValid =
                inDocumentStore.getData() != null
                        || (inDocumentStore.getIs_not_required() != null
                                && inDocumentStore.getIs_not_required());
        if (!isValid) {
            error(component1);
        }
    }
}
