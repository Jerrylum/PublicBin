package com.jerryio.publicbin.test.objects;

import static com.jerryio.publicbin.test.mock.CustomAssert.assertSaid;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import com.jerryio.publicbin.test.AbstractInventoryTest;
import com.jerryio.publicbin.util.DateTime;

public class ClearIntervalsTest extends AbstractInventoryTest {

    @Override
    public void configSetting() {
        config.set("countdown-despawn.enable", false);
        config.set("clear-intervals.enable", true);
        config.set("clear-intervals.warnings.type", "chat");
        config.set("remove-when-full.enable", false);
        config.set("smart-grouping.enable", false);
    }

    @Test
    public void testSetInventoryContentAndRequestUpdate() {
        for (int i = 0; i < 10; i ++) {
            ItemStack[] raw = new ItemStack[9 * 2];
            raw[0] = new ItemStack(Material.WOODEN_AXE);
            raw[2] = new ItemStack(Material.APPLE);
            raw[4] = new ItemStack(Material.APPLE);
            raw[6] = new ItemStack(Material.GLASS);
            raw[8] = new ItemStack(Material.RED_BED);
            
            inv.setContents(raw);
            bin.requestUpdate();
            server.getScheduler().performTicks(1);
                    
            normalCheck(raw);
            
            closeInventory();
        }
    }

    @Test
    public void testAddItemsToInventoryAndRequestUpdate() {
        for (int i = 0; i < 10; i ++) {
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

            normalCheck(expected);
            
            closeInventory();
        }        
    }

    @Test
    public void testWait10MinutesThenAddItemsToInventoryAndRequestUpdate() {
        for (int i = 0; i < 10; i ++) {
            DateTime.addMockTimestamp(10 * 60 * 1000);
            server.getScheduler().performTicks(10 * 60 * 20);
            while (player1.nextMessage() != null); // clear message queue
            
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

            normalCheck(expected);
            
            closeInventory();
        }        
    }
    
    private void normalCheck(ItemStack[] expected) {
        // should be unchanged
        assertItemStackArray(expected, inv.getContents());

        DateTime.addMockTimestamp(540 * 1000);
        server.getScheduler().performTicks(540 * 20);
        
        assertSaid(player1, "§bThe trash bin will be cleared in 60 seconds.");
        player1.assertNoMoreSaid();
        assertItemStackArray(expected, inv.getContents());
        
        DateTime.addMockTimestamp(30 * 1000);
        server.getScheduler().performTicks(30 * 20);
        
        assertSaid(player1, "§bThe trash bin will be cleared in 30 seconds.");
        player1.assertNoMoreSaid();
        assertItemStackArray(expected, inv.getContents());
        
        DateTime.addMockTimestamp(20 * 1000);
        server.getScheduler().performTicks(20 * 20);
        
        assertSaid(player1, "§bThe trash bin will be cleared in 10 seconds.");
        player1.assertNoMoreSaid();
        assertItemStackArray(expected, inv.getContents());
        
        DateTime.addMockTimestamp(10 * 1000);
        server.getScheduler().performTicks(10 * 20);
        
        assertSaid(player1, "§bThe trash bin has been cleared.");
        player1.assertNoMoreSaid();
        assertItemStackArray(new ItemStack[9 * 2], inv.getContents());
    }
}
