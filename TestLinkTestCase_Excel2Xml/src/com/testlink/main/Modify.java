package com.testlink.main;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-23
 * Time: 上午10:05
 * To change this template use File | Settings | File Templates.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream; //import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
public class Modify {
    public static void replaceString(String path, String oldstring, String newstring) throws IOException{
        try {
            BufferedReader bufReader =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(
                                            new File(path))));
            StringBuffer strBuf = new StringBuffer();
            for (String tmp1 = null; (tmp1 = bufReader.readLine()) != null; tmp1 = null) {
                // 替换操作
                String tmp = new String(tmp1.toString().getBytes("GBK"));
                tmp = tmp.replace(oldstring, newstring);
                System.out.println(tmp);
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }
            bufReader.close();
            String  strBuf2 = new String(strBuf.toString().getBytes("GBK"));
            //      System.out.println(strBuf2);
            PrintWriter printWriter = new PrintWriter(path);
            printWriter.write(strBuf2.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] s) throws IOException {
////        ReadWriteFile.creatTxtFile();
////        ReadWriteFile.readTxtFile();
////        ReadWriteFile.writeTxtFile("20080808:12:13");
////        ReadWriteFile.replaceTxtByStr("&lt;", "teset");
////        ReadWriteFile.replaceTxtByStr("test;", "tet");
//        Modify.replaceString("testcase.xml", "test", "tst");
//    }
}