package com.jerryio.publicbin.test.disk;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.AutoUpdateYamlConfigHandler;
import com.jerryio.publicbin.disk.CommentYamlConfiguration;
import com.jerryio.publicbin.test.StandardTest;

import be.seeseemelk.mockbukkit.MockBukkit;

public class CommentYamlConfigurationTest extends StandardTest {
    @Before
    public void setUp() throws IOException {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
    @Test
    public void testYamlPatchUpdateAChangedFile() throws Exception {
        String originalName = "real-config-original.yml";
        File originalFile = new File(plugin.getDataFolder(), originalName);
        
        String newName = "real-config-patch.yml";
        File patchedFile = new File(plugin.getDataFolder(), newName);
        
        CommentYamlConfiguration config;
        
        config = (CommentYamlConfiguration)AutoUpdateYamlConfigHandler.loadYaml(plugin, originalName);
        assertEquals(getResource(originalName)  + "\n", config.saveToString());
        
        FileUtils.copyFile(originalFile, patchedFile);
        
        config = (CommentYamlConfiguration)AutoUpdateYamlConfigHandler.loadYaml(plugin, newName);
        assertEquals(getResource("real-config-aftersave.yml"), config.saveToString());
    }
    
    @Test
    public void testYamlSaveWithoutModify() throws Exception {
        String name = "test-comment-config.yml";
        File configFile = new File(plugin.getDataFolder(), name);
        
        CommentYamlConfiguration config;
        
        config = (CommentYamlConfiguration)AutoUpdateYamlConfigHandler.loadYaml(plugin, name);
        assertValue(config);
        assertCommentsMap(config);
        
        assertEquals(getResource("test-comment-config-aftersave.yml"), config.saveToString());
        config.save(configFile);
        
        config = (CommentYamlConfiguration)AutoUpdateYamlConfigHandler.loadYaml(plugin, name);
        assertValue(config);
        
        assertEquals(getResource("test-comment-config-aftersave.yml"), config.saveToString());
        config.save(configFile);
        
        config = (CommentYamlConfiguration)AutoUpdateYamlConfigHandler.loadYaml(plugin, name);
        assertValue(config);
        
        assertEquals(getResource("test-comment-config-aftersave.yml"), config.saveToString());
    }
    
    public void assertValue(CommentYamlConfiguration config) {
        assertEquals("Oz-Ware Purchase Invoice", config.getString("receipt"));
        assertEquals("Dorothy", config.getString("customer.given"));
        
        assertEquals(2, config.getMapList("items").size());
        Map<?, ?> item0 = (Map<?, ?>) config.getMapList("items").get(0);
        assertEquals("A4786", item0.get("part_no"));
        assertEquals(1.47, item0.get("price"));
        
        assertEquals(config.getString("bill-to.street"), config.getString("ship-to.street"));
    }
    
    public void assertCommentsMap(CommentYamlConfiguration config) {
        String msg = commentsMapToString(config);
        
        assertEquals("------\n"
                + ">>>###### Start Comment<<<\n"
                + ">>><<<\n"
                + "@receipt:0\n"
                + "@items:0.- part_no:1.size:0\n"
                + ">>>    # From<<<\n"
                + "@customer:0.age:0.from:0\n"
                + ">>>    # Q = 1<<<\n"
                + "@items:0.- part_no:1.quantity:0\n"
                + ">>><<<\n"
                + "@specialDelivery:0\n"
                + ">>>    # To<<<\n"
                + "@customer:0.age:0.to:0\n"
                + ">>>  # Family Name, Last Name, 姓<<<\n"
                + "@customer:0.family:0\n"
                + ">>>  # Given Name, First Name, 名<<<\n"
                + "@customer:0.given:0\n"
                + ">>><<<\n"
                + ">>># All items<<<\n"
                + "@items:0\n"
                + "@date:0\n"
                + "@customer:0\n"
                + ">>><<<\n"
                + ">>>###### End Comment<<<\n"
                + "@:ENDLINE:\n"
                + ">>>    # Q = 4<<<\n"
                + "@items:0.- part_no:0.quantity:0\n"
                + ">>>  # State short name<<<\n"
                + "@bill-to:0.state:0\n"
                + ">>><<<\n"
                + "@ship-to:0\n"
                + "@items:0.- part_no:0.descrip:0\n"
                + "@customer:0.age:0\n"
                + ">>><<<\n"
                + "@bill-to:0\n"
                + ">>>  # Range: earth<<<\n"
                + ">>>  # For example: New York<<<\n"
                + "@bill-to:0.city:0\n"
                + "@items:0.- part_no:1.price:0\n"
                + ">>><<<\n"
                + ">>>    # Index 1<<<\n"
                + "@items:0.- part_no:1\n"
                + ">>>  # Index 0<<<\n"
                + "@items:0.- part_no:0\n"
                + "@items:0.- part_no:0.price:0\n"
                + ">>>  # The address <<<\n"
                + "@bill-to:0.street:0\n"
                + "@items:0.- part_no:1.descrip:0\n"
                + "------", msg);
        
        System.out.println(msg);
    }
    
    public String commentsMapToString(CommentYamlConfiguration config) {
        String msg = "------\n";
        for (Map.Entry<String, List<String>> entry : config.comments.entrySet()) {
            for (String str : entry.getValue())
                msg += ">>>" + str + "<<<\n";
            msg += "@" + entry.getKey() + "\n";
        }
        msg += "------";
        
        return msg;
    }
    
    public String getResource(String name) {
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(plugin.getResource(name), writer);
            return writer.toString();
        } catch (IOException e) {
            return null;
        }
    }
    
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
