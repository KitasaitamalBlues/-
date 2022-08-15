package com.hua.genshintools.cache;

//import com.google.gson.Gson;

import javax.security.auth.callback.Callback;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class Cache {
    private static final Cache cache=new Cache();
    private File file = new File("./cache");
    private File error = new File("./error");
    private static  Info info;
    public void readCache() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                info=new Info();
            } else {
                FileInputStream in = new FileInputStream(file);
//                info=new Gson().fromJson(new String(in.readAllBytes(),Charset.defaultCharset()),Info.class);
                if(info==null)info=new Info();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void writeCache(){
        try{
            if(!file.exists()){
                file.createNewFile();
            }else if(info!=null){
                FileOutputStream out=new FileOutputStream(file);
//                out.write(new Gson().toJson(info,Info.class).getBytes(Charset.defaultCharset()));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void errPrint(String str){
        try{
            if(!error.exists()){
                error.createNewFile();
            }else if(info!=null){
                FileOutputStream out=new FileOutputStream(file);
                out.write(str.getBytes(Charset.defaultCharset()));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static Info getInfo(){
        return info;
    }
    public static Cache getCache(){
        return cache;
    }

}
