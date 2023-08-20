package org.bitbucket.creditauto.wicket;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.convert.IConverter;
import org.bitbucket.creditauto.entity.Dictionary_data;

public class DictionaryTextField extends TextField {
    private List<Dictionary_data> dictionary;

    public DictionaryTextField(String id, List<Dictionary_data> dictionary, ChoiceRenderer choiceRenderer) {
        super(id);
        this.dictionary = dictionary;
    }

    @Override
    public IConverter getConverter(Class type) {
        return new IConverter() {
            public Object convertToObject(String arg0, Locale arg1) {
                throw new UnsupportedOperationException("converter is readonly.");
            }
            public String convertToString(Object object, Locale locale) {
                return ((Dictionary_data) object).getDvalue();
            }
        };
    }

    public List<Dictionary_data> getChoices() {
        return dictionary;
    }
}
