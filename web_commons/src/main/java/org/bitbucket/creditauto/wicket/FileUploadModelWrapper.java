/*
 * $Id$
 */
package org.bitbucket.creditauto.wicket;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.bitbucket.creditauto.entity.In_document_store;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class FileUploadModelWrapper implements IWrapModel {

    private IModel wrappedModel;

    public FileUploadModelWrapper(IModel wrappedModel) {
        this.wrappedModel = wrappedModel;
    }

    public IModel getWrappedModel() {
        return wrappedModel;
    }

    public Object getObject() {
        return wrappedModel.getObject();
    }

    public void setObject(Object object) {
        if (object != null) {
            ((In_document_store) wrappedModel.getObject())
                    .setData(((FileUpload) object).getBytes());
            ((In_document_store) wrappedModel.getObject())
                    .setFilename(((FileUpload) object).getClientFileName());
            ((In_document_store) wrappedModel.getObject())
                    .setDoc_type(((FileUpload) object).getContentType());
        }
    }

    public void detach() {
        wrappedModel.detach();
    }
}
