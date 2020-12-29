package com.jerryio.publicbin.test.mock;

import org.bukkit.inventory.ItemStack;

public class CustomItemStack extends ItemStack {
    private boolean mockHasItemMeta = false;
    
    public CustomItemStack(boolean b) {
        mockHasItemMeta = b;
    }

    @Override
    public boolean hasItemMeta() {
        return mockHasItemMeta;
    }
}
