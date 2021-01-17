package com.jerryio.publicbin.test.disk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.enums.OrderEnum;
import com.jerryio.publicbin.test.StandardTest;

import be.seeseemelk.mockbukkit.MockBukkit;

public class PluginSettingTest extends StandardTest {

    @Before
    public void setUp() throws IOException {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
    @Test
    public void testFailedPluginSetting() throws Exception {
        FileWriter myWriter = new FileWriter(new File(plugin.getDataFolder(), "config.yml"));
        myWriter.close(); // write empty config file

        plugin.onReload(true);
        
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        
        setting.getLang();
        setting.getMode();
        setting.getBinRow();
        setting.isAutoDespawnEnabled();
        setting.getKeepingTime();
        setting.isClearIntervalsEnabled();
        setting.getClearIntervalsTime();
        setting.getClearWarningMessageType();
        setting.getClearWarningPeriod();
        setting.isRmoveWhenFullEnabled();
        setting.getFullThreshold();
        setting.getAutoRemovePrincipleList();
        setting.isSmartGroupingEnabled();
        setting.getSmartGroupingPrincipleList();
        setting.isDebug();
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testInvalidPrincipleList() {
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        
        config = PublicBinPlugin.getPluginSetting().getConfig();
        
        List<String> set = Arrays.asList("type", "metadata", "unknown", "time");
        
        config.set("smart-grouping.order", set);

        OrderEnum[] expected = { OrderEnum.Type, OrderEnum.Metadata, OrderEnum.Time };
        OrderEnum[] get = setting.getSmartGroupingPrincipleList();
        
        assertArrayEquals(expected, get);
    }
    
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
