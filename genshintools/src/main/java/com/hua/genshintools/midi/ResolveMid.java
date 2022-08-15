package com.hua.genshintools.midi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ResolveMid {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\hua\\Desktop\\genshintool\\千本樱.mid");
        FileInputStream in=new FileInputStream(file);
        FileChannel ch=in.getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(4);
        for(int i=0;i<10;i++){
            ch.read(buffer);
            buffer.flip();
            for(int j=0;j<4;j++){
//            System.out.println(buffer.get(j)>>>16&0xff+buffer.get(j)&0xff);
            System.out.format("%02x",buffer.get());
            if(j==1) System.out.print(" ");
            }

            System.out.println();
            buffer.clear();
        }
    }
}
