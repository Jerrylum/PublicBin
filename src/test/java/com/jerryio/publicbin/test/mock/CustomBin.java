package com.jerryio.publicbin.test.mock;

import org.bukkit.event.inventory.InventoryType;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.objects.Bin;

public class CustomBin extends Bin {

    public CustomBin() {
        super(new CustomeInventoryMock(null, 9 * PublicBinPlugin.getPluginSetting().getBinRow(), InventoryType.CHEST));
    }
    
    public void cancelTask() {
        scheduledUpdateTask.cancel();
    }

    public void addNullToItemList() {
        binItemList.add(null);
    }
    
    public void newInventory() {
        inventory = new CustomeInventoryMock(null, 9 * 3, InventoryType.CHEST);
    }

}
