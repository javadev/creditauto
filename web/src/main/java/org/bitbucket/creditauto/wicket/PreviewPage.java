/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.protocol.http.WebResponse;
import org.bitbucket.creditauto.LOG;
import org.bitbucket.creditauto.entity.In_document_store;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
public class PreviewPage extends TemplatePage {
    private String title;
    private In_document_store inDocumentStore;

    @SuppressWarnings("serial")
    private final class PhotoDynamicImageResource extends DynamicImageResource {
        @Override
        protected byte[] getImageData() {
            return inDocumentStore.getData() == null ? new byte[0] : inDocumentStore.getData();
        }

        @Override
        protected void setHeaders(WebResponse response) {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
        }
    }

    private final class DownloadLink extends Link {
        private final class OnClickResourceStreamWriter extends AbstractResourceStreamWriter {
            public void write(OutputStream output) {
                try {
                    output.write(inDocumentStore.getData());
                } catch (IOException ex) {
                    LOG.error(this, ex, ex.getMessage());
                }
            }

            @Override
            public String getContentType() {
                return inDocumentStore.getDoc_type();
            }
        }

        public DownloadLink(String id) {
            super(id);
        }

        @Override
        public void onClick() {
            IResourceStream resourceStream = new OnClickResourceStreamWriter();

            getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(
                    resourceStream).setFileName(inDocumentStore.getFilename()));
        }
    }

    public PreviewPage(String title, In_document_store localInDocumentStore, final WebPage backPage) {
        add(new FeedbackPanel("messages"));
        replace(new Label("title", "Personal finance::Просмотр " + title));
        add(new DownloadLink("downloadlink"));
        Image photoimage = new Image("photoimage", new PhotoDynamicImageResource());
        photoimage.add(new SimpleAttributeModifier("alt", title));
        add(photoimage.setVisible(localInDocumentStore.getData().length > 0));

        this.title = title;
        this.inDocumentStore = localInDocumentStore;
        add(new Link("back") {
            @Override
            public void onClick()  {
                setResponsePage(backPage);
            }
        });
    }
}
