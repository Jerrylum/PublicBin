package com.jerryio.publicbin.test.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import com.jerryio.publicbin.test.AbstractInventoryTest;
import com.jerryio.publicbin.util.DateTime;

public class CountDownDespawnTest extends AbstractInventoryTest {
    
    public void configSetting() {
        config.set("countdown-despawn.enable", true);
        config.set("remove-when-full.enable", false);
        config.set("smart-grouping.enable", false);
    }
    
    @Test
    public void testSetInventoryContentAndRequestUpdate() {
        ItemStack[] raw = new ItemStack[9 * 2];
        raw[0] = new ItemStack(Material.WOODEN_AXE);
        raw[2] = new ItemStack(Material.APPLE);
        raw[4] = new ItemStack(Material.APPLE);
        raw[6] = new ItemStack(Material.GLASS);
        raw[8] = new ItemStack(Material.RED_BED);
        
        inv.setContents(raw);
        bin.requestUpdate();
        server.getScheduler().performTicks(1);
                
        // should be unchanged
        assertItemStackArray(raw, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be empty
        assertItemStackArray(new ItemStack[9 * 2], inv.getContents());
        
        closeInventory();
    }
    
    @Test
    public void testAddItemsToInventoryAndRequestUpdate() {
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
        addItem(expected[16] = new ItemStack(Material.GRAY_BED), 16);
        addItem(expected[17] = new ItemStack(Material.BLACK_BED), 17);

        server.getScheduler().performTicks(1);
        

        // should be unchanged
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be empty
        assertItemStackArray(new ItemStack[9 * 2], inv.getContents());
        
        closeInventory();
    }
    
    @Test
    public void testWait10MinutesThenAddItemsToInventoryAndRequestUpdate() {
        DateTime.addMockTimestamp(10 * 60 * 1000);
        server.getScheduler().performTicks(10 * 60 * 20);
        
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
        addItem(expected[16] = new ItemStack(Material.GRAY_BED), 16);
        addItem(expected[17] = new ItemStack(Material.BLACK_BED), 17);

        server.getScheduler().performTicks(1);
        

        // should be unchanged
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be empty
        assertItemStackArray(new ItemStack[9 * 2], inv.getContents());
        
        closeInventory();
    }

}
