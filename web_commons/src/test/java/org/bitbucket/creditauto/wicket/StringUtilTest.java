/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * StringUtil unit test.
 *
 * @author vko
 * @version $Revision$ $Date$
 */
public class StringUtilTest {

    @Test
    public void join() {
        assertEquals("3 + 2", StringUtil.join(" + ", "3", "2"));
        assertEquals("", StringUtil.join(" + "));
        assertEquals("", StringUtil.join(""));
        assertEquals("", StringUtil.join(null));
        assertEquals("144,123,Hello,world", StringUtil.join(",", "144", "123", "Hello", "world"));
        assertEquals("144,123 ,Hello ,world", StringUtil.join(",", "144", "123   ", "Hello  ", "world"));
        assertEquals("", StringUtil.join(",", null, null));
    }
}
