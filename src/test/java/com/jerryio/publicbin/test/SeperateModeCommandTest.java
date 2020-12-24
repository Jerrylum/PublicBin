package com.jerryio.publicbin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinCommandHandler;
import com.jerryio.publicbin.objects.BinManager;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

public class SeperateModeCommandTest {
    private ServerMock server;
    private PublicBinPlugin plugin;
    private YamlConfiguration config;
    private BinCommandHandler handler;
    private BinManager manager;
    
    private CustomPlayerMock player1;
    private PermissionAttachment pa1;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws IOException {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
        config = PublicBinPlugin.getPluginSetting().getConfig();
        config.set("mode", "separate");
        plugin.onReload(false);
        handler = PublicBinPlugin.getCommandHandler();
        manager = PublicBinPlugin.getBinManager();
        
        player1 = new CustomPlayerMock(server, "Player1");
        pa1 = player1.addAttachment(plugin);
        server.addPlayer(player1);
    }
    
    @Test
    public void testDefaultUseCommandNoPermission() {
        pa1.setPermission("publicbin.use", false);
        handler.onCommand(player1, null, "bin", new String[] {});
        player1.assertSaid("§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testDefaultUseCommandWithPermission() {
        pa1.setPermission("publicbin.use", true);
        handler.onCommand(player1, null, "bin", new String[] {});
        assertEquals(manager.getUsableBin(player1).getInventory(), player1.getOpenInventory().getTopInventory());
    }
    
    @Test
    public void testUseCommandNoPermission() {
        pa1.setPermission("publicbin.use", false);
        handler.onCommand(player1, null, "bin", new String[] {"use"});
        player1.assertSaid("§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testUseCommandWithPermission() {
        pa1.setPermission("publicbin.use", true);
        handler.onCommand(player1, null, "bin", new String[] {"use"});
        assertEquals(manager.getUsableBin(player1).getInventory(), player1.getOpenInventory().getTopInventory());
    }
    
    @Test
    public void testClearCommandNoPermission() {
        handler.onCommand(player1, null, "bin", new String[] {"clear"});
        player1.assertSaid("§4You dont have permission.");
    }
    
    @Test
    public void testClearMyBinCommandWithPermission() {
        pa1.setPermission("publicbin.command.clear.me", true);

        Inventory inv = manager.getUsableBin(player1).getInventory();
        ItemStack item1 = new ItemStack(Material.STONE, 64);
        inv.addItem(item1);
        
        handler.onCommand(player1, null, "bin", new String[] {"clear"});
        
        player1.assertSaid("§bCleared your bin.");
        player1.assertNoMoreSaid();

        assertNull(inv.getItem(0));
    }
    
    @Test
    public void testClearOfflinePlayerBinNoPermission() {
        pa1.setPermission("publicbin.command.clear.me", true);
        handler.onCommand(player1, null, "bin", new String[] {"clear", "whatever"});
        player1.assertSaid("§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testClearOnlinePlayerBinNoPermission() {
        pa1.setPermission("publicbin.command.clear.me", true);
        handler.onCommand(player1, null, "bin", new String[] {"clear", "Player1"});
        player1.assertSaid("§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testClearOfflinePlayerBinWithPermission() {
        pa1.setPermission("publicbin.command.clear.me", true);
        pa1.setPermission("publicbin.command.clear.others", true);
        handler.onCommand(player1, null, "bin", new String[] {"clear", "whatever"});
        player1.assertSaid("§4Target not found.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testClearOnlinePlayerBinWithPermission() {
        pa1.setPermission("publicbin.command.clear.me", true);
        pa1.setPermission("publicbin.command.clear.others", true);
        
        Inventory inv = manager.getUsableBin(player1).getInventory();
        ItemStack item1 = new ItemStack(Material.STONE, 64);
        inv.addItem(item1);
        
        handler.onCommand(player1, null, "bin", new String[] {"clear", "Player1"});
        player1.assertSaid("§bCleared Player1 private trash bin.");
        player1.assertNoMoreSaid();
        
        assertNull(inv.getItem(0));
    }
    
    @Test
    public void testReloadCommandNoPermission() {
        handler.onCommand(player1, null, "bin", new String[] {"reload"});
        player1.assertSaid("§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testReloadCommandWithPermission() {
        pa1.setPermission("publicbin.command.reload", true);
        handler.onCommand(player1, null, "bin", new String[] {"reload"});
        player1.assertSaid("§bPlugin reloaded.");
        player1.assertNoMoreSaid();
    }

    @Test
    public void testHelpCommandNoPermission() {
        handler.onCommand(player1, null, "bin", new String[] {"help"});
        player1.assertSaid("§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testHelpCommandWithPermission() {
        pa1.setPermission("publicbin.command.help", true);
        handler.onCommand(player1, null, "bin", new String[] {"help"});
        player1.assertSaid("");
        player1.assertSaid("§3PublicBin Commands");
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
