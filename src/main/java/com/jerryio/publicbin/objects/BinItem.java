package com.jerryio.publicbin.objects;

import java.util.Date;

import org.bukkit.inventory.ItemStack;

import com.jerryio.publicbin.enums.OrderEnum;

public class BinItem {
    public ItemStack item;
    public long placedTime;
    public int slot;
//    public HashMap<OrderEnum, Integer> transcript;

    public BinItem(ItemStack item, int slot) {
        this.item = item;
        this.placedTime = new Date().getTime();
        this.slot = slot;
    }

    public int compareTo(BinItem target, OrderEnum[] guideline) {
        for (int i = 0; i < guideline.length; i++) {
            int score = guideline[i].getComparision().run(this, target);
            if (score != 0)
                return score;
        }
        return 0;
    }
}
