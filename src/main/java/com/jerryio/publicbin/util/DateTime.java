package com.jerryio.publicbin.util;

import java.util.Date;

public class DateTime {
    private static long mockTime = 0;
    
    public static long getTimestamp() {
        return mockTime != 0 ? mockTime : new Date().getTime();
        
    }
    
    public static void setMockTimestamp(long time) {
        mockTime = time;
    }
    
    public static void addMockTimestamp(long time) {
        if (mockTime == 0)
            mockTime = new Date().getTime();
        mockTime += time;
    }
}
