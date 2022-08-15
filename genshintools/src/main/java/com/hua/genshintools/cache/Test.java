package com.hua.genshintools.cache;


import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.regex.Pattern;

public class Test implements Runnable{
//    public static void main(String[] args) {
//        System.out.println(Pattern.matches("[a-z\\(\\) ]", " "));
//        byte b=(byte)0xff;
//        b&=0xff;
//        System.out.println(b==(byte)0xff);
//        char ch='\n';
//        if(('a'<ch&&ch<'z')||('A'<ch&&ch<'Z')){
//            System.out.println("true");
//        }else System.out.println("false");
//        System.out.println(b);
//
//        System.out.println(Pattern.matches("[a-zA-Z0\\(\\)（） ]", '0'+""));
//    }
    //todo true||false&&

    @Override
    public void run() {
        //如果中断了,则跳出执行
        while(true){
            if(Thread.currentThread().isInterrupted()){
                System.out.println("收到中断停止执行");
                break;
            }
            reInterrupt();
        }
    }

    private void reInterrupt(){
        //模拟内部中断
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().isInterrupted());
            e.printStackTrace();
            //检测中断状态
            System.out.println(Thread.currentThread().isInterrupted());
            //添加此行代码,因为虽然中断了,线程中断被捕获,然后中断消息也会被清除,
            //所以加入Thread.currentThread.interrupt();
            //手动恢复中断状态
            Thread.currentThread().interrupt();
            //检测中断状态是否恢复
            System.out.println(Thread.currentThread().isInterrupted());
        }
    }

    public static void main(String[] args) {
        Thread thread=new Thread(new Test());
        thread.start();
        try {
            //先休眠,然后再让线程中断
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

}
