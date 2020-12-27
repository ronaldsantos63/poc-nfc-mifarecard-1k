package com.ronaldsantos.pocnfcmifareclassic1k.utils;

import com.google.common.primitives.Ints;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Helper {

    public static byte[] convertString2Bytes(String content) {
        byte[] ret = new byte[16];
        byte[] buf = content.getBytes(StandardCharsets.UTF_8);
        int retLen = ret.length;
        int bufLen = buf.length;
        boolean b = retLen > bufLen;

        for (int i = 0; i < retLen; i++) {
            if (b && i >= bufLen) {
                ret[i] = 0;
                continue;
            }
            ret[i] = buf[i];
        }
        return ret;
    }

    public static String convertBytes2String(byte[] data, boolean mShowDataAsHexString) {
        String ret;
        if (mShowDataAsHexString) {
            StringBuilder sb = new StringBuilder();
            for (byte b : data) {
                sb.append(Integer.toHexString((int) b).toUpperCase());
            }
            ret = sb.toString();
        } else {
            int pos = data.length;
            for (int i = data.length - 1; i >= 0; i--) {
                if (data[i] != 0) {
                    break;
                }
                pos = i;
            }
            ret = new String(data, 0, pos, StandardCharsets.UTF_8);
        }
        return ret;
    }

    public static byte[] convertInt2Bytes(int value, int byteSize) {
        return ByteBuffer.allocate(byteSize).putInt(value).array();
    }

    public static int convertBytes2Int(byte[] array) throws IllegalArgumentException {
        return Ints.fromByteArray(array);
    }

    public static float convertBytes2Float(byte[] array) {
        byte[] result = new byte[4];
        System.arraycopy(array, 0, result, 0, 4);
        return ByteBuffer.wrap(result).getFloat();
    }

    public static ArrayList<byte[]> sliceBytes(byte[] array, int size) {

        ArrayList<byte[]> arrays = new ArrayList<>();
        for (int i = 0; i < array.length; i += size) {
            byte[] slice = new byte[4];
            System.arraycopy(array, i, slice, 0, size);
            arrays.add(slice);
        }
        return arrays;
    }

}
