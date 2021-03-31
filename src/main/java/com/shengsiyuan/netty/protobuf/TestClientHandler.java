package com.shengsiyuan.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int randomInt = new Random().nextInt(3);

        MyDataInfo.MyMessage msg = null;

        if (0 == randomInt){
            msg = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.PersonType)
                    .setPerson(MyDataInfo.Person.newBuilder().setName("张三").setAge(10).setAddress("日本").build())
                    .build();
        }else if(1==randomInt){
            msg = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.DogType)
                    .setDog(MyDataInfo.Dog.newBuilder().setName("狗").setAge(2).build()).build();
        }else{
            msg = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.CateType)
                    .setCat(MyDataInfo.Cat.newBuilder().setName("猫").setAge(3).build()).build();
        }



        //MyDataInfo.Person person = MyDataInfo.Person.newBuilder().setName("张三")
          //      .setAge(20).setAddress("北京").build();
        ctx.channel().writeAndFlush(msg);
    }
}
