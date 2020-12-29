package com.jerryio.publicbin.test.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.util.I18n;

import be.seeseemelk.mockbukkit.MockBukkit;

public class I18nTest extends StandardTest {

    @Test
    public void testNewI18nInstance() {
        new I18n();
    }
    
    @Test
    public void testUnknownKey() {       
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);

        
        assertEquals("", I18n.t(""));
        
        MockBukkit.unmock();
    }
    
    @Test
    public void testNMethod() {       
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);

        
        assertEquals("PublicBin 指令幫助", I18n.n("zh_tw", "command-help-title"));
        assertEquals("PublicBin Commands", I18n.n("en_us", "command-help-title"));
        
        MockBukkit.unmock();
    }
}
