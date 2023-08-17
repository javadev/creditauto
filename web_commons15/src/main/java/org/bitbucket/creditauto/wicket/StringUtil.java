/*
 * $Id$
 *
 * Copyright (c) 2011, 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

/**.
 * @author javadev
 * @version $Revision$ $Date$
 */
public final class StringUtil {

    public static String join(String prefix, String ... strings) {
        if (strings.length == 0) {
            return "";
        }
        if (strings[0] == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        out.append(strings[0]);
        for (int index = 1; index < strings.length; index += 1) {
            if (strings[index] != null) {
                out.append(prefix).append(strings[index]);
            }
        }
        return out.toString().trim().replaceAll("\\s+", " ");
    }

    public static String join(String prefix, Object ... objects) {
        if (objects.length == 0) {
            return "";
        }
        if (objects[0] == null) {
            return "";
        }
        String[] strings = new String[objects.length];
        int index = 0;
        for (Object object : objects) {
            strings[index++] = object == null ? null : object.toString();
        }
        return join(prefix, strings);
    }

}
