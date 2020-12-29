package com.jerryio.publicbin.test.commands;

import static com.jerryio.publicbin.test.mock.CustomAssert.assertSaid;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.test.mock.CustomBinSubCommand;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;

import be.seeseemelk.mockbukkit.MockBukkit;

public class BinCommandHandlerTest extends StandardTest {
    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws IOException {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
        config = PublicBinPlugin.getPluginSetting().getConfig();
        handler = PublicBinPlugin.getCommandHandler();
        
        player1 = new CustomPlayerMock(server, "Player1");
        pa1 = player1.addAttachment(plugin);
        server.addPlayer(player1);
        
        handler.registerSubCommand(new CustomBinSubCommand("Test1"));
    }
    
    
    @Test
    public void testUnknowSubCommand() {
        handler.onCommand(player1, null, "bin", new String[] {"somewhat"});
        assertSaid(player1, "§4Unknown sub-command. Type  \"/bin help\" for a list of commands.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testSubCommandUsageHelp() {
        handler.onCommand(player1, null, "bin", new String[] {"test1"});
        assertSaid(player1, "§4Usage: /bin Test1 <a> <b> <c> [d]");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testSubCommandCatchCommandException() {
        handler.onCommand(player1, null, "bin", "test1 TypeA 2 3".split(" "));
        assertSaid(player1, "§cTest");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testSubCommandCatchUnknownException() {
        handler.onCommand(player1, null, "bin", "test1 TypeB 2 3".split(" "));
        assertSaid(player1, "§4An unknown error occurred. Turn on debug mode to see whats happened.");
        player1.assertNoMoreSaid();
    }
    
    @Test
    public void testSubCommandCatchUnknownExceptionOnDebugMode() {
        config.set("debug", true);
        
        handler.onCommand(player1, null, "bin", "test1 TypeB 2 3".split(" "));
        assertSaid(player1, "§4An unknown error occurred. Turn on debug mode to see whats happened.");
        player1.assertNoMoreSaid();
    }
    

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
