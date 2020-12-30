package com.jerryio.publicbin.test.mock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import be.seeseemelk.mockbukkit.inventory.InventoryMock;

public class CustomeInventoryMock extends InventoryMock {

    public List<HumanEntity> allViewer = new ArrayList<HumanEntity>();
    
    public CustomeInventoryMock(InventoryHolder holder, int size, InventoryType type) {
        super(holder, size, type);
    }
    
    @Override
    public List<HumanEntity> getViewers()
    {
        return allViewer;
    }

}
