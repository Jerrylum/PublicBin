package com.jerryio.publicbin.test;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.PermissionAttachment;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinCommandHandler;
import com.jerryio.publicbin.objects.BinManager;
import com.jerryio.publicbin.test.mock.CustomConsoleCommandSenderMock;
import com.jerryio.publicbin.test.mock.CustomPlayerMock;

import be.seeseemelk.mockbukkit.ServerMock;

public abstract class StandardTest {
    protected ServerMock server;
    protected PublicBinPlugin plugin;
    protected YamlConfiguration config;
    protected BinCommandHandler handler;
    protected BinManager manager;
    
    
    protected CustomPlayerMock player1;
    protected PermissionAttachment pa1;
    protected CustomConsoleCommandSenderMock console;
}
