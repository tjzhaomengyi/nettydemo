package com.shengsiyuan.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

public class NioTest1 {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);

        System.out.println("capacity:"+buffer.capacity());

        for(int i = 0; i< 5; ++i){
            int randomNum = new SecureRandom().nextInt(20);
            buffer.put(randomNum);//往NIO的buffer里写入数据，类似io的读操作
        }

        System.out.println("before flip limit:"+buffer.limit());

        buffer.flip();

        System.out.println("after flip limit:"+buffer.limit());

        System.out.println("enter while loop");
        while (buffer.hasRemaining()){
            System.out.println("pos:"+buffer.position() + ",limit:"+buffer.limit()+"cap:"+buffer.capacity());
            System.out.println(buffer.get());//从NIO的buffer中取出数据，类似io的写操作
        }
    }
}
