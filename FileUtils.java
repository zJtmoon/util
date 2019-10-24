package com.creditease.honeybot.utils;

import java.io.*;

/**
 * @ Author     ：TangXD
 * @ Date       ：Created in 10:24 2018/10/08
 * @ Description：${description}
 * @ Modified By：
 */
public class FileUtils {

    /**
     * InputStream to byte[]
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static final byte[] input2bytes(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[2048];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 2048)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }


    /**
     * 将文件转换成Byte数组
     *
     * @param file
     * @return
     */
    public static byte[] getBytesByFile(File file) {

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
            byte[] b = new byte[2048];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Byte数组转换成文件
     *
     * @param bytes
     * @param filePath
     * @param fileName
     */
    public static void bytesToFile(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * InputStream 写成文件
     *
     * @param inStream
     * @param filePath
     * @param fileName
     */
    public static void inputStreamToFile(InputStream inStream, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] buff = new byte[2048];
            int rc = 0;
            while ((rc = inStream.read(buff, 0, 2048)) > 0) {
                bos.write(buff, 0, rc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 批量重命名
     *
     * @param path
     */
    public static void multiRename(String path) {
        File file = new File(path);

        // 如果不是文件夹，就返回
        if (!file.isDirectory()) {
            System.out.println(path + "不是一个文件夹！");
            return;
        }

        // 循环遍历所有文件
        File f = null;
        String oldFileName = "";
        String[] files = file.list();
        for (String fileName : files) {
            oldFileName = fileName;

            //转化拼音名字
            String newStr = PinYinUtils.toPinYinWithToneNum(oldFileName);

            //输出地址和原路径保持一致
            f = new File(path + "\\" + oldFileName);
            f.renameTo(new File(path + "\\" + newStr));
        }
        System.out.println("恭喜，批量重命名成功！");
    }


}
