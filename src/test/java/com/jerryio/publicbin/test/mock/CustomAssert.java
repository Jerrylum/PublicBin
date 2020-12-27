package com.jerryio.publicbin.test.mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import be.seeseemelk.mockbukkit.command.MessageTarget;

public class CustomAssert {
    static public void assertSaid(MessageTarget target, String expected) {
        String message = target.nextMessage();
        if (message == null) {
            fail("No more messages were sent");
        } else {
            if (message.contains(expected) == false)
                assertEquals(expected, message);
        }
    }
}
