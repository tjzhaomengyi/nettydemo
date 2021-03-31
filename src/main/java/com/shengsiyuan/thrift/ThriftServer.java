package com.shengsiyuan.thrift;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import thrift.generated.PersonService;

public class ThriftServer {
    public static void main(String[] args) throws TTransportException {
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);//ThriftSocket对象
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);//线程数
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        arg.protocolFactory(new TCompactProtocol.Factory());//工厂对象，协议层用到的协议对象，压缩二进制字节码的协议
        arg.transportFactory(new TFramedTransport.Factory());//网络传输层用到的对象
        arg.processorFactory(new TProcessorFactory(processor));//

        TServer server = new THsHaServer(arg);//使用半同步半异步
        System.out.println("Thrift Server start");
        server.serve();
    }
}
