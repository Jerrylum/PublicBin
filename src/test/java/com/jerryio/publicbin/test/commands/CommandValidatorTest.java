package com.jerryio.publicbin.test.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bukkit.command.CommandException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.CommandValidator;
import com.jerryio.publicbin.test.StandardTest;

import be.seeseemelk.mockbukkit.MockBukkit;

public class CommandValidatorTest extends StandardTest {

    @Before
    public void setUp() {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
    @Test(expected = CommandException.class)
    public void testMethodNotNullException() {
        CommandValidator.notNull(null, "Test");
    }
    
    @Test
    public void testMethodNotNullVaild() {
        CommandValidator.notNull(0, "Test");
    }
    
    @Test(expected = CommandException.class)
    public void testMethodIsTrueException() {
        CommandValidator.isTrue(false, "Test");
    }
    
    @Test
    public void testMethodIsTrueVaild() {
        CommandValidator.isTrue(true, "Test");
    }
    
    @Test(expected = CommandException.class)
    public void testMethodGetIntegerException() {
        CommandValidator.getInteger("Test");
    }
    
    @Test
    public void testMethodGetIntegerVaild() {
        assertEquals(123, CommandValidator.getInteger("123"));
    }
    
    @Test
    public void testMethodIsIntegerFalse() {
        assertFalse(CommandValidator.isInteger("abc"));
    }
    
    @Test
    public void testMethodIsIntegerTrue() {
        assertTrue(CommandValidator.isInteger("123"));
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
