package com.jerryio.publicbin.test.disk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.BasicYamlConfig;
import com.jerryio.publicbin.test.StandardTest;

import be.seeseemelk.mockbukkit.MockBukkit;

public class BasicYamlConfigTest extends StandardTest {

    @Before
    public void setUp() throws IOException {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
    @Test
    public void testNewBasicYamlConfig() {
        new BasicYamlConfig();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLoadUnknowConfigFile() {       
        BasicYamlConfig.loadYaml(plugin, "this-is-an-unknow-config-file.yml");
    }
    
    @Test
    public void testDoubleLoad() {       
        BasicYamlConfig.loadYaml(plugin, "config.yml");
        BasicYamlConfig.loadYaml(plugin, "config.yml");
    }
    
    @Test
    public void testInvalidFile() {
        try {
            FileWriter myWriter = new FileWriter(new File(plugin.getDataFolder(), "config.yml"));
            myWriter.write("Invalid File");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        BasicYamlConfig.loadYaml(plugin, "config.yml");
    }
    
    @Test
    public void testLockedFile() throws Exception {
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(plugin.getDataFolder(), "config.yml"));
            FileChannel channel = fileOutputStream.getChannel();
            FileLock lock = channel.lock()) { 

            BasicYamlConfig.loadYaml(plugin, "config.yml");
            
            lock.close();
        }        
    }
    
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
