package com.jerryio.publicbin.test;

import static org.junit.Assert.assertEquals;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.enums.OrderEnum;
import com.jerryio.publicbin.objects.BinItem;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

public class ComparisonTest {
    private ServerMock server;
    private PublicBinPlugin plugin;

    @Before
    public void setUp() {
        System.setProperty("MockTest", "true");

        server = MockBukkit.mock();
        plugin = (PublicBinPlugin) MockBukkit.load(PublicBinPlugin.class);
    }
    
    @Test
    public void testTypeComparisonWithName1() {
        BinItem a = new BinItem(new ItemStack(Material.PINK_BED), 0);
        BinItem b = new BinItem(new ItemStack(Material.RED_BED), 0);
        
        int result = OrderEnum.Type.getComparision().run(a, b);
        // BED == BED; PINK < RED; so PINK_BED < RED_BED
        assertEquals(-1, result);
    }
    
    @Test
    public void testTypeComparisonWithName2() {
        BinItem a = new BinItem(new ItemStack(Material.WHITE_BED), 0);
        BinItem b = new BinItem(new ItemStack(Material.RED_BED), 0);
        
        int result = OrderEnum.Type.getComparision().run(a, b);
        // BED == BED; WHITE > RED; so WHITE_BED > RED_BED
        assertEquals(1, result);
    }
    
    @Test
    public void testTypeComparisonWithName3() {
        BinItem a = new BinItem(new ItemStack(Material.YELLOW_BED), 0);
        BinItem b = new BinItem(new ItemStack(Material.YELLOW_BED), 0);
        
        int result = OrderEnum.Type.getComparision().run(a, b);
        // BED == BED; YELLOW == YELLOW, so equals
        assertEquals(0, result);
    }
    
    @Test
    public void testTypeComparisonWithName4() {
        BinItem a = new BinItem(new ItemStack(Material.LIGHT_BLUE_BED), 0);
        BinItem b = new BinItem(new ItemStack(Material.BLUE_BED), 0);
        
        int result = OrderEnum.Type.getComparision().run(a, b);
        // BED == BED; BLUE == BLUE, a has longer names, so LIGHT_BLUE_BED > YELLOW_BED
        assertEquals(1, result);
    }
    
    @Test
    public void testTypeComparisonWithName5() {
        BinItem a = new BinItem(new ItemStack(Material.BLUE_BANNER), 0);
        BinItem b = new BinItem(new ItemStack(Material.BLUE_BED), 0);
        
        int result = OrderEnum.Type.getComparision().run(a, b);
        // BED == BED; BANNER < BED, so BLUE_BANNER < BLUE_BED
        assertEquals(-1, result);
    }
    
    @Test
    public void testTypeComparisonWithName6() {
        BinItem a = new BinItem(new ItemStack(Material.STONECUTTER), 0);
        BinItem b = new BinItem(new ItemStack(Material.END_STONE), 0);
        
        int result = OrderEnum.Type.getComparision().run(a, b);
        // STONECUTTER > STONE; so STONECUTTER > END_STONE
        assertEquals(1, result);
    }
    
    @Test
    public void testMetadataComparison1() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        
        ItemMeta meta = a.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, true);
        a.item.setItemMeta(meta);
        
        meta = b.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, true);
        b.item.setItemMeta(meta);
        
        int result = OrderEnum.Metadata.getComparision().run(a, b);
        assertEquals(0, result);
    }
    
    @Test
    public void testMetadataComparison2() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        
        ItemMeta meta = a.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addEnchant(Enchantment.MENDING, 0, true);
        a.item.setItemMeta(meta);
        
        meta = b.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, true);
        b.item.setItemMeta(meta);
        
        int result = OrderEnum.Metadata.getComparision().run(a, b);
        assertEquals(1, result);
    }
    
    @Test
    public void testMetadataComparison3() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        
        ItemMeta meta = a.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, true);
        a.item.setItemMeta(meta);
        
        meta = b.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addEnchant(Enchantment.MENDING, 0, true);
        b.item.setItemMeta(meta);
        
        int result = OrderEnum.Metadata.getComparision().run(a, b);
        assertEquals(-1, result);
    }
    
    @Test
    public void testMetadataComparison4() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        
        ItemMeta meta = a.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, true);
        a.item.setItemMeta(meta);
                
        int result = OrderEnum.Metadata.getComparision().run(a, b);
        assertEquals(1, result);
    }
    
    @Test
    public void testMetadataComparison5() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        
        ItemMeta meta = b.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, true);
        b.item.setItemMeta(meta);
        
        int result = OrderEnum.Metadata.getComparision().run(a, b);
        assertEquals(-1, result);
    }
    
    @Test
    public void testMetadataComparison6() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        
        int result = OrderEnum.Metadata.getComparision().run(a, b);
        assertEquals(0, result);
    }
    
    @Test
    public void testMetadataComparison7() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        
        ItemMeta meta = a.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 3, true);
        a.item.setItemMeta(meta);
        
        meta = b.item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 4, true);
        b.item.setItemMeta(meta);
        
        int result = OrderEnum.Metadata.getComparision().run(a, b);
        assertEquals(0, result);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testDurabilityComparison1() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE, 1, (short) 100), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE, 1, (short) 100), 0);
        
        int result = OrderEnum.Durability.getComparision().run(a, b);
        assertEquals(0, result);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testDurabilityComparison2() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE, 1, (short) 200), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE, 1, (short) 100), 0);
        
        int result = OrderEnum.Durability.getComparision().run(a, b);
        assertEquals(1, result);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testDurabilityComparison3() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE, 1, (short) 100), 0);
        BinItem b = new BinItem(new ItemStack(Material.WOODEN_AXE, 1, (short) 200), 0);
        
        int result = OrderEnum.Durability.getComparision().run(a, b);
        assertEquals(-1, result);
    }
    
    @Test
    public void testAmountComparison1() {
        BinItem a = new BinItem(new ItemStack(Material.WOLF_SPAWN_EGG, 20), 0);
        BinItem b = new BinItem(new ItemStack(Material.CAT_SPAWN_EGG, 20), 0);

        int result = OrderEnum.Amount.getComparision().run(a, b);
        assertEquals(0, result);
    }
    
    @Test
    public void testAmountComparison2() {
        BinItem a = new BinItem(new ItemStack(Material.WOLF_SPAWN_EGG, 30), 0);
        BinItem b = new BinItem(new ItemStack(Material.CAT_SPAWN_EGG, 20), 0);

        int result = OrderEnum.Amount.getComparision().run(a, b);
        assertEquals(1, result);
    }
    
    @Test
    public void testAmountComparison3() {
        BinItem a = new BinItem(new ItemStack(Material.WOLF_SPAWN_EGG, 20), 0);
        BinItem b = new BinItem(new ItemStack(Material.CAT_SPAWN_EGG, 30), 0);

        int result = OrderEnum.Amount.getComparision().run(a, b);
        assertEquals(-1, result);
    }
    
    @Test
    public void testTimeComparison1() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.CAT_SPAWN_EGG), 0);
        
        a.placedTime = 0;
        b.placedTime = 0;
        
        int result = OrderEnum.Time.getComparision().run(a, b);
        assertEquals(0, result);
    }
    
    @Test
    public void testTimeComparison2() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.CAT_SPAWN_EGG), 0);
        
        a.placedTime = 2;
        b.placedTime = 0;
        
        int result = OrderEnum.Time.getComparision().run(a, b);
        assertEquals(1, result);
    }
    
    @Test
    public void testTimeComparison3() {
        BinItem a = new BinItem(new ItemStack(Material.WOODEN_AXE), 0);
        BinItem b = new BinItem(new ItemStack(Material.CAT_SPAWN_EGG), 0);
        
        a.placedTime = 0;
        b.placedTime = 2;
        
        int result = OrderEnum.Time.getComparision().run(a, b);
        assertEquals(-1, result);
    }


    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
