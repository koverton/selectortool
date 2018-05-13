package com.solace.poc;

class Helper {
    static String buildSelectorString(String[] args) {
        StringBuilder builder = new StringBuilder();
        for(String arg : args) {
            builder.append(arg).append(' ');
        }
        return builder.toString();
    }

    static void safeSleep(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
