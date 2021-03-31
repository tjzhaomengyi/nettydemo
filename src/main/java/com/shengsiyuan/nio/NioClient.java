package com.shengsiyuan.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8899));

        while(true){
            selector.select();
            Set<SelectionKey> keySet = selector.selectedKeys();
            for(SelectionKey selectionKey:keySet){
                if(selectionKey.isConnectable()){//和服务器建立好连接，然后在新的线程中读取键盘输入，把内容写给服务端
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    if(client.isConnectionPending()){ //连接是否在进行中，此时连接真正建立好，客户端可以向服务端发送数据了
                        client.finishConnect();
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put((LocalDateTime.now()+"连接成功").getBytes());
                        writeBuffer.flip();
                        client.write(writeBuffer);
                        ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());//启动线程池,监听键盘输入，客户端发送数据
                        executorService.submit(()->{
                           while (true){
                               try{
                                   writeBuffer.clear();
                                   InputStreamReader input = new InputStreamReader(System.in);
                                   BufferedReader br = new BufferedReader(input);
                                   String sendMessage = br.readLine();

                                   writeBuffer.put(sendMessage.getBytes());//放到writebuffer中
                                   writeBuffer.flip();
                                   client.write(writeBuffer);
                               }catch (Exception ex){
                                   ex.printStackTrace();
                               }
                           }
                        });
                    }
                    client.register(selector,selectionKey.OP_READ);//先注册读事件，然后再执行读取操作
                } else if(selectionKey.isReadable()){
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int count = client.read(readBuffer);
                    if(count > 0){
                        String receiveMessage = new String(readBuffer.array(),0,count);
                        System.out.println(receiveMessage);
                    }
                }
            }
            keySet.clear();
        }
    }
}
