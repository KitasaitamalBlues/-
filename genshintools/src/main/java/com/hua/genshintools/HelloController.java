package com.hua.genshintools;

import com.hua.genshintools.cache.Cache;
import com.hua.genshintools.win32.HotKey;
import com.hua.genshintools.win32.VirtualInput;
import com.sun.jna.platform.win32.WinDef;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private Button chooseFile;
    @FXML
    private Button binding;
    @FXML
    private Label HotKeyInfo;
    @FXML
    private Label handle;
    @FXML
    private Label file;
    @FXML
    private Label status;
    @FXML
    private Button btnStatus;
    private HotKey hotKey=new HotKey();
    private VirtualInput vInput=new VirtualInput();
    private WinDef.HWND hwnd;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    File choosed;
    @FXML
    protected void select(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("文本文档","*.txt")
        );
        choosed = fileChooser.showOpenDialog(chooseFile.getScene().getWindow());

        if(choosed!=null){
            file.setText(choosed.getName());
//            Cache.getInfo().lastOpen=choosed.getName();
//            Cache.getCache().writeCache();
        }
//        System.out.println(choosed.getName());

    }
    /*

fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))

fileChooser.getExtensionFilters().addAll(
    new FileChooser.ExtensionFilter("All Images", "*.*")
);
     */

    @FXML
    protected void bind(){
        //todo
        hwnd=vInput.findWnd();
        if(hwnd!=null)handle.setText("原神"+hwnd.getPointer().hashCode());
        else handle.setText("原神未打开");
    }

    Runnable runnable=new Runnable(){
        StringBuilder temp=new StringBuilder();
        @Override
        public void run() {
                if(choosed!=null&&choosed.exists()&&hwnd!=null){

                    //读取
                    FileInputStream in;
                    FileChannel ch;
                    try{
                        in=new FileInputStream(choosed);
                        ch=in.getChannel();
                        temp.delete(0,temp.capacity());
                        ByteBuffer buffer=ByteBuffer.allocate(1024);
                        vInput.reset();
                        for(;ch.read(buffer)>0;){
                            if(Thread.currentThread().isInterrupted()){
                                break;
                            }
                            buffer.flip();
                            resolve(buffer);
                            buffer.clear();
                            if(Thread.currentThread().isInterrupted()){
                                break;
                            }
                        }
                        buffer.clear();
                        buffer.put((byte)0xff);
                        buffer.flip();
                        resolve(buffer);
                        findBpm=true;
                        temp.delete(0,temp.capacity());
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                Platform.runLater(()->{
                    status.setText("已停止");
                });
        }
        boolean flag=false;
        boolean findBpm=true;
        int bpm;
        void resolve(ByteBuffer buffer){
            if(buffer.get(0)==(byte)0xff){
                vInput.play(hwnd,temp.toString());
                return;
            }
            CharsetDecoder decoder=Charset.defaultCharset().newDecoder();
            String str = new String(buffer.array(), Charset.defaultCharset());
            try {
                str=decoder.decode(buffer).toString();
            } catch (CharacterCodingException e) {
                throw new RuntimeException(e);
            }
            for(char ch:str.toCharArray()){
                if(Thread.currentThread().isInterrupted())return;
                if(findBpm&&Pattern.matches("[0-9\\[]",ch+"")){
                    if(ch!='['&&ch!='【') temp.append(ch);
                    continue;
                }
                if(findBpm&&Pattern.matches("[\\]】]",ch+"")){
                    try{
                        bpm=Integer.parseInt(temp.toString());
                        vInput.setBpm(bpm);
                        findBpm=false;
                        temp.delete(0,temp.capacity());
                        System.out.println("bpm:"+bpm);
                    }catch (Exception e){
                        String log=file.getText()+":bpm解析错误  "+Calendar.getInstance().getTime();
                        Cache.getCache().errPrint(log);
                        System.out.println(log);
                    }
                    continue;
                }
                if(Pattern.matches("[a-zA-Z0\\(\\)（） ]",ch+"")){//字母为边界
                    if(findBpm){
                        temp.delete(0,temp.capacity());
                        findBpm=false;
                    }
                    if(temp.toString().length()>0){
                        vInput.play(hwnd,temp.toString());
                        temp.delete(0,temp.capacity());
                    }
                    temp.append(ch);
                    continue;
                }
                if(ch=='\n'||ch=='\r'){
                    System.out.println();
                    continue;
                }
                temp.append(ch);
            }
        }
    };
    Thread playProc;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //读取记录
//        if(Cache.getInfo().lastOpen!=null){
//            if(new File("./"+Cache.getInfo().lastOpen).exists())
//        choosed=new File("./"+Cache.getInfo().lastOpen);
//            file.setText(choosed.getName());
//        }
        //初始化 绑定热键 获得原神句柄
        if(hotKey.registerHotKey()){
            HotKeyInfo.setText("绑定热键F9");
            //todo 判断运行暂停
            hotKey.unregisterHotKey();
            Thread detect = new Thread(()->{
                hotKey.registerHotKey();
               while(true){
                   hotKey.waitHotKey();
                   changeStatus();
               }
            });
            detect.setDaemon(true);
            detect.start();
        }
        else {
            HotKeyInfo.setText("热键绑定失败,换用延时启动");
            btnStatus.setVisible(true);
            btnStatus.setManaged(true);
        }
        bind();
    }
    void changeStatus(){
        if(playProc!=null&&playProc.isAlive()){
            playProc.interrupt();
            Platform.runLater(()->{
                status.setText("已停止");
            });
        }else{
            playProc=new Thread(runnable);
            playProc.setDaemon(true);
            playProc.start();
            Platform.runLater(()->{
                status.setText("播放中");
            });
        }
    }
    @FXML
    protected void btnChange(){
        if(playProc!=null&&playProc.isAlive()){
            playProc.interrupt();
            btnStatus.setText("3s后启动");
        }else {
            playProc = new Thread(() -> {
                try {
                    for (int i = 0; i < 3; i++) {
                        final int var=3-i;
                        Platform.runLater(() -> {
                            btnStatus.setText(var+"");
                        });
                        Thread.currentThread().sleep(1000);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    status.setText("播放中");
                    btnStatus.setText("暂停");
                });
                runnable.run();
            });
            playProc.setDaemon(true);
            playProc.start();
        }
    }
}