package com.jerryio.publicbin.test.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.objects.Bin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.test.mock.CustomItemStack;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;
import com.jerryio.publicbin.util.BukkitVersion;

import be.seeseemelk.mockbukkit.MockBukkit;

public class MainListenerTest extends StandardTest {

    @Before
    public void setUp() {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
        handler = PublicBinPlugin.getCommandHandler();
        manager = PublicBinPlugin.getBinManager();
        
        player1 = new CustomPlayerMock(server, "Player1");
        pa1 = player1.addAttachment(plugin);
        server.addPlayer(player1);
    }
    
    @Test
    public void testInventoryClickEventWithNothing() {
        Inventory inv = Bukkit.createInventory(null, 9 * 1);
        player1.openInventory(inv);
        
        InventoryClickEvent event = new InventoryClickEvent(player1.getOpenInventory(), SlotType.CONTAINER, 0, ClickType.RIGHT, InventoryAction.PICKUP_HALF);
        Bukkit.getPluginManager().callEvent(event);
        server.getScheduler().performTicks(20);
    }
    
    @Test
    public void testInventoryDragEvent​WithNothing() {
        Inventory inv = Bukkit.createInventory(null, 9 * 1);
        player1.openInventory(inv);
        
        InventoryDragEvent event = new InventoryDragEvent(player1.getOpenInventory(), new CustomItemStack(false), new CustomItemStack(false), false, new HashMap<Integer, ItemStack>());
        Bukkit.getPluginManager().callEvent(event);
        server.getScheduler().performTicks(20);
    }
    
    @Test
    public void testInventoryDragEvent​WithBin() {
        Bin usableBin = PublicBinPlugin.getBinManager().getUsableBin(player1);
        player1.openInventory(usableBin.getInventory());
        
        InventoryDragEvent event = new InventoryDragEvent(player1.getOpenInventory(), new CustomItemStack(false), new CustomItemStack(false), false, new HashMap<Integer, ItemStack>());
        Bukkit.getPluginManager().callEvent(event);
        server.getScheduler().performTicks(20);
    }
    
    @Test
    public void testInventoryOpenEventWithNothing() {
        Inventory inv = Bukkit.createInventory(null, 9 * 1);
        player1.openInventory(inv);
        
        InventoryOpenEvent event = new InventoryOpenEvent(player1.getOpenInventory());
        Bukkit.getPluginManager().callEvent(event);
        server.getScheduler().performTicks(20);
    }
    
    @Test
    public void testInventoryCloseEventWithNothing() {
        Inventory inv = Bukkit.createInventory(null, 9 * 1);
        player1.openInventory(inv);
        
        InventoryCloseEvent event = new InventoryCloseEvent(player1.getOpenInventory());
        Bukkit.getPluginManager().callEvent(event);
        server.getScheduler().performTicks(20);
    }
    
    @Test
    public void testInventoryCloseEventWithBin() {
        Bin usableBin = PublicBinPlugin.getBinManager().getUsableBin(player1);
        player1.openInventory(usableBin.getInventory());
        
        InventoryCloseEvent event = new InventoryCloseEvent(player1.getOpenInventory());
        Bukkit.getPluginManager().callEvent(event);
        server.getScheduler().performTicks(20);
    }
    
    @Test
    public void testPlaySound() {
        Bin usableBin = PublicBinPlugin.getBinManager().getUsableBin(player1);
        player1.openInventory(usableBin.getInventory());
        
        BukkitVersion.setMockVersion("1.12");
        
        InventoryCloseEvent event = new InventoryCloseEvent(player1.getOpenInventory());
        Bukkit.getPluginManager().callEvent(event);
        server.getScheduler().performTicks(20);
        
        BukkitVersion.setMockVersion("1.15");
        
        event = new InventoryCloseEvent(player1.getOpenInventory());
        Bukkit.getPluginManager().callEvent(event);
        server.getScheduler().performTicks(20);
    }
    
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
