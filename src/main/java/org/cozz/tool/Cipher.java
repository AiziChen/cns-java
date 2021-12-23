package org.cozz.tool;

import org.cozz.record.Config;

import java.util.Base64;

public class Cipher {

    public static String decryptHost(String host) {
        byte[] decodeHost = Base64.getDecoder().decode(host);
        xorCrypt(decodeHost, 0);
        return new String(decodeHost, 0, decodeHost.length - 1);
    }

    private static int xorCrypt(byte[] data, int passwordSub) {
        int dataLen = data.length;
        if (dataLen <= 0) {
            return passwordSub;
        }
        int passLen = Config.password.length();
        int pi = passwordSub;
        for (int dataSub = 0; dataSub < dataLen; dataSub++) {
            pi = (dataSub + passwordSub) % passLen;
            data[dataSub] ^= (byte) Config.password.charAt(pi) | (byte) pi;
        }
        return pi + 1;
    }
}
