package com.jerryio.publicbin.test.commands;

import static com.jerryio.publicbin.test.mock.CustomAssert.assertSaid;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.test.mock.CustomConsoleCommandSenderMock;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;

import be.seeseemelk.mockbukkit.MockBukkit;

public class ShareModeCommandTest extends StandardTest {

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
        
        console = new CustomConsoleCommandSenderMock();
    }
    
    @Test
    public void testDefaultUseCommandInConsole() {
        handler.onCommand(console, null, "bin", new String[] {});
        assertSaid(console, "");
        assertSaid(console, "§3PublicBin Commands");
    }
    
    @Test
    public void testDefaultUseCommandNoPermission() {
        pa1.setPermission("publicbin.use", false);
        handler.onCommand(player1, null, "bin", new String[] {});
        assertSaid(player1, "§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testDefaultUseCommandWithPermission() {
        pa1.setPermission("publicbin.use", true);
        handler.onCommand(player1, null, "bin", new String[] {});
        
        server.getScheduler().performTicks(1);
        
        assertEquals(manager.getUsableBin(player1).getInventory(), player1.getOpenInventory().getTopInventory());
    }
    
    @Test
    public void testUseCommandInConsole() {
        handler.onCommand(console, null, "bin", new String[] {"use"});
        assertSaid(console, "§4You must be a player to use this command.");
        console.assertNoMoreSaid();
    }
    
    @Test
    public void testUseCommandNoPermission() {
        pa1.setPermission("publicbin.use", false);
        handler.onCommand(player1, null, "bin", new String[] {"use"});
        assertSaid(player1, "§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testUseCommandWithPermission() {
        pa1.setPermission("publicbin.use", true);
        handler.onCommand(player1, null, "bin", new String[] {"use"});
        
        server.getScheduler().performTicks(1);
        
        assertEquals(manager.getUsableBin(player1).getInventory(), player1.getOpenInventory().getTopInventory());
    }
    
    @Test
    public void testClearCommandNoPermission() {
        handler.onCommand(player1, null, "bin", new String[] {"clear"});
        assertSaid(player1, "§4You dont have permission.");
    }
    
    @Test
    public void testClearCommandWithPermission() {
        pa1.setPermission("publicbin.command.clear.public", true);
        
        Inventory inv = manager.getUsableBin(player1).getInventory();
        ItemStack item1 = new ItemStack(Material.STONE, 64);
        inv.addItem(item1);
        
        handler.onCommand(player1, null, "bin", new String[] {"clear"});
        assertSaid(player1, "§bCleared public bin.");
        player1.assertNoMoreSaid();

        assertTrue(inv.getItem(0) == null || inv.getItem(0).getType() == Material.AIR);
    }
    
    @Test
    public void testClearCommandWithNameAndPermission() {
        pa1.setPermission("publicbin.command.clear.public", true);
        handler.onCommand(player1, null, "bin", new String[] {"clear", "whatever"});
        assertSaid(player1, "§bCleared public bin.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testReloadCommandNoPermission() {
        handler.onCommand(player1, null, "bin", new String[] {"reload"});
        assertSaid(player1, "§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testReloadCommandWithPermission() {
        pa1.setPermission("publicbin.command.reload", true);
        handler.onCommand(player1, null, "bin", new String[] {"reload"});
        assertSaid(player1, "§bPlugin reloaded.");
        player1.assertNoMoreSaid();
    }

    @Test
    public void testHelpCommandNoPermission() {
        handler.onCommand(player1, null, "bin", new String[] {"help"});
        assertSaid(player1, "§4You dont have permission.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testHelpCommandWithPermission() {
        pa1.setPermission("publicbin.command.help", true);
        handler.onCommand(player1, null, "bin", new String[] {"help"});
        assertSaid(player1, "");
        assertSaid(player1, "§3PublicBin Commands");
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
