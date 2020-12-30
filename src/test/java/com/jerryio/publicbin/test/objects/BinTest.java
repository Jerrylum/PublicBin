package com.jerryio.publicbin.test.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.test.mock.CustomBin;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;
import com.jerryio.publicbin.test.mock.CustomeInventoryMock;

import be.seeseemelk.mockbukkit.MockBukkit;

public class BinTest extends StandardTest {

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
        config = PublicBinPlugin.getPluginSetting().getConfig();
        
        player1 = new CustomPlayerMock(server, "Player1");
        pa1 = player1.addAttachment(plugin);
        pa1.setPermission("publicbin.use", true);
        server.addPlayer(player1);
    }
    
    
    @Test
    public void testRepeatCloseBinManager() {
        CustomBin bin = new CustomBin();
        bin.requestUpdate();
        bin.close();
        bin.close();
    }
    
    @Test
    public void testRepeatRequestUpdate() {
        CustomBin bin = new CustomBin();
        bin.requestUpdate();
        bin.requestUpdate();
        bin.requestUpdate();
        bin.cancelTask();
        bin.requestUpdate();
        bin.requestUpdate();
        bin.requestUpdate();
    }
    
    @Test
    public void testViewer() {
        System.clearProperty("MockTest"); // special case
        
        CustomBin bin = new CustomBin();
        
        assertEquals(false, bin.hasViewer());
        CustomeInventoryMock inv = (CustomeInventoryMock)bin.getInventory();
        inv.allViewer.add(player1);
        
        assertEquals(true, bin.hasViewer());
        assertEquals(inv.allViewer, bin.getViewers());
    }
    
    @Test
    public void testNullInItemList() {
        CustomBin bin = new CustomBin();
        
        bin.getInventory().setItem(0, new ItemStack(Material.APPLE));
        bin.addNullToItemList();
        bin.requestUpdate();
        
        server.getScheduler().performTicks(1);
    }
    
    @Test
    public void testInventorySizeChanged() {
        CustomBin bin = new CustomBin();
        
        bin.newInventory();
        bin.requestUpdate();
        
        server.getScheduler().performTicks(1);
    }
    
    @Test
    public void testSlotChanged() {
        CustomBin bin = new CustomBin();
        
        bin.getInventory().setItem(0, new ItemStack(Material.APPLE));
        
        bin.requestUpdate();
        server.getScheduler().performTicks(1);
        
        bin.getInventory().setItem(0, new ItemStack(Material.GLASS));
        
        bin.requestUpdate();
        server.getScheduler().performTicks(1);
    }
    
    @Test
    public void testThresholdOver() {
        CustomBin bin = new CustomBin();
        Inventory inv = bin.getInventory();
        
        config.set("remove-when-full.threshold", 1000);
        
        inv.setItem(0, new ItemStack(Material.APPLE));
        
        bin.requestUpdate();
        server.getScheduler().performTicks(1);
        
        assertTrue(inv.getItem(0) == null || inv.getItem(0).getType() == Material.AIR);
    }
    
    @Test
    public void testUselessTimeUpdate() {
        CustomBin bin = new CustomBin();

        bin.getInventory().setItem(0, new ItemStack(Material.APPLE));
        
        bin.requestUpdate();
        server.getScheduler().performTicks(1);

        bin.timeUpdate(0, 0);
    }
    

    @After
    public void tearDown() {
        System.setProperty("MockTest", "true"); // special case
        MockBukkit.unmock();
    }
}
