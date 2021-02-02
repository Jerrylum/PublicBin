package com.jerryio.publicbin.test.disk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.AutoUpdateYamlConfigHandler;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.test.StandardTest;
import com.jerryio.publicbin.util.MD5Checksum;

import be.seeseemelk.mockbukkit.MockBukkit;

public class AutoUpdateYamlConfigHandlerTest extends StandardTest {

    @Before
    public void setUp() throws IOException {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
    @Test
    public void testNewAutoUpdateYamlConfig() {
        new AutoUpdateYamlConfigHandler();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLoadUnknowConfigFile() {
        assertNull(AutoUpdateYamlConfigHandler.loadYaml(plugin, "this-is-an-unknow-config-file.yml"));
    }
    
    @Test
    public void testDoubleLoad() {       
        assertNotNull(AutoUpdateYamlConfigHandler.loadYaml(plugin, "config.yml"));
        assertNotNull(AutoUpdateYamlConfigHandler.loadYaml(plugin, "config.yml"));
    }
    
    @Test
    public void testInvalidFile() {
        try {
            FileWriter myWriter = new FileWriter(new File(plugin.getDataFolder(), "config.yml"));
            myWriter.write("Invalid File");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            throw new RuntimeException(e);
        }
        
        assertNull(AutoUpdateYamlConfigHandler.loadYaml(plugin, "config.yml"));
    }
    
    @Test
    public void testLockedFile() throws Exception {
        AutoUpdateYamlConfigHandler.loadYaml(plugin, "config.yml");
        
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(plugin.getDataFolder(), "config.yml"));
            FileChannel channel = fileOutputStream.getChannel();
            FileLock lock = channel.lock()) { 

            // CommentYamlConfiguration will reject any blank config file and return null
            assertNull(AutoUpdateYamlConfigHandler.loadYaml(plugin, "config.yml"));
            
            lock.close();
        }        
    }
    
    @Test
    public void testBackupFilePath() throws Exception {
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);
        
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        
        File file0 = AutoUpdateYamlConfigHandler.getNonExistsBackupFile(configFile);
        assertTrue(file0.getAbsolutePath().startsWith(plugin.getDataFolder().getAbsolutePath()));
        assertEquals("backup " + strDate + " config.yml", file0.getName());
        file0.getParentFile().mkdir();
        new FileOutputStream(file0).close();
        
        File file1 = AutoUpdateYamlConfigHandler.getNonExistsBackupFile(configFile);
        assertTrue(file1.getAbsolutePath().startsWith(plugin.getDataFolder().getAbsolutePath()));
        assertEquals("backup " + strDate + " (1) config.yml", file1.getName());
        new FileOutputStream(file1).close();
        
        File file2 = AutoUpdateYamlConfigHandler.getNonExistsBackupFile(configFile);
        assertTrue(file2.getAbsolutePath().startsWith(plugin.getDataFolder().getAbsolutePath()));
        assertEquals("backup " + strDate + " (2) config.yml", file2.getName());
    }
    
    @Test
    public void testBackupFile() throws Exception {
        File original = new File(plugin.getDataFolder(), "test.txt");

        FileWriter myWriter = new FileWriter(original);
        myWriter.write("A utf-8 file\n這是一個包含中文字的檔案\r\n这是一个包含中文字的档案");
        myWriter.close();
        
        File backup = AutoUpdateYamlConfigHandler.backupFile(original);
        
        assertTrue(FileUtils.contentEquals(original, backup));
    }
    
    @Test
    public void testUpdateFile() throws Exception {
        File original = new File(plugin.getDataFolder(), "test-config.yml");

        FileWriter myWriter = new FileWriter(original);
        myWriter.write("version: 4.5.5");
        myWriter.close();
        
        YamlConfiguration config = AutoUpdateYamlConfigHandler.loadYaml(plugin, "test-config.yml");
        
        assertEquals("4.5.6", config.getString("version"));
    }
    
    @Test
    public void testUpdateNotSupportedFile() throws Exception {
        File original = new File(plugin.getDataFolder(), "not-a-resource.yml");

        FileWriter myWriter = new FileWriter(original);
        myWriter.write("version: 1.2.3");
        myWriter.close();
        
        YamlConfiguration config = AutoUpdateYamlConfigHandler.loadYaml(plugin, "not-a-resource.yml");
        
        assertEquals("1.2.3", config.getString("version"));
    }
    
    @Test
    public void testLazyUpdate() throws Exception {
        testVersion("1.0.0");
    }
    
    private void testVersion(String ver) throws Exception {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        
        Files.copy(plugin.getResource("config-" + ver + ".yml"), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        AutoUpdateYamlConfigHandler.loadYaml(plugin, "config.yml", PluginSetting.OLD_CONFIG_MD5_CHECKSUMS);
        
        String expected = MD5Checksum.createStringChecksum(plugin.getResource("config.yml"));
        String actual = MD5Checksum.createStringChecksum(new FileInputStream(configFile));
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLazyUpdateWithoutMD5() throws Exception {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        
        Files.copy(plugin.getResource("config-1.0.0.yml"), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        AutoUpdateYamlConfigHandler.loadYaml(plugin, "config.yml", new String[] {"does not match"});
        
        String expected = MD5Checksum.createStringChecksum(plugin.getResource("config.yml"));
        String actual = MD5Checksum.createStringChecksum(new FileInputStream(configFile));
        
        assertNotEquals(expected, actual);
    }
    
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
