package com.jerryio.publicbin.test.mock;

import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;

public class CustomConsoleCommandSenderMock extends ConsoleCommandSenderMock {
    @Override
    public boolean hasPermission(String str) {
        return true;
    }

}
