package com.jerryio.publicbin.test.objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import com.jerryio.publicbin.test.AbstractInventoryTest;
import com.jerryio.publicbin.test.mock.CustomItemEntityMock;

public class CollectDespawnTest extends AbstractInventoryTest {

    @Override
    public void configSetting() {
        config.set("countdown-despawn.enable", false);
        config.set("clear-intervals.enable", false);
        config.set("remove-when-full.enable", false);
        config.set("smart-grouping.enable", false);
        config.set("collect-despawn.enable", true);
    }

    @Test
    public void testNormalCollect() {
        server.getScheduler().performTicks(20);
        
        ItemStack a = new ItemStack(Material.STICK, 1);
        despawnItem(a);
        
        ItemStack b = new ItemStack(Material.STICK, 2);
        CustomItemEntityMock bi = damageItem(b);
        bi.setDead(false);
        
        ItemStack c = new ItemStack(Material.STICK, 3);
        despawnItem(c);
        
        ItemStack d = new ItemStack(Material.STICK, 4);
        damageItem(d);
        
        
        server.getScheduler().performTicks(20);
        
        ItemStack[] raw = new ItemStack[9 * 2];
        raw[0] = a;
        raw[1] = c;
        raw[2] = d;
        
        assertItemStackArray(raw, inv.getContents());
        
        
        
        bi.setDead(true);
        
        server.getScheduler().performTicks(20);
        
        raw[3] = b;
        
        assertItemStackArray(raw, inv.getContents());
    }
    
    @Test
    public void testUntrack() {
        server.getScheduler().performTicks(20);
        
        ItemStack a = new ItemStack(Material.STICK, 1);
        
        pickup(despawnItem(a));
        
        ItemStack[] raw = new ItemStack[9 * 2];
        
        assertItemStackArray(raw, inv.getContents());

        
        assertItemStackArray(raw, inv.getContents());
    }
    
    @Test
    public void testFullInventory() {
        server.getScheduler().performTicks(20);
        

        ItemStack a = new ItemStack(Material.STICK, 1);
        ItemStack[] raw = new ItemStack[9 * 2];
        
        for (int i = 0; i < 9 * 2; i ++) {
            despawnItem(a);
            raw[i] = a;
        }
        
        for (int i = 0; i < 10; i ++) {
            despawnItem(a);
        }

        server.getScheduler().performTicks(20);
        
        assertItemStackArray(raw, inv.getContents());

        server.getScheduler().performTicks(20);
        
        assertItemStackArray(raw, inv.getContents());
        
        // right now, the tracking list cleared
        
        inv.clear();
        
        server.getScheduler().performTicks(20);
        
        // the inventory is empty but nothing put in
        
        assertItemStackArray(new ItemStack[9 * 2], inv.getContents());

    }
    
    @Test
    public void testSpawnZombie() {
        Entity e = player1.getWorld().spawnEntity(player1.getLocation(), EntityType.ZOMBIE);
        
        Bukkit.getPluginManager().callEvent(new EntityDamageEvent(e, EntityDamageEvent.DamageCause.FIRE, 1));
    }
    
    public CustomItemEntityMock despawnItem(ItemStack is) {
        CustomItemEntityMock entity = new CustomItemEntityMock(server, UUID.randomUUID(), is);
        entity.setLocation(player1.getLocation());
        server.registerEntity(entity);
        
        Bukkit.getPluginManager().callEvent(new ItemDespawnEvent(entity, player1.getLocation()));
        
        entity.setDead(true);
        return entity;
    }
    
    public CustomItemEntityMock damageItem(ItemStack is) {
        CustomItemEntityMock entity = new CustomItemEntityMock(server, UUID.randomUUID(), is);
        entity.setLocation(player1.getLocation());
        server.registerEntity(entity);
        
        Bukkit.getPluginManager().callEvent(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.FIRE, 1));
        Bukkit.getPluginManager().callEvent(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.FIRE, 1));
        Bukkit.getPluginManager().callEvent(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.FIRE, 1));
        Bukkit.getPluginManager().callEvent(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.FIRE, 1));
        
        entity.setDead(true);
        return entity;
    }
    
    public void pickup(Item item) {
        Bukkit.getPluginManager().callEvent(new EntityPickupItemEvent(player1, item, 0));
    }
}
