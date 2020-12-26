package com.jerryio.publicbin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bukkit.command.CommandException;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinCommandHandler;
import com.jerryio.publicbin.commands.CommandValidator;
import com.jerryio.publicbin.objects.BinManager;
import com.jerryio.publicbin.test.mock.CustomConsoleCommandSenderMock;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

public class CommandValidatorTest {
    private ServerMock server;
    private PublicBinPlugin plugin;
    private BinCommandHandler handler;
    private BinManager manager;
    
    private CustomPlayerMock player1;
    private PermissionAttachment pa1;
    private CustomConsoleCommandSenderMock console;

    @Before
    public void setUp() {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
    @Test(expected = CommandException.class)
    public void testMethodNotNull1() {
        CommandValidator.notNull(null, "Test");
    }
    
    @Test
    public void testMethodNotNull2() {
        CommandValidator.notNull(0, "Test");
    }
    
    @Test(expected = CommandException.class)
    public void testMethodIsTrue1() {
        CommandValidator.isTrue(false, "Test");
    }
    
    @Test
    public void testMethodIsTrue2() {
        CommandValidator.isTrue(true, "Test");
    }
    
    @Test(expected = CommandException.class)
    public void testMethodGetInteger1() {
        CommandValidator.getInteger("Test");
    }
    
    @Test
    public void testMethodGetInteger2() {
        assertEquals(123, CommandValidator.getInteger("123"));
    }
    
    @Test
    public void testMethodIsInteger1() {
        assertFalse(CommandValidator.isInteger("abc"));
    }
    
    @Test
    public void testMethodIsInteger2() {
        assertTrue(CommandValidator.isInteger("123"));
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
