package com.jerryio.publicbin.objects;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.util.DateTime;

public class CountdownDespawnStrategy extends Strategy {
    private int cacheKeepingTime;
    
    public CountdownDespawnStrategy(BinManager manager) {
        super(manager);
        

        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        cacheKeepingTime = setting.getKeepingTime() * 1000;
    }
    
    @Override
    public void tickCheck() {
        long now = DateTime.getTimestamp();

        for (Bin bin : manager.getAllBin()) {
            if (now < bin.requestCheckTime)
                continue;

            bin.timeUpdate(now, cacheKeepingTime);
        }
    }

}
