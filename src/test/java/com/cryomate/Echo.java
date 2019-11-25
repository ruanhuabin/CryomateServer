package com.cryomate;

import java.io.*;

public class Echo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Hello, eric");
		
		FileOutputStream out = null;
        FileOutputStream outSTr = null;
        BufferedOutputStream Buff = null;
        FileWriter fw = null;

        int count = 1000;//写文件行数

        try {
            //经过测试：FileOutputStream执行耗时:17，6，10 毫秒
            out = new FileOutputStream(new File("E:\\add.txt"));
            long begin = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                out.write("测试java 文件操作\r\n".getBytes());
            }
            out.close();
            long end = System.currentTimeMillis();
            System.out.println("FileOutputStream:" + (end - begin) + " ");

            //经过测试：ufferedOutputStream执行耗时:1,1，1 毫秒
            outSTr = new FileOutputStream(new File("E:\\add1.txt"));
            Buff = new BufferedOutputStream(outSTr);
            long begin0 = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                Buff.write("测试java 文件操作\r\n".getBytes());
            }
            Buff.flush();
            Buff.close();
            long end0 = System.currentTimeMillis();
            System.out.println("BufferedOutputStream:" + (end0 - begin0) + " ");

            //经过测试：FileWriter执行耗时:3,9，5 毫秒
            fw = new FileWriter("E:\\add2.txt");
            long begin3 = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                fw.write("测试java 文件操作\r\n");
            }
            fw.close();
            long end3 = System.currentTimeMillis();
            System.out.println("FileWriter:" + (end3 - begin3) + " ");
            
            System.err.println("error print\n");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
                Buff.close();
                outSTr.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

	}

}
