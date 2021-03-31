package com.shengsiyuan.nio;

import java.nio.ByteBuffer;

public class NioTestBufferSlice {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        for(int i = 0; i<buffer.capacity();++i){
            buffer.put((byte)i);
        }
        buffer.position(2);
        buffer.limit(6);
        ByteBuffer sliceBuffer = buffer.slice();

        for(int i = 0; i< sliceBuffer.capacity();++i){
            byte b = sliceBuffer.get(i);
            b *= 2;
            sliceBuffer.put(i,b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}