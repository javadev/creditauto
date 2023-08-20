package org.bitbucket.creditauto.printer.server;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**.
 */
public class JoinDirective extends Directive {
    public String getName() {
        return "join";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node) 
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        //setting default params
        java.util.List<String> stringValues = new java.util.ArrayList<String>();

        //reading params
        for (int index = 0; index < node.jjtGetNumChildren(); index += 1) {
            if (node.jjtGetChild(index) != null) {
                stringValues.add(String.valueOf(node.jjtGetChild(index).value(context)));
            }
        }
        //truncate and write result to writer
        writer.write(join(stringValues));
        return true;

    }

    public String join(java.util.List<String> stringValues) {
        if (stringValues.isEmpty()) {
            return null;
        }
        return org.bitbucket.creditauto.wicket.StringUtil.join(", ", stringValues.toArray(new String[0]));
    }
}
