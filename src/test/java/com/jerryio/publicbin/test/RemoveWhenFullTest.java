package com.jerryio.publicbin.test;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import com.jerryio.publicbin.util.DateTime;

public class RemoveWhenFullTest extends AbstractInventoryTest {

    @Override
    public void configSetting() {
        config.set("countdown-despawn.enable", false);
        config.set("remove-when-full.enable", true);
        config.set("smart-grouping.enable", false);
    }
    
    @Test
    public void testSetInventoryContentAndUnchanged() {
        ItemStack[] raw = new ItemStack[9 * 2];
        raw[0] = new ItemStack(Material.WOODEN_AXE);
        raw[2] = new ItemStack(Material.APPLE);
        raw[4] = new ItemStack(Material.APPLE);
        raw[6] = new ItemStack(Material.GLASS);
        raw[8] = new ItemStack(Material.RED_BED);
        raw[9] = new ItemStack(Material.ORANGE_BED);
        raw[10] = new ItemStack(Material.YELLOW_BED);
        raw[11] = new ItemStack(Material.GREEN_BED);
        raw[12] = new ItemStack(Material.CYAN_BED);
        raw[13] = new ItemStack(Material.LIGHT_BLUE_BED);
        raw[14] = new ItemStack(Material.BLUE_BED);
        raw[15] = new ItemStack(Material.PURPLE_BED);
        
        inv.setContents(raw);
        bin.requestUpdate();
        server.getScheduler().performTicks(1);

        // should be unchanged
        assertItemStackArray(raw, inv.getContents());

        DateTime.addMockTimestamp(300 * 1000);
        server.getScheduler().performTicks(300 * 20);
        
        // should be the same
        assertItemStackArray(raw, inv.getContents());
        
        closeInventory();
    }
    
    @Test
    public void testSetInventoryContentAndChanged() {
        // threshold = 6; 18 - 6 = 12
        
        ItemStack[] raw = new ItemStack[9 * 2];
        raw[0] = new ItemStack(Material.WOODEN_AXE);
        raw[2] = new ItemStack(Material.APPLE);
        raw[4] = new ItemStack(Material.APPLE);
        raw[6] = new ItemStack(Material.GLASS);
        raw[8] = new ItemStack(Material.RED_BED);
        raw[9] = new ItemStack(Material.ORANGE_BED);
        raw[10] = new ItemStack(Material.YELLOW_BED);
        raw[11] = new ItemStack(Material.GREEN_BED);
        raw[12] = new ItemStack(Material.CYAN_BED);
        raw[13] = new ItemStack(Material.LIGHT_BLUE_BED);
        raw[14] = new ItemStack(Material.BLUE_BED);
        raw[15] = new ItemStack(Material.PURPLE_BED);
        raw[16] = new ItemStack(Material.GRAY_BED);
        raw[17] = new ItemStack(Material.BLACK_BED);
        
        ItemStack[] expected = new ItemStack[9 * 2];
        expected[0] = raw[0];
        expected[6] = raw[6];
        expected[8] = raw[8];
        expected[9] = raw[9];
        expected[10] = raw[10];
        expected[11] = raw[11];
        expected[12] = raw[12];
        expected[13] = raw[13];
        expected[14] = raw[14];
        expected[15] = raw[15];
        expected[16] = raw[16];
        expected[17] = raw[17];
        
        inv.setContents(raw);
        bin.requestUpdate();
        server.getScheduler().performTicks(1);        

        // should be cleared two apples
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(300 * 1000);
        server.getScheduler().performTicks(300 * 20);
        
        // should be the same
        assertItemStackArray(expected, inv.getContents());
        
        closeInventory();
    }
    
    @Test
    public void testAddItemsToInventoryAndUnchanged() {
        openInventory();
        
        // start adding items        
        ItemStack[] expected = new ItemStack[9 * 2];
                
        addItem(expected[0] = new ItemStack(Material.WOODEN_AXE), 0);
        addItem(expected[2] = new ItemStack(Material.APPLE), 2);
        addItem(expected[4] = new ItemStack(Material.APPLE), 4);
        addItem(expected[6] = new ItemStack(Material.GLASS), 6);
        addItem(expected[8] = new ItemStack(Material.RED_BED), 8);
        addItem(expected[9] = new ItemStack(Material.ORANGE_BED), 9);
        addItem(expected[10] = new ItemStack(Material.YELLOW_BED), 10);
        addItem(expected[11] = new ItemStack(Material.GREEN_BED), 11);
        addItem(expected[12] = new ItemStack(Material.CYAN_BED), 12);
        addItem(expected[13] = new ItemStack(Material.LIGHT_BLUE_BED), 13);
        addItem(expected[14] = new ItemStack(Material.BLUE_BED), 14);
        addItem(expected[15] = new ItemStack(Material.PURPLE_BED), 15);

        server.getScheduler().performTicks(1);
        

        // should be unchanged
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be the same
        assertItemStackArray(expected, inv.getContents());
        
        closeInventory();
    }
    
    @Test
    public void testAddItemsToInventoryAndChanged() {
        openInventory();
        
        // start adding items        
        ItemStack[] expected = new ItemStack[9 * 2];
                
        addItem(expected[0] = new ItemStack(Material.WOODEN_AXE), 0);
        addItem(new ItemStack(Material.APPLE), 2);
        addItem(new ItemStack(Material.APPLE), 4);
        addItem(expected[6] = new ItemStack(Material.GLASS), 6);
        addItem(expected[8] = new ItemStack(Material.RED_BED), 8);
        addItem(expected[9] = new ItemStack(Material.ORANGE_BED), 9);
        addItem(expected[10] = new ItemStack(Material.YELLOW_BED), 10);
        addItem(expected[11] = new ItemStack(Material.GREEN_BED), 11);
        addItem(expected[12] = new ItemStack(Material.CYAN_BED), 12);
        addItem(expected[13] = new ItemStack(Material.LIGHT_BLUE_BED), 13);
        addItem(expected[14] = new ItemStack(Material.BLUE_BED), 14);
        addItem(expected[15] = new ItemStack(Material.PURPLE_BED), 15);
        addItem(expected[16] = new ItemStack(Material.GRAY_BED), 16);
        addItem(expected[17] = new ItemStack(Material.BLACK_BED), 17);

        server.getScheduler().performTicks(1);
        

        // should be cleared two apples
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be the same
        assertItemStackArray(expected, inv.getContents());
        
        closeInventory();
    }

}
