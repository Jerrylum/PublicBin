package com.jerryio.publicbin.test.mock;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.ItemEntityMock;

public class CustomItemEntityMock extends ItemEntityMock {

    private boolean dead;
    
    public CustomItemEntityMock(@NotNull ServerMock server, @NotNull UUID uuid, @NotNull ItemStack item) {
        super(server, uuid, item);
    }
    
    public void setDead(boolean dead) {
        this.dead = dead;
    }
    
    @Override
    public boolean isDead() {
        return dead;
    }
}
