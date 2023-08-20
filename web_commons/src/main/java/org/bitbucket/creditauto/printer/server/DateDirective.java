package org.bitbucket.creditauto.printer.server;

import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

public class DateDirective extends Directive {
    private java.text.SimpleDateFormat defaultFormat =
            new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US);
    private java.text.SimpleDateFormat format =
            new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.US);

    public String getName() {
        return "date";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException,
                    MethodInvocationException {

        // setting default params
        String dateValue = null;

        // reading params
        if (node.jjtGetChild(0) != null) {
            dateValue = String.valueOf(node.jjtGetChild(0).value(context));
        }

        // truncate and write result to writer
        writer.write(date(dateValue));
        return true;
    }

    public String date(String dateValue) {
        if (dateValue == null) {
            return null;
        }
        try {
            return format.format(defaultFormat.parse(dateValue));
        } catch (Exception ex) {
            return "";
        }
    }
}
