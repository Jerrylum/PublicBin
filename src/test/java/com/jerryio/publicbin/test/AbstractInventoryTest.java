package com.jerryio.publicbin.test;

import static org.junit.Assert.assertEquals;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.After;
import org.junit.Before;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinCommandHandler;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.objects.Bin;
import com.jerryio.publicbin.objects.BinManager;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;
import com.jerryio.publicbin.util.DateTime;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

public abstract class AbstractInventoryTest {
    protected ServerMock server;
    protected PublicBinPlugin plugin;
    protected PluginSetting setting;
    protected YamlConfiguration config;
    protected BinCommandHandler handler;
    protected BinManager manager;
    
    protected CustomPlayerMock player1;
    protected PermissionAttachment pa1;
    
    protected Bin bin;
    protected Inventory inv;
    
    public abstract void configSetting();

    @Before
    public void setUp() {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
        config = PublicBinPlugin.getPluginSetting().getConfig();
        
        config.set("size", 2); // 2 rows
        
        configSetting();
        
        plugin.onReload(false);

        setting = PublicBinPlugin.getPluginSetting();
        handler = PublicBinPlugin.getCommandHandler();
        manager = PublicBinPlugin.getBinManager();
        
        player1 = new CustomPlayerMock(server, "Player1");
        pa1 = player1.addAttachment(plugin);
        server.addPlayer(player1);

        bin = manager.getUsableBin(null);
        inv = bin.getInventory();
    }
    
    protected void openInventory() {
        // open inventory, trigger InventoryOpenEvent
        
        pa1.setPermission("publicbin.use", true);
        handler.onCommand(player1, null, "bin", new String[] {});
        InventoryOpenEvent event = new InventoryOpenEvent(player1.getOpenInventory()); // XXX: trigger manually
        Bukkit.getPluginManager().callEvent(event);
        
        server.getScheduler().performTicks(1);
    }
    
    protected void closeInventory() {
        // close inventory
        
        player1.closeInventory();
        
        server.getScheduler().performTicks(1);
    }
    
    protected void addItem(ItemStack item, int slot) {
        bin.getInventory().setItem(slot, item);
        
        InventoryClickEvent event = new InventoryClickEvent(
                player1.getOpenInventory(), 
                SlotType.CONTAINER, 
                slot, 
                ClickType.LEFT, 
                InventoryAction.PLACE_ALL);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    protected void assertItemStackArray(ItemStack[] expected, ItemStack[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
    
    @After
    public void tearDown() {
        PlayerQuitEvent event = new PlayerQuitEvent(player1, "Tigger quit event");
        Bukkit.getPluginManager().callEvent(event);
        
        MockBukkit.unmock();
        DateTime.setMockTimestamp(0);
    }
}
