package com.hua.genshintools.win32;

import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinDef.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class VirtualInput {
    public static final Map<String, Integer> mapToVKCode = new HashMap();

    static {
        mapToVKCode.put("back", 8);
        mapToVKCode.put("tab", 9);
        mapToVKCode.put("return", 13);
        mapToVKCode.put("shift", 16);
        mapToVKCode.put("control", 17);
        mapToVKCode.put("menu", 18);
        mapToVKCode.put("pause", 19);
        mapToVKCode.put("capital", 20);
        mapToVKCode.put("escape", 27);
        mapToVKCode.put("space", 32);
        mapToVKCode.put("end", 35);
        mapToVKCode.put("home", 36);
        mapToVKCode.put("left", 37);
        mapToVKCode.put("up", 38);
        mapToVKCode.put("right", 39);
        mapToVKCode.put("down", 40);
        mapToVKCode.put("print", 42);
        mapToVKCode.put("snapshot", 44);
        mapToVKCode.put("insert", 45);
        mapToVKCode.put("delete", 46);
        mapToVKCode.put("lwin", 91);
        mapToVKCode.put("rwin", 92);
        mapToVKCode.put("numpad0", 96);
        mapToVKCode.put("numpad1", 97);
        mapToVKCode.put("numpad2", 98);
        mapToVKCode.put("numpad3", 99);
        mapToVKCode.put("numpad4", 100);
        mapToVKCode.put("numpad5", 101);
        mapToVKCode.put("numpad6", 102);
        mapToVKCode.put("numpad7", 103);
        mapToVKCode.put("numpad8", 104);
        mapToVKCode.put("numpad9", 105);
        mapToVKCode.put("multiply", 106);
        mapToVKCode.put("add", 107);
        mapToVKCode.put("separator", 108);
        mapToVKCode.put("subtract", 109);
        mapToVKCode.put("decimal", 110);
        mapToVKCode.put("divide", 111);
        mapToVKCode.put("f1", 112);
        mapToVKCode.put("f2", 113);
        mapToVKCode.put("f3", 114);
        mapToVKCode.put("f4", 115);
        mapToVKCode.put("f5", 116);
        mapToVKCode.put("f6", 117);
        mapToVKCode.put("f7", 118);
        mapToVKCode.put("f8", 119);
        mapToVKCode.put("f9", 120);
        mapToVKCode.put("f10", 121);
        mapToVKCode.put("f11", 122);
        mapToVKCode.put("f12", 123);
        mapToVKCode.put("numlock", 144);
        mapToVKCode.put("scroll", 145);
        mapToVKCode.put("lshift", 160);
        mapToVKCode.put("rshift", 161);
        mapToVKCode.put("lcontrol", 162);
        mapToVKCode.put("rcontrol", 163);
        mapToVKCode.put("lmenu", 164);
        mapToVKCode.put("rmenu", 165);
    }

    public static final int __WM_KEYDOWN = 0x100;
    public static final int __WM_KEYUP = 0x101;
    public static final HKL DWHKL = User32.INSTANCE.GetKeyboardLayout(0);
    HWND hwnd;

    public HWND findWnd() {
        String windowName = "原神";
        return hwnd = User32.INSTANCE.FindWindow("UnityWndClass", windowName);
    }

    public static void main(String[] args) throws Exception {
        String windowName = "原神";
        HWND hwnd = User32.INSTANCE.FindWindow("UnityWndClass", windowName);

        if (hwnd == null) {
            System.out.println("无窗口");
            return;
        }
        System.out.println(hwnd.getPointer().hashCode());
//        if(true)return;

        Thread.sleep(3000);
        String str = "aanads";
        new VirtualInput().play(hwnd, str);

        new VirtualInput().pressKey(hwnd, "w");
        if (hwnd == null) {
            System.out.println("the Window is missing");
        } else {
            System.out.println("get the handle");
            User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE);
        }

    }

    public void pressKey(HWND hwnd, String key) {
        keyDown(hwnd, key);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            if(!Thread.currentThread().isInterrupted())Thread.currentThread().interrupt();
        }
        keyUp(hwnd, key);
    }

    public void keyDown(HWND hwnd, String key) {
        int vkCode = getVirtualKey(key);
        int scanCode = User32.INSTANCE.MapVirtualKeyEx(vkCode, 0, DWHKL);
        WPARAM wParam = new WPARAM(vkCode);
        LPARAM lParam = new LPARAM(scanCode << 16 | 1);
        User32.INSTANCE.PostMessage(hwnd, __WM_KEYDOWN, wParam, lParam);
    }

    public void keyUp(HWND hwnd, String key) {
        int vkCode = getVirtualKey(key);
        int scanCode = User32.INSTANCE.MapVirtualKeyEx(vkCode, 0, DWHKL);
//        System.out.println("vkCode"+vkCode+"scanCode"+scanCode);
        WPARAM wParam = new WPARAM(vkCode);
        LPARAM lParam = new LPARAM(scanCode << 16 | 0XC0000001);
        User32.INSTANCE.PostMessage(hwnd, __WM_KEYUP, wParam, lParam);
    }

    public int getVirtualKey(String str) {
        if (str.length() == 1 && 31 < str.charAt(0) && str.charAt(0) < 127) {//判断可打印字符
            return User32.INSTANCE.VkKeyScanExW(str.charAt(0), DWHKL) & 0xff;
        } else {
            return mapToVKCode.get(str);
        }
    }

    private static int speed = 400;
    int delay;
    private static char[] a={'z','x','c','v','b','n','m'},
                            b={'a','s','d','f','g','h','j'},
                            c={'q','w','e','r','t','y','u'};
    Random random = new Random();
    public void play(HWND hwnd, String str) {
//        delay = speed;

        try{
            if(Pattern.matches("[ /]",str)){
                Thread.sleep(delay);
                System.out.print(str);
                return;
            }
            if(Pattern.matches("[(（]",str)){

//                Thread.sleep(delay);
//                System.out.print("[" + delay + "](");
                System.out.print(str);
                delay = random.nextInt(10);
                return;
            }
            if(Pattern.matches("[)）]",str.charAt(0)+"")){
                delay = speed;
                System.out.print(str);
                Thread.sleep(suffixResolve(1,str.toCharArray()));
                return;
            }
            char[] chars = str.toCharArray();

            if(chars.length>1){
                if(Pattern.matches("[1-7]",chars[1]+"")) {
                    //数字谱
                    if (chars[1] == '0' || chars[1] > '7') return;
                    int x=Integer.parseInt(chars[1]+"");
                    if (chars[0] == 'a' || chars[0] == 'A') {
                        pressKey(hwnd, a[x - 1] + "");
                    } else if (chars[0] == 'b' || chars[0] == 'B') {
                        pressKey(hwnd, b[x - 1] + "");
                    } else if (chars[0] == 'c' || chars[0] == 'C') {
                        pressKey(hwnd, c[x - 1] + "");
                    } else {
                        return;
                    }
                    Thread.sleep(suffixResolve(2, chars));
                    System.out.print(chars);
                    return;
                }
            }
//            if(a.toString().matches())
            if(chars[0]!='0') pressKey(hwnd,chars[0]+"");
            Thread.sleep(suffixResolve(1,chars));
            System.out.print(chars);
        }catch (Exception e){
//            if(e instanceof InterruptedException)return;
            if(! (e instanceof InterruptedException))e.printStackTrace();
            if(!Thread.currentThread().interrupted()&&e instanceof InterruptedException)
                Thread.currentThread().interrupt();
        }

    }
    public int suffixResolve(int begin,char[] chars){
        int up=1,down=1,addition=2;
        for(int i=begin;i<chars.length;i++){
            if(chars[i]=='.'||chars[i]=='·'){
                addition=3;
            }
            if(chars[i]=='-'){
                up*=2;
            }
            if(chars[i]=='_'){
                down*=2;
            }
        }
        if((up-1)*(down-1)!=0){
            return -1;
        }else{
            if(up>4){
                up=4;
            }
            System.out.print(" ["+delay*up/down*addition/2+"] ");
            return delay*up/down*addition/2;
        }
    }
    public void setBpm(int bpm){
        delay=speed=60*1000/bpm/4;
    }
    public void reset(){
        delay=speed;
    }


    public interface CLibrary extends Library {
        CLibrary INSTANCE = Native.load((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        void printf(String format, Object... args);
    }

    public interface Kernel32 extends StdCallLibrary {
        Kernel32 INSTANCE = (Kernel32)
                Native.load("kernel32", Kernel32.class);
        // Optional: wraps every call to the native library in a
        // synchronized block, limiting native calls to one at a time
        Kernel32 SYNC_INSTANCE = (Kernel32)
                Native.synchronizedLibrary(INSTANCE);
    }
}