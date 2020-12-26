package com.jerryio.publicbin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinCommandHandler;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.objects.BinManager;
import com.jerryio.publicbin.test.mock.CustomConsoleCommandSenderMock;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

public class BinManagerTest {
    private ServerMock server;
    private PublicBinPlugin plugin;
    private YamlConfiguration config;
    private BinCommandHandler handler;
    private BinManager manager;
    
    private CustomPlayerMock player1;
    private PermissionAttachment pa1;
    
    private CustomPlayerMock player2;
    private PermissionAttachment pa2;

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
        
        player2 = new CustomPlayerMock(server, "Player2");
        pa2 = player2.addAttachment(plugin);
        pa2.setPermission("publicbin.use", true);
        server.addPlayer(player2);
    }
    
    @Test
    public void testPublicBinManager() {
        config.set("mode", "share");
        plugin.onReload(false);
        handler = PublicBinPlugin.getCommandHandler();
        manager = PublicBinPlugin.getBinManager();

        handler.onCommand(player1, null, "bin", new String[] {});
        handler.onCommand(player2, null, "bin", new String[] {});
        
        assertEquals(manager.getUsableBin(player1).getInventory(), player1.getOpenInventory().getTopInventory());
        assertEquals(manager.getUsableBin(player2).getInventory(), player2.getOpenInventory().getTopInventory());
        assertEquals(manager.getUsableBin(player1).getInventory(), manager.getUsableBin(player2).getInventory());
        
        PlayerQuitEvent event1 = new PlayerQuitEvent(player1, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event1);
        
        PlayerQuitEvent event2 = new PlayerQuitEvent(player2, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event2);
    }
    
    @Test
    public void testPrivateBinManager() {
        config.set("mode", "seperate");
        plugin.onReload(false);
        handler = PublicBinPlugin.getCommandHandler();
        manager = PublicBinPlugin.getBinManager();

        handler.onCommand(player1, null, "bin", new String[] {});
        handler.onCommand(player2, null, "bin", new String[] {});
        
        assertEquals(manager.getUsableBin(player1).getInventory(), player1.getOpenInventory().getTopInventory());
        assertEquals(manager.getUsableBin(player2).getInventory(), player2.getOpenInventory().getTopInventory());
        assertNotEquals(manager.getUsableBin(player1).getInventory(), manager.getUsableBin(player2).getInventory());
        
        PlayerQuitEvent event1 = new PlayerQuitEvent(player1, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event1);
        
        PlayerQuitEvent event2 = new PlayerQuitEvent(player2, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event2);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}