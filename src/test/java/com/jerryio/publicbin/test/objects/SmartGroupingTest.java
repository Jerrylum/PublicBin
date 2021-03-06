package com.jerryio.publicbin.test.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import com.jerryio.publicbin.test.AbstractInventoryTest;
import com.jerryio.publicbin.util.DateTime;

public class SmartGroupingTest extends AbstractInventoryTest {

    @Override
    public void configSetting() {
        config.set("countdown-despawn.enable", false);
        config.set("clear-intervals.enable", false);
        config.set("remove-when-full.enable", false);
        config.set("smart-grouping.enable", true);
    }
    
    @Test
    public void testSetInventoryContent() {
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

        ItemStack[] expected = {
            raw[6], raw[10], raw[8], raw[15], raw[9], raw[11], raw[16], raw[12], raw[13], 
            raw[14], raw[17], raw[0], new ItemStack(Material.APPLE, 2)
        };
        
        inv.setContents(raw);
        bin.requestUpdate();
        server.getScheduler().performTicks(1);

        // should be same as the expected array
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be the same
        assertItemStackArray(expected, inv.getContents());
        
        closeInventory();
    }

    @Test
    public void testAddItemsToInventoryAndChanged1() {
        openInventory();
        
        // start adding items        
        ItemStack[] expected = new ItemStack[9 * 2];
                
        addItem(expected[11] = new ItemStack(Material.WOODEN_AXE), 0);
        addItem(new ItemStack(Material.APPLE), 2);
        addItem(new ItemStack(Material.APPLE), 4);
        addItem(expected[0] = new ItemStack(Material.GLASS), 6);
        addItem(expected[2] = new ItemStack(Material.RED_BED), 8);
        addItem(expected[4] = new ItemStack(Material.ORANGE_BED), 9);
        addItem(expected[1] = new ItemStack(Material.YELLOW_BED), 10);
        addItem(expected[5] = new ItemStack(Material.GREEN_BED), 11);
        addItem(expected[7] = new ItemStack(Material.CYAN_BED), 12);
        addItem(expected[8] = new ItemStack(Material.LIGHT_BLUE_BED), 13);
        addItem(expected[9] = new ItemStack(Material.BLUE_BED), 14);
        addItem(expected[3] = new ItemStack(Material.PURPLE_BED), 15);
        addItem(expected[6] = new ItemStack(Material.GRAY_BED), 16);
        addItem(expected[10] = new ItemStack(Material.BLACK_BED), 17);
        
        expected[12] = new ItemStack(Material.APPLE, 2);

        server.getScheduler().performTicks(1);
        

        // should be cleared two apples
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be the same
        assertItemStackArray(expected, inv.getContents());
        
        closeInventory();
    }
    
    @Test
    public void testAddItemsToInventoryAndChanged2() {
        openInventory();
        
        // start adding items        
        ItemStack[] expected = new ItemStack[9 * 2];
                
        addItem(expected[11] = new ItemStack(Material.WOODEN_AXE), 0);
        addItem(new ItemStack(Material.APPLE, 32), 2);
        addItem(new ItemStack(Material.APPLE, 48), 4);
        addItem(expected[0] = new ItemStack(Material.GLASS), 6);
        addItem(expected[2] = new ItemStack(Material.RED_BED), 8);
        addItem(expected[4] = new ItemStack(Material.ORANGE_BED), 9);
        addItem(expected[1] = new ItemStack(Material.YELLOW_BED), 10);
        addItem(expected[5] = new ItemStack(Material.GREEN_BED), 11);
        addItem(expected[7] = new ItemStack(Material.CYAN_BED), 12);
        addItem(expected[8] = new ItemStack(Material.LIGHT_BLUE_BED), 13);
        addItem(expected[9] = new ItemStack(Material.BLUE_BED), 14);
        addItem(expected[3] = new ItemStack(Material.PURPLE_BED), 15);
        addItem(expected[6] = new ItemStack(Material.GRAY_BED), 16);
        addItem(expected[10] = new ItemStack(Material.BLACK_BED), 17);
        
        expected[12] = new ItemStack(Material.APPLE, 64);
        expected[13] = new ItemStack(Material.APPLE, 16);

        server.getScheduler().performTicks(1);
        

        // should be cleared two apples
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be the same
        assertItemStackArray(expected, inv.getContents());
        
        closeInventory();
    }
    
    @Test
    public void testAddItemsToInventoryOneByOneAndChanged() {
        openInventory();
        
        // start adding items        
        ItemStack[] expected = new ItemStack[9 * 2];
                
        addItemAndWait(expected[11] = new ItemStack(Material.WOODEN_AXE), 0);
        addItemAndWait(new ItemStack(Material.APPLE, 32), 2);
        addItemAndWait(new ItemStack(Material.APPLE, 32), 4);
        addItemAndWait(expected[0] = new ItemStack(Material.GLASS), 6);
        addItemAndWait(expected[2] = new ItemStack(Material.RED_BED), 8);
        addItemAndWait(expected[4] = new ItemStack(Material.ORANGE_BED), 9);
        addItemAndWait(expected[1] = new ItemStack(Material.YELLOW_BED), 10);
        addItemAndWait(expected[5] = new ItemStack(Material.GREEN_BED), 11);
        addItemAndWait(expected[7] = new ItemStack(Material.CYAN_BED), 12);
        addItemAndWait(expected[8] = new ItemStack(Material.LIGHT_BLUE_BED), 13);
        addItemAndWait(expected[9] = new ItemStack(Material.BLUE_BED), 14);
        addItemAndWait(expected[3] = new ItemStack(Material.PURPLE_BED), 15);
        addItemAndWait(expected[6] = new ItemStack(Material.GRAY_BED), 16);
        addItemAndWait(expected[10] = new ItemStack(Material.BLACK_BED), 17);
        
        expected[12] = new ItemStack(Material.APPLE, 64); // test mother full break
        

        // should be cleared two apples
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(setting.getKeepingTime() * 1000);
        server.getScheduler().performTicks(setting.getKeepingTime() * 20);
        
        // should be the same
        assertItemStackArray(expected, inv.getContents());
        
        closeInventory();
    }
    
    protected void addItemAndWait(ItemStack item, int slot) {
        super.addItem(item, slot);
        server.getScheduler().performTicks(1);
    }
}
