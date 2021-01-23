package com.jerryio.publicbin.test.update;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.update.PublicBinUpdateChecker;

import be.seeseemelk.mockbukkit.MockBukkit;

public class UpdateCheckerTest extends StandardTest {
    
    @Before
    public void setUp() throws IOException {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
    @Test
    public void testPrintMessage() {        
        new PublicBinUpdateChecker(plugin).printMessage("HaHa", false);
        new PublicBinUpdateChecker(plugin).printMessage("HaHa", true);
    }
    
    @Test
    public void testAnalysisVersion() {
        assertEquals(false, new PublicBinUpdateChecker(plugin).analysisVersion(""));
        assertEquals(false, new PublicBinUpdateChecker(plugin).analysisVersion("error"));
        assertEquals(true, new PublicBinUpdateChecker(plugin).analysisVersion("9.9.9"));
    }
    
    @Test
    public void testNormalCheck() {
        new PublicBinUpdateChecker(plugin).checkForUpdates();
        
        server.getScheduler().performTicks(1);
    }
    
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
