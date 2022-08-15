package com.hua.genshintools.win32;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
public class HotKey {

    int id1=105;
    public Boolean registerHotKey(){
        return User32.INSTANCE.RegisterHotKey(null,id1,0,120);
    }
    public Boolean unregisterHotKey(){
        return User32.INSTANCE.UnregisterHotKey(null,id1);
    }
    public void waitHotKey(){
        WinUser.MSG msg=new WinUser.MSG();
        if(User32.INSTANCE.GetMessage(msg,null,0,0)!=0){
            System.out.println("接受到热键");
//            System.out.println("start to Unregister");
        }
        else{
            System.out.println("WM_QUIT");
        }
    }
    public static void main(String[] args) {
        HotKey hotKey = new HotKey();
        hotKey.registerHotKey();
        WinUser.MSG msg=new WinUser.MSG();
//        while(true){
            if(User32.INSTANCE.GetMessage(msg,null,0,0)!=0){
                System.out.println("接受到热键");
                System.out.println("start to Unregister");
                hotKey.unregisterHotKey();
//                break;
            }
            else{
                System.out.println("WM_QUIT");
                hotKey.unregisterHotKey();
//                break;
            }
        }
//    }

}
