package org.bitbucket.creditauto.printer.server;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import org.bitbucket.creditauto.dictionary.facade.IDictionary;
import org.bitbucket.creditauto.dictionary.server.DictionaryServerImpl;

/**.
 */
public class DictionaryExp2Directive extends Directive {

    private IDictionary dictionary = new DictionaryServerImpl();
    public String getName() {
        return "dictionaryExp2";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node) 
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        //setting default params
        String dictionaryName = null;
        String dictionaryKey = null;
        String dictionaryExp = null;
        String dictionaryExp2 = null;

        //reading params
        if (node.jjtGetChild(0) != null) {
            dictionaryName = String.valueOf(node.jjtGetChild(0).value(context));
        }
        if (node.jjtGetChild(1) != null) {
            dictionaryKey = String.valueOf(node.jjtGetChild(1).value(context));
        }
        if (node.jjtGetChild(2) != null) {
            dictionaryExp = String.valueOf(node.jjtGetChild(2).value(context));
        }
        if (node.jjtGetChild(3) != null) {
            dictionaryExp2 = String.valueOf(node.jjtGetChild(3).value(context));
        }

        //truncate and write result to writer
        writer.write(dictionaryExp2(dictionaryName, dictionaryExp, dictionaryExp2, dictionaryKey));
        return true;

    }

    private String dictionaryExp2(String dictionaryName, String dictionaryExp, String dictionaryExp2, String dictionaryKey) {
        if (dictionaryName == null || dictionaryKey == null) {
            return null;
        }
        return dictionary.getDictionaryItem(dictionaryName, dictionaryExp, dictionaryExp2, dictionaryKey, new java.util.Date(), IDictionary.LANGUAGE_UK, IDictionary.LANGUAGE_RU);
    }
}
