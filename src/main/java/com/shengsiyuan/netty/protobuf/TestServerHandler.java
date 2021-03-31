package com.shengsiyuan.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if(dataType == MyDataInfo.MyMessage.DataType.PersonType){
            System.out.println(msg.getPerson().getName()+","+msg.getPerson().getAddress()+","+msg.getPerson().getAddress());
        }else if(dataType == MyDataInfo.MyMessage.DataType.DogType){
            System.out.println(msg.getDog().getName()+","+msg.getDog().getAge());
        }else if(dataType == MyDataInfo.MyMessage.DataType.CateType){
            System.out.println(msg.getCat().getName()+","+msg.getCat().getAge());
        }
    }
}
