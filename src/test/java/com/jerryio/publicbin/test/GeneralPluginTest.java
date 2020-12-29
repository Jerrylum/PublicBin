package com.jerryio.publicbin.test;

import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;

import be.seeseemelk.mockbukkit.MockBukkit;

public class GeneralPluginTest extends StandardTest {
    
    @SuppressWarnings("deprecation")
    @Test
    public void testPluginDebugMode() {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
        config = PublicBinPlugin.getPluginSetting().getConfig();
        config.set("debug", true);
        
        plugin.onReload(false);
        
        MockBukkit.unmock();
    }
}
