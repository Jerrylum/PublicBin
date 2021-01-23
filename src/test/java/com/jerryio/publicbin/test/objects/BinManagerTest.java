package com.jerryio.publicbin.test.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;

import be.seeseemelk.mockbukkit.MockBukkit;

public class BinManagerTest extends StandardTest {
    private CustomPlayerMock player2;
    private PermissionAttachment pa2;

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
        
        server.getScheduler().performTicks(1);
        
        assertEquals(manager.getUsableBin(player1).getInventory(), player1.getOpenInventory().getTopInventory());
        assertEquals(manager.getUsableBin(player2).getInventory(), player2.getOpenInventory().getTopInventory());
        assertEquals(manager.getUsableBin(player1).getInventory(), manager.getUsableBin(player2).getInventory());
        
        PlayerQuitEvent event1 = new PlayerQuitEvent(player1, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event1);
        
        PlayerQuitEvent event2 = new PlayerQuitEvent(player2, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event2);
        
        manager.close();
        manager.close();
        manager.close();
    }
    
    @Test
    public void testPrivateBinManager() {
        config.set("mode", "separate");
        plugin.onReload(false);
        handler = PublicBinPlugin.getCommandHandler();
        manager = PublicBinPlugin.getBinManager();

        handler.onCommand(player1, null, "bin", new String[] {});
        handler.onCommand(player2, null, "bin", new String[] {});
        
        server.getScheduler().performTicks(1);
        
        assertEquals(manager.getUsableBin(player1).getInventory(), player1.getOpenInventory().getTopInventory());
        assertEquals(manager.getUsableBin(player2).getInventory(), player2.getOpenInventory().getTopInventory());
        assertNotEquals(manager.getUsableBin(player1).getInventory(), manager.getUsableBin(player2).getInventory());
        
        PlayerQuitEvent event1 = new PlayerQuitEvent(player1, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event1);
        
        PlayerQuitEvent event2 = new PlayerQuitEvent(player2, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event2);
        
        manager.close();
        manager.close();
        manager.close();
    }
    
    @Test
    public void testTrackDroppedItem() {
        manager = PublicBinPlugin.getBinManager();
        
        manager.trackDroppedItem(null);
        manager.untrackDroppedItem(null);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
