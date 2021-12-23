package org.cozz.tool;

import org.cozz.record.Config;

import java.util.Arrays;
import java.util.Base64;

public class Tools {

    public static boolean startsWith(byte[] array, String prefix) {
        if (array == null || prefix == null) {
            return false;
        }
        if (prefix.length() > array.length) {
            return false;
        }

        for (int i = 0; i < prefix.length(); i++) {
            if (array[i] != prefix.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    public static int indexOf(byte[] array, String prefix) {
        if (array == null || prefix == null) {
            return -1;
        }

        int prefixLen = prefix.length();
        int arrayLen = array.length;

        if (prefixLen > arrayLen) {
            return -1;
        }

        for (int i = 0; i < arrayLen; ++i) {
            int total = 0;
            for (int j = 0; j < prefixLen; ++j) {
                if (array[i + j] == prefix.charAt(j)) {
                    total++;
                    if (total == prefixLen) {
                        return i;
                    }
                } else {
                    break;
                }
            }
        }
        return -1;
    }
}
