package com.solace.poc;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

public class Test {
    @org.junit.Test
    public void testSelectorBuilder() {
        String[] args = { "192.168.56.103","demo","foo","bar","cf1","-te","dte1","a/b/c/d","glank",">","1.2345" };

        String selector = Helper.buildSelectorString(Arrays.copyOfRange(args, 8, args.length));
        System.out.println("Selector: " + selector);
        assertEquals("glank > 1.2345", selector.trim());
    }
}
