package com.creditease.honeybot.utils;

/**
 * @ Author     ：TangXD
 * @ Date       ：Created in 10:24 2018/10/08
 * @ Description：字节数组相关工具类
 * @ Modified By：
 */
public class ByteUtils {



    /**
     * 数组反转
     * @param a
     * @return
     */
    public static byte[] byteToByte(byte[] a){
        if( a.length ==4 ){
            byte[] b = new byte[4];
            b[0] = a[3];
            b[1] = a[2];
            b[2] = a[1];
            b[3] = a[0];
            return b;
        }
        return null ;
    }




    /**
     * 字节数组转short
     *
     * @param b
     * @return short
     */
    public static short byteToShort(byte[] b) {
        return (short) ((((b[0] & 0xff) << 8) | b[1] & 0xff));
    }

    /**
     * 字节数组转int
     *
     * @param b
     * @return int
     */
    public static int byteToInt(byte[] b) {
        return ((((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff)));
    }

    /**
     * 字节数组转long
     *
     * @param b
     * @return long
     */
    public static long byteToLong(byte[] b) {
        return ((((long) b[0] & 0xff) << 56) | (((long) b[1] & 0xff) << 48) | (((long) b[2] & 0xff) << 40) | (((long) b[3] & 0xff) << 32) | (((long) b[4] & 0xff) << 24)
                | (((long) b[5] & 0xff) << 16) | (((long) b[6] & 0xff) << 8) | ((long) b[7] & 0xff));
    }


    /**
     * short转字节数组
     *
     * @param number short
     * @return 字节数组
     */
    public static byte[] shortToByte(short number) {
        byte[] b = new byte[2];
        for (int i = 1; i >= 0; i--) {
            b[i] = (byte) (number % 256);
            number >>= 8;
        }
        return b;
    }


    /**
     * int转字节数组
     *
     * @param number int
     * @return 字节数组
     */
    public static byte[] intToByte(int number) {
        byte[] b = new byte[4];
        for (int i = 3; i >= 0; i--) {
            b[i] = (byte) (number % 256);
            number >>= 8;
        }
        return b;
    }

    /**
     * long转字节数组
     *
     * @param number long
     * @return 字节数组
     */
    public static byte[] longToByte(long number) {
        byte[] b = new byte[8];
        for (int i = 7; i >= 0; i--) {
            b[i] = (byte) (number % 256);
            number >>= 8;
        }
        return b;
    }



}



