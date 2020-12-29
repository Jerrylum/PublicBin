package com.jerryio.publicbin.test.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bukkit.command.CommandSender;
import org.junit.Test;

import com.jerryio.publicbin.commands.BinSubCommand;
import com.jerryio.publicbin.commands.Colors;
import com.jerryio.publicbin.commands.CommandValidator;
import com.jerryio.publicbin.commands.Strings;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.test.mock.CustomBinSubCommand;
import com.jerryio.publicbin.test.mock.CustomConsoleCommandSenderMock;

public class GeneralCommandTest extends StandardTest {

    @Test
    public void testBinSubCommand() {
        BinSubCommand cmd = new CustomBinSubCommand("Test1", "Test2", "Test3", "Test4");
        
        assertEquals("Test1", cmd.getName());
        
        assertTrue(cmd.isValidTrigger("Test1"));
        assertTrue(cmd.isValidTrigger("Test2"));
        assertTrue(cmd.isValidTrigger("Test3"));
        assertTrue(cmd.isValidTrigger("Test4"));
        assertTrue(cmd.isValidTrigger("test1"));
        assertTrue(cmd.isValidTrigger("tEst2"));
        assertTrue(cmd.isValidTrigger("TEST3"));
        
        assertFalse(cmd.isValidTrigger("Apple"));
        assertFalse(cmd.isValidTrigger("test"));
        assertFalse(cmd.isValidTrigger("Test 1"));
        assertFalse(cmd.isValidTrigger("TEST 1"));
        
        cmd.setPermission(null);
        assertTrue(cmd.hasPermission(null)); // should always return true and ignore the sender
        
        CommandSender sender = new CustomConsoleCommandSenderMock();
        cmd.setPermission("something");
        assertTrue(cmd.hasPermission(sender)); 
    }
    
    @Test
    public void testNewColorsInstance() {
        new Colors();
    }
    
    @Test
    public void testNewCommandValidatorInstance() {
        new CommandValidator();
    }
    
    @Test
    public void testNewStringsInstance() {
        new Strings();
    }
}
