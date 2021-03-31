package com.shengsiyuan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/***
 * 使用一个通道，服务端只有一个线程，进行多个客户端的通信
 */
public class NioServer {
    //服务端要保留客户端的连接信息即可
    private static Map<String,SocketChannel> clientMap = new HashMap();
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();//channel对应的监听socket端口
        serverSocket.bind(new InetSocketAddress(8899));

        //创建selector对象
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//将serverSocketChannel注册到selector上，然后将Accept添加到Selection key set中

        //服务器监听，事件处理
        while(true){
            selector.select();//这就可以获取到selectionKeys
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeys.forEach(selectionKey -> {
                final SocketChannel client;
                try {
                    if(selectionKey.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                        client = server.accept();//连接已经获取，向selector中注册client
                        client.configureBlocking(false);
                        client.register(selector,SelectionKey.OP_READ);

                        //将客户端的消息注册到服务器端
                        String key = "【" + UUID.randomUUID().toString() + "】";
                        clientMap.put(key,client);
                    }else if(selectionKey.isReadable()){
                        //向所有连接的客户端发送数据，服务器把消息广播出去
                        client = (SocketChannel)selectionKey.channel();//通过注册进来的client进行读取操作，client对象时SocketChannel类型，所以这里做转换要用SocketChannel类型
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                        int count = client.read(readBuffer);

                        if (count>0){
                            //把readbuffer的内容广播出去
                            readBuffer.flip();

                            Charset charset = Charset.forName("utf-8");
                            String receiveMessage = String.valueOf(charset.decode(readBuffer).array());
                            System.out.println(client+":"+receiveMessage);

                            //找到client对应的key
                            String senderKey = null;
                            for(Map.Entry<String,SocketChannel> entry:clientMap.entrySet()){
                                if(client == entry.getValue()){
                                    senderKey = entry.getKey();
                                    break;
                                }
                            }

                            //向所有客户端发送消息
                            for(Map.Entry<String,SocketChannel> entry:clientMap.entrySet()){
                                SocketChannel value = entry.getValue();

                                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                writeBuffer.put((senderKey + ":" + receiveMessage).getBytes());
                                writeBuffer.flip();

                                value.write(writeBuffer);
                            }
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });
            selectionKeys.clear();//SelectionKeys在使用完一定要进行删除，否则下一次循环内部还对这个selection进行处理，这样就会出现空错误
        }
    }
}
