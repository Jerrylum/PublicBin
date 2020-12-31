package com.jerryio.publicbin.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.util.I18n;
import com.jerryio.publicbin.util.I18n.UTF8PropertiesControl;

import be.seeseemelk.mockbukkit.MockBukkit;

public class I18nTest extends StandardTest {

    @Test
    public void testNewI18nInstance() {
        new I18n();
    }
    
    @Test
    public void testReloadPropertiesControl() throws IOException {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        
        UTF8PropertiesControl control = new UTF8PropertiesControl();
        control.newBundle("messages" , new Locale("en", "us"), "java.properties", cl, true);
        
        control.newBundle("messages" , new Locale("en", "uk"), "java.properties", cl, true);
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

        
        assertNotEquals(I18n.n("en_us", "command-help-title"), I18n.n("zh_tw", "command-help-title"));
        
        MockBukkit.unmock();
    }
}
