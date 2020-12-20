package com.jerryio.publicbin.objects;

import java.util.Date;

import org.bukkit.inventory.ItemStack;

public class BinItem {
    public ItemStack item;
    public long placedTime;

    public BinItem(ItemStack item) {
        this.item = item;
        this.placedTime = new Date().getTime();
    }
}
