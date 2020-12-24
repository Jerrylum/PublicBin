package com.jerryio.publicbin.test;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.UnimplementedOperationException;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

public class CustomPlayerMock extends PlayerMock {

    private String locale = "en_us";
    
    public CustomPlayerMock(ServerMock server, String name) {
        super(server, name);
    }
    
    public CustomPlayerMock(ServerMock server, String name, UUID uuid) {
        super(server, name, uuid);
    }

    @Override
    public String getLocale() {
        return locale;
    }
    
    public void setLocale(String str) {
        locale = str;
    }
    
    @Override
    public org.bukkit.entity.Player.Spigot spigot() {
        return new MockSpigot();
    }
    
    class MockSpigot extends org.bukkit.entity.Player.Spigot {
        @Override
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent component) {
            return;
        }
        
        @Override
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent... component) {
            return;
        }
    }
}
