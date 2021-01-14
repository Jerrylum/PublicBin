package com.jerryio.publicbin.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.util.I18n;
import com.jerryio.publicbin.util.I18n.UTF8PropertiesControl;

import be.seeseemelk.mockbukkit.MockBukkit;

public class I18nTest extends StandardTest {

    @Before
    public void setUp() throws IOException {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
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
        assertEquals("", I18n.t("unknow-key"));
    }
    
    @Test
    public void testMessagesConfig() {
        try {
            FileWriter myWriter = new FileWriter(new File(plugin.getDataFolder(), "messages.yml"));
            myWriter.write("public-bin-title: My custom message\ncustom-key: Custom stuff");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        I18n.load(plugin);
        
        assertEquals("Your Trash Bin", I18n.t("private-bin-title"));
        assertEquals("My custom message", I18n.t("public-bin-title"));
        assertEquals("Custom stuff", I18n.t("custom-key"));
    }
    
    @Test
    public void testInvalidMessagesConfig() {
        try {
            FileWriter myWriter = new FileWriter(new File(plugin.getDataFolder(), "messages.yml"));
            myWriter.write("Invalid File");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        I18n.load(plugin);
        
        assertEquals("Your Trash Bin", I18n.t("private-bin-title"));
        assertEquals("Public Trash Bin", I18n.t("public-bin-title"));
        assertEquals("", I18n.t("custom-key"));
    }
    
    @Test
    public void testNMethod() {        
        assertNotEquals(I18n.n("en_us", "command-help-title"), I18n.n("zh_tw", "command-help-title"));
    }
    
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
