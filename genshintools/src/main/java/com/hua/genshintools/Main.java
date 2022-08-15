package com.hua.genshintools;

import com.hua.genshintools.cache.Cache;
import com.sun.jna.platform.win32.*;

public class Main {
    public static void main(String[] args) {
//        User32.INSTANCE.CreateWindowEx()
//        Kernel32.INSTANCE.CreateProcessW()
        Cache.getCache().readCache();
        System.out.println(System.getenv("user.dir"));
        HelloApplication.main(new String[0]);
    }
}
