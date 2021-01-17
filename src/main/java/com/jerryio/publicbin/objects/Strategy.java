package com.jerryio.publicbin.objects;

public abstract class Strategy {
    protected BinManager manager;
    
    public Strategy(BinManager manager) {
        this.manager = manager;
    }
    
    public abstract void tickCheck();
}
