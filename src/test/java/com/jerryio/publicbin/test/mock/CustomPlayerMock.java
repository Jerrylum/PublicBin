package com.jerryio.publicbin.test.mock;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

public class CustomPlayerMock extends PlayerMock {

    private String locale = "en_us";
    
    public CustomPlayerMock(ServerMock server, String name) {
        super(server, name);
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
    
    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {
        return;
    }
    
    class MockSpigot extends org.bukkit.entity.Player.Spigot {

        @Override
        public void sendMessage(@NotNull net.md_5.bungee.api.chat.BaseComponent... component) {
            return;
        }
        
        @Override
        public void sendMessage(@NotNull net.md_5.bungee.api.ChatMessageType position, @NotNull net.md_5.bungee.api.chat.BaseComponent... components) {
            return;
        }
    }
}
