package com.jerryio.publicbin.util;

import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Checksum {
    public static byte[] createChecksum(InputStream fis) throws Exception {
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String createStringChecksum(InputStream fis) throws Exception {
        byte[] checksum = createChecksum(fis);
        String result = "";

        for (int i = 0; i < checksum.length; i++) {
            result += Integer.toString( ( checksum[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
}
