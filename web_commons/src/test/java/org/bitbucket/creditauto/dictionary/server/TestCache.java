/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.dictionary.server;

import org.junit.Assert;
import org.junit.Test;

public class TestCache {

   @Test
   public void testSimpleCacheSizeLimit() throws Exception {
      Cache<String, String> cache = new Cache<String, String>(200, 500, 5);

      cache.put("1", "1");
      cache.put("2", "2");
      cache.put("3", "3");
      cache.put("4", "4");
      cache.put("5", "5");
      cache.put("6", "6");

      Assert.assertNull(cache.get("1"));  // just pushed off end
      Assert.assertNotNull(cache.get("2"));
      Assert.assertNotNull(cache.get("3"));
      Assert.assertNotNull(cache.get("4"));
      Assert.assertNotNull(cache.get("5"));
      Assert.assertNotNull(cache.get("6"));
      
      cache.size();
      cache.remove("2");
      Assert.assertNull(cache.get("2"));
   }

   @Test
   public void testSimpleCacheExpireObject() throws Exception {
      Cache<String, String> cache = new Cache<String, String>(1, 1, 10);
      cache.put("1", "1");
      Thread.sleep(3000);
      Assert.assertNull(cache.get("1"));
   }
   
   @Test
   public void testCleanupPerf() throws Exception {
      int size = 2000000;
      Cache<String, String> cache = new Cache<String, String>(size);
      
      for (int i = 0; i < size; i++) {
         String value = Integer.toString(i);
         cache.put(value, value);
      }      
      
      Assert.assertEquals(size, cache.size());
      
      Thread.sleep(200);
      
      long start = System.currentTimeMillis();
      cache.cleanup();
      double finish = (double)(System.currentTimeMillis() - start) / 1000.0;
      
      //System.out.println("Cleanup time " + finish + " s");
            
      Assert.assertEquals(0, cache.size());
   }
}
