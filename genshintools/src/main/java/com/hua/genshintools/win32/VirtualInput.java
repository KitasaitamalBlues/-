package com.hua.genshintools.win32;

import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinDef.*;
import java.util.HashMap;
import java.util.Map;

public class VirtualInput {
    public static final Map<String,Integer> mapToVKCode=new HashMap();
    static{
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
    public static final HKL DWHKL=User32.INSTANCE.GetKeyboardLayout(0);
    HWND hwnd;
    public HWND findWnd(){
        String windowName="原神";
        return hwnd=User32.INSTANCE.FindWindow("UnityWndClass",windowName);
    }
    public static void main(String[] args) throws Exception {
        String windowName="原神";
        HWND hwnd=User32.INSTANCE.FindWindow("UnityWndClass",windowName);

        if(hwnd==null){
            System.out.println("无窗口");
            return;
        }
        System.out.println(hwnd.getPointer().hashCode());
//        if(true)return;

        Thread.sleep(3000);
        String str=
                "aanads\n" +
                        "\n" +
                        "addbmn\n" +
                        "\n" +
                        "amnnasdb\n" +
                        "\n" +
                        "cbbcgd\n" +
                        "\n" +
                        "dsnads\n" +
                        "\n" +
                        "addbmn\n" +
                        "\n" +
                        "amnnddas\n" +
                        "\n" +
                        "addsdn\n" +
                        "\n" +
                        "dgh dhg\n" +
                        "\n" +
                        "ghg dsd\n" +
                        "\n" +
                        "dsanads\n" +
                        "\n" +
                        "dhhhas\n" +
                        "\n" +
                        "dgh dhg\n" +
                        "\n" +
                        "ghgghd\n" +
                        "\n" +
                        "dsanasdb\n" +
                        "\n" +
                        "bcbbmn";
        new VirtualInput().play(hwnd,str);

        new VirtualInput().pressKey(hwnd,"w");
        if(hwnd==null){
            System.out.println("the Window is missing");
        }else{
            System.out.println("get the handle");
            User32.INSTANCE.ShowWindow(hwnd,WinUser.SW_RESTORE);
        }

    }

    public void pressKey(HWND hwnd,String key){
        keyDown(hwnd,key);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        keyUp(hwnd,key);
    }
    public void keyDown(HWND hwnd,String key){
        int vkCode=getVirtualKey(key);
        int scanCode=User32.INSTANCE.MapVirtualKeyEx(vkCode,0,DWHKL);
        WPARAM wParam=new WPARAM(vkCode);
        LPARAM lParam=new LPARAM(scanCode<<16| 1);
        User32.INSTANCE.PostMessage(hwnd,__WM_KEYDOWN,wParam,lParam);
    }
    public void keyUp(HWND hwnd,String key){
        int vkCode=getVirtualKey(key);
        int scanCode=User32.INSTANCE.MapVirtualKeyEx(vkCode,0,DWHKL);
//        System.out.println("vkCode"+vkCode+"scanCode"+scanCode);
        WPARAM wParam=new WPARAM(vkCode);
        LPARAM lParam=new LPARAM(scanCode<<16| 0XC0000001);
        User32.INSTANCE.PostMessage(hwnd,__WM_KEYUP,wParam,lParam);
    }
    public int getVirtualKey(String str){
        if(str.length()==1&&31<str.charAt(0)&&str.charAt(0)<127){//判断可打印字符
            return User32.INSTANCE.VkKeyScanExW(str.charAt(0),DWHKL)&0xff;
        }else{
            return mapToVKCode.get(str);
        }
    }
    private static final int speed=150;

    public void play(HWND hwnd,String str){
        int delay=speed;
        StringBuilder custom=null;
        boolean b=false;
        for(char ch:str.toCharArray()){
            System.out.println(ch);
            if(ch=='['||ch=='【'){
                b=true;
                custom=new StringBuilder();
                custom.append('0');
                System.out.println("[");
            }
            if(b&&ch<'9'&&ch>'0'){
                custom.append(ch);
                System.out.println("0-9");
            }
            if(ch=='('||ch=='（'){
                delay=0;
                System.out.println("(");
                continue;
            }
            if(ch==')'||ch=='）'){
                delay=speed;
                System.out.println(")");
                continue;
            }
            if((ch<'z'||ch<'Z')&&(ch>'a'||ch>'A')){
                pressKey(hwnd,ch+"");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("a-z");
                continue;
            }
            try {
                if(ch==' '){
                    Thread.sleep(50);
                    System.out.println(" ");
                }
                if(ch=='\n'){
                    Thread.sleep(2*delay);
                    System.out.println("\\n");
                }
                if(ch==']'||ch=='】'&&Integer.parseInt(custom.toString())<300){
                    Thread.sleep(Integer.parseInt(custom.toString())*100);
                    System.out.println("]");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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