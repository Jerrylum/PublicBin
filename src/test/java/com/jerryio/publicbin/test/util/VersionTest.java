package com.jerryio.publicbin.test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jerryio.publicbin.util.Version;

public class VersionTest {
    @Test
    public void newInstance_withTwoDotRelease_isParsedCorrectly() {
        final Version version = new Version("1.26.6");
        assertArrayEquals(version.numbers, new int[]{1, 26, 6});
    }

    @Test
    public void newInstance_withTwoDotReleaseAndPreReleaseName_isParsedCorrectly() {
        final Version version = new Version("1.26.6-DEBUG");
        assertArrayEquals(version.numbers, new int[]{1, 26, 6});
    }

    @Test
    public void compareTo_withEarlierVersion_isGreaterThan() {
        assertEquals(new Version("2.0.0").compareTo(new Version("1.0.0")), 1);
    }

    @Test
    public void compareTo_withSameVersion_isEqual() {
        assertEquals(new Version("2.0.0").compareTo(new Version("2.0.0")), 0);
    }

    @Test
    public void compareTo_withLaterVersion_isLessThan() {
        assertEquals(new Version("1.0.0").compareTo(new Version("2.0.0")), -1);
    }

    @Test
    public void compareTo_withMorePreciseSameVersion_isFalse() {
        assertEquals(new Version("1").compareTo(new Version("1.0.0")), 0);
    }

    @Test
    public void compareTo_withMorePreciseEarlierVersion_isFalse() {
        assertEquals(new Version("2").compareTo(new Version("1.0.0")), 1);
    }

    @Test
    public void compareTo_withMorePreciseLaterVersion_isLessThan() {
        assertEquals(new Version("1").compareTo(new Version("1.0.1")), -1);
    }
}