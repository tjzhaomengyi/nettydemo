package com.shengsiyuan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOTestSelector {
    public static void main(String[] args) throws IOException {
        int[] ports = new int[5];

        ports[0]=5000;
        ports[1]=5001;
        ports[2]=5002;
        ports[3]=5003;
        ports[4]=5004;

        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; ++i){
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();//创建NIO的channel
            serverSocketChannel.configureBlocking(false);//severSocketChannel有个特别重要的方法，配置不阻塞方式，
            ServerSocket serverSocket = serverSocketChannel.socket();//构造ServerSocket对象，与这个channel关联的server socket
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            serverSocket.bind(address);//将地址和serverSocket进行绑定操作。

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//将channel注册到selector上，并返回一个selection Key；通过selection key可以找到关联的key

            System.out.println("监听端口："+ports[i]);
        }

        //NIO监听客户端
        while(true){
            int numbers = selector.select();
            System.out.println("numbers:"+numbers);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();//返回selected-key set
            System.out.println("selectedKeys:"+selectionKeys);
            Iterator<SelectionKey> iter = selectionKeys.iterator();
            while(iter.hasNext()){
                SelectionKey selectionKey = iter.next();
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();//拿到selectionkey关联的channel对象
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector,SelectionKey.OP_READ);//真正连接，并从端口进行读操作
                    iter.remove();
                    System.out.println("获取客户端连接"+socketChannel);
                } else if(selectionKey.isReadable()){
                    //读数据
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    int byteRead = 0;
                    while (true){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);
                        if(read <= 0){
                            break;
                        }
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        byteRead += read;
                    }
                    System.out.println("读取数据:"+byteRead+"，来自于："+socketChannel);
                    iter.remove();//一定要手动进行remove
                }
            }
        }

    }
}
